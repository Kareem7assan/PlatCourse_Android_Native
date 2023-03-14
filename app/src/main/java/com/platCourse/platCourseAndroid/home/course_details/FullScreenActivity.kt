package com.platCourse.platCourseAndroid.home.course_details

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.display.DisplayManager
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kotvertolet.youtubejextractor.JExtractorCallback
import com.github.kotvertolet.youtubejextractor.YoutubeJExtractor
import com.github.kotvertolet.youtubejextractor.exception.YoutubeRequestException
import com.github.kotvertolet.youtubejextractor.models.newModels.AdaptiveFormatsItem
import com.github.kotvertolet.youtubejextractor.models.newModels.VideoPlayerConfig
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityFullScreenBinding
import com.platCourse.platCourseAndroid.home.course_details.adapter.QualityAdapter
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseActivity
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.invisible
import com.rowaad.utils.extention.show
import com.rowaad.utils.extention.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FullScreenActivity : BaseActivity(R.layout.activity_full_screen), Player.Listener, MotionLayout.TransitionListener, DisplayManager.DisplayListener {

    private var youtube: Boolean = false
    private var speed: Float = 1f
    private val qualityAdapter by lazy { QualityAdapter() }
    private var isShowQuality: Boolean = false

    private var lastPos: Long = 0
    private var playWhenReady: Boolean = true
    private var simplePlayer: ExoPlayer? = null
    private var binding: ActivityFullScreenBinding? = null
    private var isPlay: Boolean = false
    private val viewModel: CoursesViewModel by viewModels()
    private var videoQuality: String?=null
    private var audioQuality: String?=null

    private var initialTime = 0L
    private var updateTime:Long? = null
    private var currentTime:Long? = null
    private var lessonId:Int? = null
    private var isWatched=false
    private var isPlayedForFirstTime=false


    override fun init() {
        binding=ActivityFullScreenBinding.bind(findViewById(R.id.rootFullScreen))
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //hideSystemBars()
        handleMuting()
        //window.decorView.setOnSystemUiVisibilityChangeListener(onSystemUiVisibilityChangeListener);
        binding!!.rootFullScreen.setOnApplyWindowInsetsListener(onApplyWindowInsetsListener)
        setupActions()

    }

    private fun setupActions() {
        binding!!.qualityLay.setOnClickListener {
            isShowQuality=isShowQuality.not()
            if (isShowQuality){
                binding!!.rvQualities.show().also { binding!!.tvQuality.setCompoundDrawablesWithIntrinsicBounds(null,null,
                    ContextCompat.getDrawable(this,R.drawable.ic_arrow_up_24),null) }
            }
            else{
                binding!!.rvQualities.hide().also { binding!!.tvQuality.setCompoundDrawablesWithIntrinsicBounds(null,null,
                    ContextCompat.getDrawable(this,R.drawable.ic_arrow_down_24),null) }
            }
            playFullScreen()
        }
    }


    private fun hideSystemBars() {
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun hideSystemUI() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
            // Configure the behavior of the hidden system bars
            windowInsetsController.isAppearanceLightNavigationBars = false

            windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // Hide both the status bar and the navigation bar
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

            ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                // Apply the insets as a margin to the view. Here the system is setting
                // only the bottom, left, and right dimensions, but apply whichever insets are
                // appropriate to your layout. You can also update the view padding
                // if that's more appropriate.
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    leftMargin = insets.left
                    bottomMargin = insets.bottom
                    rightMargin = -150
                }


                // Return CONSUMED if you don't want want the window insets to keep being
                // passed down to descendant views.
                WindowInsetsCompat.CONSUMED
            }

        }
        else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
        }


    }


    /*override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

    }*/

    private val onApplyWindowInsetsListener = View.OnApplyWindowInsetsListener { view, windowInsets ->
        binding?.rootFullScreen?.setPadding(
                0,
                windowInsets.systemWindowInsetTop,
                0,
                windowInsets.systemWindowInsetBottom)


        return@OnApplyWindowInsetsListener windowInsets.consumeSystemWindowInsets()

    }


    private fun handleMuting() {
        val displayManager = getSystemService(AppCompatActivity.DISPLAY_SERVICE) as DisplayManager
        displayManager.registerDisplayListener(this, null)

    }



    private fun handleWaterMark(name: String?) {
        if (viewModel.isUserLogin()){
            binding?.tvOwner?.text = viewModel.getUser()?.username
            binding?.tvPhone?.text = viewModel.getUser()?.phone_number
            binding?.markLay?.show()
        }
        else {
            binding?.markLay?.hide()
        }
    }

    private fun setupVideo(url: String, pos: Long, isPlay: Boolean) {
        simplePlayer = ExoPlayer.Builder(this, getDrf())
                .setTrackSelector(getTrackSelector())
                .setLoadControl(getLoadControl())
                .build()
        handleQualityRec(youtube)

        if (youtube){
            handleYoutubeExtractor(speed,url,pos)
        } else {
            setupNormalMedia(media = MediaItem
                .Builder()
                .setUri(url)
                .setMimeType(MimeTypes.BASE_TYPE_VIDEO)
                .build(), speed = speed)
        }




        playFullScreen()
        binding?.styledVideo?.player= simplePlayer


        binding?.styledVideo?.setFullscreenButtonClickListener {
            intent.putExtra("lastPos", simplePlayer?.currentPosition)
            intent.putExtra("isPlay", simplePlayer?.isPlaying)
            intent.putExtra("speed", simplePlayer?.playbackParameters?.speed)
            intent.putExtra("youtube", youtube)

            setResult(RESULT_OK, intent)
            releasePlayer()
            finish()

        }
        setupVideoListener()
    }
    private fun setupNormalMedia(media: MediaItem, speed: Float) {
        simplePlayer?.addMediaItem(media)
        setupPlayerSettings(speed)
    }
    private fun handleYoutubeExtractor(speed: Float,url: String, pos: Long) {
        val youtubeJExtractor = YoutubeJExtractor()
        GlobalScope.launch {
            youtubeJExtractor.extract(url, object : JExtractorCallback {
                override fun onSuccess(videoData: VideoPlayerConfig?) {
                    videoQuality =
                        videoData?.streamingData!!.adaptiveFormats.first { it.qualityLabel?.contains("360") == true }.url
                            ?:
                                    videoData?.streamingData!!.adaptiveFormats.first { it.mimeType?.contains("video/mp4") == true }.url!!

                    audioQuality=videoData?.streamingData!!.adaptiveFormats.first { it.mimeType?.contains("audio")==true }.url!!


                    launch(Dispatchers.Main){
                        qualityAdapter.swapData(videoData?.streamingData!!.adaptiveFormats.filter { it.qualityLabel.isNullOrBlank().not() }).also { qualityAdapter.onClickItem=::onClickItem }
                        val pair=getAudioSource(audioQuality!!, videoQuality!!)
                        simplePlayer?.setMediaSource(MergingMediaSource(pair.first,pair.second),true)
                        setupPlayerSettings(speed).also {
                            playFullScreen()

                        }
                    }
                }

                override fun onNetworkException(e: YoutubeRequestException?) {

                }

                override fun onError(exception: Exception?) {

                }

            })
        }
    }

    private fun playFullScreen() {
        val params: ConstraintLayout.LayoutParams =
            binding?.styledVideo?.layoutParams as ConstraintLayout.LayoutParams
        params.width = MATCH_PARENT
        params.height = MATCH_PARENT
        binding?.styledVideo?.layoutParams = params
        binding?.styledVideo?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
    }

    private fun onClickItem(adaptiveFormatsItem: AdaptiveFormatsItem, i: Int) {
        val speed=simplePlayer?.playbackParameters?.speed ?: 1f
        lastPos=simplePlayer?.currentPosition ?: 0
        videoQuality=adaptiveFormatsItem.url
        val pair=getAudioSource(audioQuality!!, videoQuality!!)
        simplePlayer?.setMediaSource(MergingMediaSource(pair.first,pair.second),true)
        setupPlayerSettings(speed).also { binding!!.rvQualities.hide() }
        binding!!.tvQuality.text=adaptiveFormatsItem.qualityLabel
    }
    private fun setupPlayerSettings(speed: Float) {
        simplePlayer?.setPlaybackSpeed(speed)
        simplePlayer?.playWhenReady=true
        if (lastPos > 0) simplePlayer?.seekTo(lastPos)
        simplePlayer?.playWhenReady = playWhenReady
        simplePlayer?.prepare()
        binding?.styledVideo?.showController()
        binding?.styledVideo?.controllerAutoShow=true
        binding?.frameLayout?.addTransitionListener(this)
        binding?.progressVideo?.isVisible=true
        binding?.styledVideo?.player = simplePlayer
        simplePlayer?.addListener(this)
    }

    private fun getAudioSource(audioQuality:String,videoQuality:String):Pair<ProgressiveMediaSource, ProgressiveMediaSource>{
        val audioSource= ProgressiveMediaSource
            .Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(audioQuality))

        val videoSource= ProgressiveMediaSource
            .Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(videoQuality))
        return Pair(audioSource,videoSource)
    }
    private fun handleQualityRec(youtube: Boolean?) {
        if (youtube==true)
            binding!!.qualityLay.show()
        else
            binding!!.qualityLay.hide()

        binding!!.rvQualities.layoutManager= LinearLayoutManager(this)
        binding!!.rvQualities.adapter=qualityAdapter
    }
    private fun setupVideoListener(){
        simplePlayer?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                this@FullScreenActivity.isPlay = isPlaying

                if (isPlaying && viewModel.isUserLogin()) {
                    if (!isPlayedForFirstTime) {
                        //change flag
                        isPlayedForFirstTime = true
                        initialTime = System.currentTimeMillis()
                            .div(CourseDetailsFragment.SECOND_DURATION_INTERVAL)
                    }
                    else {
                        if (updateTime != null) {
                            initialTime = System.currentTimeMillis()
                                .div(CourseDetailsFragment.SECOND_DURATION_INTERVAL).minus(
                                    updateTime ?: 0L
                                )
                            updateTime = null
                        }
                    }
                    binding?.styledVideo?.postDelayed(
                        this@FullScreenActivity::calculatePlayingTime,
                        CourseDetailsFragment.SECOND_DURATION_INTERVAL
                    )
                }
                if (!isPlaying) {
                    updateTime = currentTime?.minus((initialTime))
                }
            }

        })

    }
    private fun getDrf(): DefaultRenderersFactory {
        return DefaultRenderersFactory(this.applicationContext)
                .experimentalSetSynchronizeCodecInteractionsWithQueueingEnabled(true)
                .setEnableDecoderFallback(true)
                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER).setMediaCodecSelector { mimeType, requiresSecureDecoder, requiresTunnelingDecoder ->
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
        return DefaultTrackSelector(this, AdaptiveTrackSelection.Factory())
                .apply {
                    setParameters(
                            buildUponParameters()
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
    fun calculatePlayingTime(){
        val totalDuration = simplePlayer?.duration?.toFloat()?.div(CourseDetailsFragment.SECOND_DURATION_INTERVAL)
        currentTime = System.currentTimeMillis().div(CourseDetailsFragment.SECOND_DURATION_INTERVAL)

        val difference = currentTime!! - initialTime

        val diff= (difference.div(totalDuration ?: 0f)).times(100)


        //check if video pass 70% watching
        if (lessonId!=0 && isWatched.not()&&diff>= CourseDetailsFragment.WATCHED_DURATION) {
            //change flag
            isWatched=true
            //send lesson to server to mark video as watched
            lessonId?.let { viewModel.sendRequestMarkVideo(it) }
        }

        if (simplePlayer?.isPlaying==true){
            Handler(Looper.getMainLooper()).postDelayed(
                    this::calculatePlayingTime,
                    CourseDetailsFragment.SECOND_DURATION_INTERVAL
            )
        }
    }

    private fun setCurrentPosition(simplePlayer: ExoPlayer, pos: Long) {
        simplePlayer.seekTo(pos)
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

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            //toast("start")
           // initPlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
       // hideSystemUi()
        hideSystemUI()
       /* window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        ViewCompat.setOnApplyWindowInsetsListener(
            window.decorView
        ) { v, insets ->
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0, v.paddingBottom + systemBarInsets.bottom)
            insets
        }*/
        if (simplePlayer == null) {
            initPlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        enableSound()
        CourseDetailsFragment.SharedPlayer.lastPos=simplePlayer?.currentPosition ?: 0

        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        simplePlayer?.let { exoPlayer ->
            CourseDetailsFragment.SharedPlayer.lastPos = exoPlayer.currentPosition
            this.lastPos = exoPlayer.currentPosition
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        simplePlayer = null
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding?.styledVideo!!).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun initPlayer(){
        youtube=intent?.getBooleanExtra("youtube",false) ?: false
        val url=intent.getStringExtra("url")!!
        lastPos=if (lastPos==0L) intent.getLongExtra("pos", 0L) else lastPos
        val name=intent.getStringExtra("name")
        lessonId=intent?.getIntExtra("lesson_id", 0)
        handleWaterMark(name)
        val isPlay=intent.getBooleanExtra("isPlay", false)
        speed=intent.getFloatExtra("speed", 1f)
        setupVideo(url, lastPos, isPlay)
        binding?.progressVideo?.isVisible=true
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        //super.onPlaybackStateChanged(playbackState)
        handlePlayBackState(playbackState)
    }

    private fun handlePlayBackState(playbackState: Int) {
        Log.e("playBack", playbackState.toString())

        when(playbackState){
            ExoPlayer.STATE_IDLE -> {
                binding!!.progressVideo.isVisible = true
            }
            ExoPlayer.STATE_BUFFERING -> {
                binding!!.progressVideo.isVisible = true
            }
            ExoPlayer.STATE_READY -> {
                binding!!.progressVideo.isVisible = false
            }

            ExoPlayer.STATE_ENDED -> {

            }
        }
    }
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        this.isPlay=isPlaying
        if (isPlaying && viewModel.isUserLogin()){
            playingWaterMark()

        }
    }

    private fun playingWaterMark() {
        binding?.frameLayout?.transitionToStart()
        binding?.frameLayout?.transitionToEnd()
    }

    override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
    ) {

    }


    private var audioManager: AudioManager? = null

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
            audioManager=getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
        audioManager?.isMicrophoneMute=false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            audioManager?.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_PLAY_SOUND)
        }


    }

    override fun onDisplayAdded(p0: Int) {
        startMute()
    }

    override fun onDisplayRemoved(p0: Int) {
        enableSound()
    }

    override fun onDisplayChanged(p0: Int) {

    }
/*
    override fun onStop() {
        super.onStop()
        enableSound()
        CourseDetailsFragment.SharedPlayer.lastPos=simplePlayer?.currentPosition ?: 0

    }*/



    override fun onBackPressed() {
        //super.onBackPressed()

        intent.putExtra("lastPos", simplePlayer?.currentPosition)
        intent.putExtra("isPlay", simplePlayer?.isPlaying)
        intent.putExtra("speed", simplePlayer?.playbackParameters?.speed)
        intent.putExtra("youtube", youtube)
        setResult(RESULT_OK, intent)
        finish()
        releasePlayer()
    }
}

