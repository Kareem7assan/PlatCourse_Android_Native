package com.platCourse.platCourseAndroid.home.course_details

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kotvertolet.youtubejextractor.JExtractorCallback
import com.github.kotvertolet.youtubejextractor.YoutubeJExtractor
import com.github.kotvertolet.youtubejextractor.exception.YoutubeRequestException
import com.github.kotvertolet.youtubejextractor.models.newModels.AdaptiveFormatsItem
import com.github.kotvertolet.youtubejextractor.models.newModels.VideoPlayerConfig
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentDetailsCourseBinding
import com.platCourse.platCourseAndroid.home.course_details.adapter.QualityAdapter
import com.platCourse.platCourseAndroid.home.course_details.dialog.CouponBottomDialog
import com.platCourse.platCourseAndroid.home.course_details.dialog.CouponPurchaseBottomDialog
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.attribute_course_model.Action
import com.rowaad.app.data.model.attribute_course_model.CourseListener
import com.rowaad.app.data.model.attribute_course_model.ItemCourseDetails
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CourseDetailsFragment : BaseFragment(R.layout.fragment_details_course), MotionLayout.TransitionListener, CourseListener, ExoPlayer.AudioOffloadListener, DisplayManager.DisplayListener {

    private var isShowQuality: Boolean = false
    private var videoQuality: String?=null
    private var audioQuality: String?=null
    private var audioManager: AudioManager? = null
    private var videoUrl: String? = null
    private var youtube: Boolean? = null
    private var isPlay: Boolean = false

    var simplePlayer: ExoPlayer? = null

    private var details: CourseItem? = null
    private var lessonId:Int? = null
    private var isWatched=false
    private var isPlayedForFirstTime=false
    private val binding by viewBinding<FragmentDetailsCourseBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()

    private var initialTime = 0L
    private var updateTime:Long? = null
    private var currentTime:Long? = null
    private val adapter by lazy { AttributeAdapter(this) }
    private val qualityAdapter by lazy { QualityAdapter() }
    object SharedPlayer{
        var lastPos=0L
    }
    private var playWhenReady = true

    private  val TAG = "CourseDetailsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        details=arguments?.getString("details")?.fromJson<CourseItem>()
        videoUrl=arguments?.getString("url")
        lessonId=arguments?.getInt("lesson_id")
        youtube=arguments?.getBoolean("youtube")
        initPlayer()
        handleWaterMark()
        setupRec()
        sendRequestUser()
        handleCourses()
        setupActions()
        handleBuyObservable()
        handleCouponObservable()
        handleContactObservable()
        handleMuting()
        requireActivity().handleCustomBack(this){
            findNavController().navigateUp()
            resetTime()
        }

    }

    private fun resetTime() {
        viewModel.lastPos = 0
        SharedPlayer.lastPos = 0
        Log.e("pos1",SharedPlayer.lastPos.toString()+","+viewModel.lastPos)
    }


    private fun handleMuting() {
             val displayManager = requireActivity().getSystemService(AppCompatActivity.DISPLAY_SERVICE) as DisplayManager
             displayManager.registerDisplayListener(this, null)

         }


         override fun onResume() {
             super.onResume()
             requireActivity().volumeControlStream=AudioManager.STREAM_MUSIC
            /* if (Util.SDK_INT < 24) {
                 initPlayer()
             }*/
             Log.e("seek",SharedPlayer.lastPos.toString())
         }

    override fun onStart() {
        super.onStart()
        /*if (Util.SDK_INT >= 24) {
            initPlayer()
        }*/
    }

    private fun startMute(){
        audioManager?.setStreamMute(AudioManager.STREAM_MUSIC, true)
        audioManager?.isMicrophoneMute=true
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            audioManager?.adjustVolume(
                    AudioManager.ADJUST_MUTE,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
            )
        }

    }

    private fun enableSound() {
        if (audioManager==null){
            audioManager=requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
        audioManager?.isMicrophoneMute=false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            audioManager?.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_PLAY_SOUND)
        }


    }

    private fun handleCouponObservable() {
        handleSharedFlow(viewModel.couponFlow, onSuccess = {
            showSuccessMsg(it as String)
        })
        handleSharedFlow(viewModel.couponPurchaseFlow, onSuccess = {
            showSuccessMsg(getString(R.string.purchase_successfully)).also { findNavController().navigateUp() }
        })
    }

    private fun handleContactObservable() {
        handleSharedFlow(viewModel.contactFlow, onSuccess = {
            it as UserModel
            IntentUtils.openWhatsappIntent(
                    if (it.phone_number?.startsWith("+2") == true) it.phone_number
                            ?: "" else ("+2" + it.phone_number), requireContext()
            )
        })
    }

    private fun handleBuyObservable() {
        handleSharedFlow(viewModel.buyCourseFlow, onSuccess = {
            it
            showSuccessMsg(getString(R.string.request_sent))
        })
    }

    private fun setupActions() {
        binding.buyBtn.setOnClickListener {
            viewModel.sendRequestBuyCourse(courseId = details?.id ?: 0)
        }
        binding.couponBtn.setOnClickListener {
            showCouponDialog()
        }
        binding.couponPurchaseBtn.setOnClickListener {
            showCouponPurchaseDialog()
        }
        binding.contactBtn.setOnClickListener {
            viewModel.sendRequestTeacher(details?.ownerId ?: 0)
        }

        binding.qualityLay.setOnClickListener {
            isShowQuality=isShowQuality.not()
            if (isShowQuality){
                binding.rvQualities.show().also { binding.tvQuality.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.ic_arrow_up_24),null) }
            }
            else{
                binding.rvQualities.hide().also { binding.tvQuality.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(requireContext(),R.drawable.ic_arrow_down_24),null) }
            }

        }
    }

    private fun showCouponPurchaseDialog() {
        CouponPurchaseBottomDialog{
            sendRequestPurchase(it)
        }.show(requireActivity().supportFragmentManager, "coupon")
    }

    private fun sendRequestPurchase(code: String) {
        viewModel.sendRequestCouponPurchase(details?.id ?: 0, code)

    }

    private fun showCouponDialog() {
        CouponBottomDialog{
            sendRequestCoupon(it)
        }.show(requireActivity().supportFragmentManager, "coupon")
    }

    private fun sendRequestCoupon(coupon: String) {
        viewModel.sendRequestCoupon(details?.id ?: 0, coupon)
    }

    private fun sendRequestUser() {
        viewModel.sendRequestUser()
    }

    private fun handleCourses() {
        handleSharedFlow(viewModel.userFlow, onSuccess = {
            it as UserModel
            setupAdapter()
        })
    }

    private fun handleWaterMark() {
        if (viewModel.isUserLogin()){
            binding.tvOwner.text = viewModel.getUser()?.username
            binding.tvPhone.text = viewModel.getUser()?.phone_number
            binding.markLay.show()
        }
        else {
            binding.markLay.hide()
        }
    }

    private fun setupAdapter() {
        val items=mutableListOf<ItemCourseDetails>()
        items.add(ItemCourseDetails(title = getString(R.string.desc), desc = details?.overview))
        showBuyButton()
        if (viewModel.isUserLogin() && viewModel.isCourseOwner(details?.id ?: 0)) {
            items.add(
                    ItemCourseDetails(
                            title = getString(R.string.rates),
                            showForward = true,
                            type = Action.RATES
                    )
            )
            showContactButton()
        }

        if (viewModel.isUserLogin() && viewModel.isCourseOwner(details?.id ?: 0)) {
            items.add(
                    ItemCourseDetails(
                            title = getString(R.string.lessons),
                            showForward = true,
                            type = Action.LESSONS
                    )
            )
            items.add(
                    ItemCourseDetails(
                            title = getString(R.string.discussions),
                            showForward = true,
                            type = Action.DISCUSSIONS
                    )
            )
            items.add(
                    ItemCourseDetails(
                            title = getString(R.string.quizes),
                            showForward = true,
                            type = Action.QUIZES
                    )
            )
            items.add(
                    ItemCourseDetails(
                            title = getString(R.string.files),
                            showForward = true,
                            type = Action.FILES
                    )
            )
            items.add(
                    ItemCourseDetails(
                            title = getString(R.string.anouneces),
                            showForward = true,
                            type = Action.ADS
                    )
            )
        }
        adapter.swapData(items)
    }

    private fun showContactButton() {
        if ( details?.ownerId!=null && details?.ownerId != 0 ) binding.contactBtn.show()
        else binding.contactBtn.hide()
        binding.contactBtn.show()
        binding.buyBtn.hide()
        binding.couponBtn.hide()
        binding.couponPurchaseBtn.hide()
    }

    private fun showBuyButton() {
        binding.contactBtn.hide()
        binding.buyBtn.show()
        binding.couponBtn.show()
        binding.couponPurchaseBtn.show()
    }

    private fun setupRec() {
        binding.rvAttributes.layoutManager=LinearLayoutManager(requireContext())
        binding.rvAttributes.adapter=adapter
    }


    private fun setupVideoListener() {
        simplePlayer?.addListener(object : Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) {
               // super.onPlaybackStateChanged(playbackState)
                handlePlayBackState(playbackState)
                /*trackSelector?.generateQualityList()?.let {
                    qualityList = it
                    setUpQualityList()
                }*/
            }


            override fun onIsPlayingChanged(isPlaying: Boolean) {
                this@CourseDetailsFragment.isPlay = isPlaying
                Log.e("onIsPlaying",isPlaying.toString())
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying && viewModel.isUserLogin()) {
                    if (!isPlayedForFirstTime) {
                        //change flag
                        isPlayedForFirstTime = true
                        initialTime = System.currentTimeMillis().div(SECOND_DURATION_INTERVAL)
                    }
                    else {
                        if (updateTime != null) {
                            initialTime =
                                    System.currentTimeMillis().div(SECOND_DURATION_INTERVAL).minus(
                                            updateTime ?: 0L
                                    )
                            updateTime = null
                        }
                    }
                    binding.styledVideo.postDelayed(
                            this@CourseDetailsFragment::calculatePlayingTime,
                            SECOND_DURATION_INTERVAL
                    )
                    playingWaterMark()

                }
                if (!isPlaying) {
                    updateTime = currentTime?.minus((initialTime))
                }
            }

        })

        binding.styledVideo.setFullscreenButtonClickListener {

             startActivityForResult(Intent(requireContext(), FullScreenActivity::class.java).also {
                 Log.e("currentPosition",simplePlayer!!.currentPosition.toString())
                 it.putExtra("isPlay", simplePlayer!!.isPlaying)
                 it.putExtra("name", viewModel.getUser()?.username)
                 it.putExtra("url", videoUrl ?: details?.intro!!)
                 it.putExtra("youtube", youtube)
                 it.putExtra("speed", simplePlayer?.playbackParameters?.speed ?: 1f)
                 it.putExtra("pos", simplePlayer!!.currentPosition)
                 it.putExtra("lesson_id", lessonId)

             },500).also {
                 releasePlayer()
             }
        }



    }

    private fun handlePlayBackState(playbackState: Int) {
        Log.e("playBack",playbackState.toString())

        when(playbackState){
            ExoPlayer.STATE_IDLE->{
                binding.progressVideo.isVisible=true
            }
            ExoPlayer.STATE_BUFFERING->{
                binding.progressVideo.isVisible=true
            }
            ExoPlayer.STATE_READY->{
                binding.progressVideo.isVisible=false
            }

            ExoPlayer.STATE_ENDED->{

            }
        }
    }


    private fun initPlayer(speed: Float=1f) {

        val MIN_BUFFER_DURATION = 1000/*3000*/ // 3 seconds

        val MAX_BUFFER_DURATION = 2000/*8000*/ // 8 seconds

        val MIN_PLAYBACK_RESUME_BUFFER = 500/*1500*/ // 1.5 seconds

        val MIN_PLAYBACK_START_BUFFER = 500 // 0.5 seconds


        val allocatorSize = 2 * 1024 * 1024; // 2MB
        val allocator =  DefaultAllocator(true, allocatorSize)
        val loadControl =  DefaultLoadControl.Builder().setAllocator( DefaultAllocator(true, 16))
                                                       .setBufferDurationsMs(MIN_BUFFER_DURATION, MAX_BUFFER_DURATION, MIN_PLAYBACK_START_BUFFER, MIN_PLAYBACK_RESUME_BUFFER)
                                                       .setTargetBufferBytes(-1)
                                                       .setPrioritizeTimeOverSizeThresholds(true)
                                                       .createDefaultLoadControl()

        simplePlayer = ExoPlayer.Builder(requireContext(),getDrf())
                .setTrackSelector(getTrackSelector())
                .setLoadControl(if (youtube==true) loadControl else getLoadControl())
                .build()
        handleQualityRec(youtube)
        if (youtube==true){
            handleYoutubeExtractor(speed)
        } else {
            setupNormalMedia(media = MediaItem
                .Builder()
                .setUri(videoUrl ?: details?.intro!!)
                .setMimeType(MimeTypes.BASE_TYPE_VIDEO)
                .build(), speed = speed)
        }


        setupVideoListener()
    }

    private fun handleYoutubeExtractor(speed: Float) {
        val youtubeJExtractor = YoutubeJExtractor()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            youtubeJExtractor.extract(videoUrl, object : JExtractorCallback {
                override fun onSuccess(videoData: VideoPlayerConfig?) {

                    videoQuality =
                        videoData?.streamingData!!.adaptiveFormats.first {
                            it.qualityLabel?.contains(
                                "360"
                            ) == true
                        }.url
                            ?: videoData?.streamingData!!.adaptiveFormats.first {
                                it.mimeType?.contains(
                                    "video/mp4"
                                ) == true
                            }.url!!

                    audioQuality =
                        videoData?.streamingData!!.adaptiveFormats.first { it.mimeType?.contains("audio") == true }.url!!


                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){

                        qualityAdapter.swapData(videoData?.streamingData!!.adaptiveFormats.filter {
                            it.qualityLabel.isNullOrBlank().not()
                        }).also { qualityAdapter.onClickItem = ::onClickItem }

                        val pair = getAudioSource(audioQuality!!, videoQuality!!)
                        simplePlayer?.setMediaSource(MergingMediaSource(pair.first, pair.second), true)
                        setupPlayerSettings(speed)
                    }

                }

                override fun onNetworkException(e: YoutubeRequestException?) {

                }

                override fun onError(exception: Exception?) {

                }

            })

        }
    }
    private fun onClickItem(adaptiveFormatsItem: AdaptiveFormatsItem, i: Int) {
        val speed=simplePlayer?.playbackParameters?.speed ?: 1f
        SharedPlayer.lastPos=simplePlayer?.currentPosition ?: 0
        viewModel.lastPos=simplePlayer?.currentPosition ?: 0
        videoQuality=adaptiveFormatsItem.url
        val pair=getAudioSource(audioQuality!!, videoQuality!!)
        simplePlayer?.setMediaSource(MergingMediaSource(pair.first,pair.second),true)
        setupPlayerSettings(speed).also { binding.rvQualities.hide() }
        binding!!.tvQuality.text=adaptiveFormatsItem.qualityLabel
    }

    private fun getAudioSource(audioQuality:String,videoQuality:String):Pair<ProgressiveMediaSource,ProgressiveMediaSource>{
        val audioSource=ProgressiveMediaSource
            .Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(audioQuality))

        val videoSource=ProgressiveMediaSource
            .Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(videoQuality))
    return Pair(audioSource,videoSource)
    }
    private fun handleQualityRec(youtube: Boolean?) {
        if (youtube==true)
            binding.qualityLay.show()
        else
            binding.qualityLay.hide()

        binding.rvQualities.layoutManager=LinearLayoutManager(requireContext())
        binding.rvQualities.adapter=qualityAdapter
    }

    private fun setupNormalMedia(media: MediaItem, speed: Float) {
        simplePlayer?.addMediaItem(media)
        setupPlayerSettings(speed)
    }

    private fun setupPlayerSettings(speed: Float) {

        simplePlayer?.setPlaybackSpeed(speed)
        simplePlayer?.playWhenReady=true
        if (SharedPlayer.lastPos > 0) simplePlayer?.seekTo(viewModel.lastPos)
        simplePlayer?.playWhenReady = playWhenReady


        simplePlayer?.prepare()
        binding.styledVideo.showController()
        binding.styledVideo.controllerAutoShow=true
        binding.frameLayout.addTransitionListener(this)
        binding.progressVideo.isVisible=true
        binding.styledVideo.player = simplePlayer
    }
    private fun getDrf(): DefaultRenderersFactory {
        return DefaultRenderersFactory(requireContext().applicationContext)
                .experimentalSetSynchronizeCodecInteractionsWithQueueingEnabled(true)
                .setEnableDecoderFallback(true)
                .setExtensionRendererMode(EXTENSION_RENDERER_MODE_PREFER).setMediaCodecSelector { mimeType, requiresSecureDecoder, requiresTunnelingDecoder ->
                    var decoderInfos: List<MediaCodecInfo> = MediaCodecSelector.DEFAULT
                            .getDecoderInfos(mimeType, requiresSecureDecoder, false)
                    if (MimeTypes.VIDEO_H264 == mimeType) {
                        // copy the list because MediaCodecSelector.DEFAULT returns an unmodifiable list
                        decoderInfos = decoderInfos
                        decoderInfos.toMutableList().reverse()
                    }
                    decoderInfos
                }

    }

    private fun getTrackSelector(): TrackSelector {
        return DefaultTrackSelector(requireContext(),AdaptiveTrackSelection.Factory())
                .apply {
                    setParameters(buildUponParameters()
                    )

                }
    }

    private fun getLoadControl(): LoadControl {
        return DefaultLoadControl.Builder()
                .setAllocator(DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE))
                .setTargetBufferBytes(C.DEFAULT_BUFFER_SEGMENT_SIZE)
                .setBufferDurationsMs(10000, 100000, 1000, 1000)
                .setPrioritizeTimeOverSizeThresholds(true)
                .build()
    }


    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

    }

    override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
    ) {

    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        if (isPlay && viewModel.isUserLogin()) playingWaterMark()
    }

    fun calculatePlayingTime(){
        val totalDuration = simplePlayer?.duration?.toFloat()?.div(SECOND_DURATION_INTERVAL)
        currentTime = System.currentTimeMillis().div(SECOND_DURATION_INTERVAL)

        val difference = currentTime!! - initialTime

        val diff= (difference.div(totalDuration ?: 0f)).times(100)

        Log.e(TAG, "calculatePlayingTime: $diff %")
        //check if video pass 45% watching
        if (lessonId!=0 && isWatched.not() && diff >= WATCHED_DURATION) {
                //change flag
                isWatched=true
                //send lesson to server to mark video as watched
                lessonId?.let { viewModel.sendRequestMarkVideo(it) }
        }

        if (simplePlayer?.isPlaying==true){
            Handler(Looper.getMainLooper()).postDelayed(
                    this::calculatePlayingTime,
                    SECOND_DURATION_INTERVAL
            )
        }
    }
    override fun onPause() {
        super.onPause()
        //simplePlayer?.pause()
        //releasePlayer()
       // isPlay=false
    }




    private fun playingWaterMark() {
            binding.frameLayout.transitionToStart()
            binding.frameLayout.transitionToEnd()
    }

    override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
    ) {

    }

    override fun onClickLessons() {
        findNavController().navigate(
                R.id.action_global_courseLessonsFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        )
        )
    }

    override fun onClickQuiz() {
        findNavController().navigate(
                R.id.action_global_quizFragment, bundleOf(
                "course"
                        to
                details.toJson()
        )
        )
    }

    override fun onClickDisc() {
        findNavController().navigate(
                R.id.action_global_discussionsFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        )
        )
    }

    override fun onClickRates() {
        findNavController().navigate(
                R.id.action_global_rateCourseFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        )
        )
    }

    override fun onClickFiles() {
        findNavController().navigate(
                R.id.action_global_courseFilesFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        )
        )
    }

    override fun onClickAds() {
        findNavController().navigate(
                R.id.action_global_announcementsFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        )
        )

    }

    companion object{
        const val WATCHED_DURATION= 45.0
        const val SECOND_DURATION_INTERVAL=1000L
    }

    override fun onDisplayAdded(p0: Int) {
        startMute()
    }

    override fun onDisplayRemoved(p0: Int) {
        enableSound()
    }

    override fun onDisplayChanged(p0: Int) {

    }

    override fun onDestroyView() {
        releasePlayer(true)
        resetTime()
        enableSound()
        super.onDestroyView()

    }




    private fun releasePlayer(fromDestroy:Boolean=false) {
        simplePlayer?.let {
            viewModel.lastPos =/*if (fromDestroy) 0 else*/ it.currentPosition
            SharedPlayer.lastPos = /*if (fromDestroy) 0 else*/ it.currentPosition
            playWhenReady = it.playWhenReady
            it.release()
            simplePlayer = null
            //Log.e("pos11",SharedPlayer.lastPos.toString()+","+viewModel.lastPos)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==500 && resultCode==RESULT_OK){
            viewModel.lastPos=data?.getLongExtra("lastPos",0)!!
            SharedPlayer.lastPos=data?.getLongExtra("lastPos",0)!!
            youtube=data.getBooleanExtra("youtube", false)
            if (youtube==true && videoQuality!=null && audioQuality!=null)
                 handleResumeYoutube(data.getFloatExtra("speed",0f)).also { Log.e("onActivity",youtube.toString()+","+videoQuality) }
            else
                initPlayer(data.getFloatExtra("speed",1f)).also { Log.e("onActivity",youtube.toString()+","+videoQuality) }

        }
    }

    private fun handleResumeYoutube(speed: Float) {
        simplePlayer = ExoPlayer.Builder(requireContext(),getDrf())
            .setTrackSelector(getTrackSelector())
            .setLoadControl(getLoadControl())
            .build()

        val pair=getAudioSource(audioQuality!!, videoQuality!!)
        simplePlayer?.setMediaSource(MergingMediaSource(pair.first,pair.second),true)
        setupPlayerSettings(speed).also { binding.rvQualities.hide() }
        setupVideoListener()
    }


}