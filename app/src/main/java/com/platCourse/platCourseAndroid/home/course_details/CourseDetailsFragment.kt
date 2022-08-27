package com.platCourse.platCourseAndroid.home.course_details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentDetailsCourseBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.cache.fromJson
import com.rowaad.app.data.cache.toJson
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.attribute_course_model.Action
import com.rowaad.app.data.model.attribute_course_model.CourseListener
import com.rowaad.app.data.model.attribute_course_model.ItemCourseDetails
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show


class CourseDetailsFragment : BaseFragment(R.layout.fragment_details_course), MotionLayout.TransitionListener, CourseListener {

    private var videoUrl: String? = null
    private var isPlay: Boolean = false
    private var simplePlayer: ExoPlayer? = null
    private var details: CourseItem? = null
    private val binding by viewBinding<FragmentDetailsCourseBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()

    private val adapter by lazy {
        AttributeAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        details=arguments?.getString("details")?.fromJson<CourseItem>()
        videoUrl=arguments?.getString("url")
        Log.e("details",details.toString())
        binding.frameLayout.addTransitionListener(this)
        handleWaterMark()
        setupVideo()
        setupRec()
        setupAdapter()
        sendRequestUser()
        handleCourses()
    }

    private fun sendRequestUser() {
        viewModel.sendRequestUser()
    }

    private fun handleCourses() {
        handleSharedFlow(viewModel.userFlow,onSuccess = { it as UserModel

        })
    }

    private fun handleWaterMark() {
        if (viewModel.isUserLogin()){
            binding.tvOwner.text = viewModel.getUser()?.name
            binding.tvPhone.text = viewModel.getUser()?.phone_number
            binding.markLay.show()
        }
        else {
            binding.markLay.hide()
        }
    }

    private fun setupAdapter() {
        val items=mutableListOf<ItemCourseDetails>()
        items.add(ItemCourseDetails(title = getString(R.string.desc),desc = details?.overview))
        items.add(ItemCourseDetails(title = getString(R.string.quizes), showForward = true, type = Action.QUIZES))
        items.add(ItemCourseDetails(title = getString(R.string.rates),showForward = true,type = Action.RATES))
        if (viewModel.isUserLogin() && viewModel.isCourseOwner(details?.id ?: 0)) { items.add(ItemCourseDetails(title = getString(R.string.lessons), showForward = true, type = Action.LESSONS)) }
            //items.add(ItemCourseDetails(title = getString(R.string.rates),showForward = true,type = Action.RATES))
        if (viewModel.isUserLogin() && viewModel.isCourseOwner(details?.id ?: 0)) {
            items.add(ItemCourseDetails(title = getString(R.string.lessons), showForward = true, type = Action.LESSONS))
            items.add(ItemCourseDetails(title = getString(R.string.discussions), showForward = true, type = Action.DISCUSSIONS))
            //items.add(ItemCourseDetails(title = getString(R.string.quizes), showForward = true, type = Action.QUIZES))
            items.add(ItemCourseDetails(title = getString(R.string.files), showForward = true, type = Action.FILES))
            items.add(ItemCourseDetails(title = getString(R.string.anouneces), showForward = true, type = Action.ADS))
        }
        adapter.swapData(items)
    }

    private fun setupRec() {
        binding.rvAttributes.layoutManager=LinearLayoutManager(requireContext())
        binding.rvAttributes.adapter=adapter
    }


    private fun setupVideo() {
         simplePlayer = ExoPlayer.Builder(requireContext()).build()
         binding.styledVideo.player= simplePlayer
         simplePlayer!!.addMediaItem(MediaItem.fromUri(videoUrl ?: details?.intro!!))
         simplePlayer!!.prepare()
        simplePlayer?.addListener(object : Player.Listener{
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                this@CourseDetailsFragment.isPlay=isPlaying
                if (isPlaying && viewModel.isUserLogin()){
                    playingWaterMark()
                }
            }

        })

        binding.styledVideo.setFullscreenButtonClickListener {
            startActivity(Intent(requireContext(), FullScreenActivity::class.java).also {
                it.putExtra("pos", simplePlayer!!.currentPosition)
                it.putExtra("isPlay", simplePlayer!!.isPlaying)
                it.putExtra("name", details?.ownerName)
                it.putExtra("url", videoUrl ?: details?.intro!!)

            })
        }

    }

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

    }

    override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {

    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

             if (isPlay && viewModel.isUserLogin()) playingWaterMark()



    }

    override fun onPause() {
        super.onPause()
        simplePlayer?.pause()
        isPlay=false
    }




    private fun playingWaterMark() {
            binding.frameLayout.transitionToStart()
            binding.frameLayout.transitionToEnd()
    }

    override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {

    }

    override fun onClickLessons() {
        findNavController().navigate(R.id.action_global_courseLessonsFragment, bundleOf(
            "course"
                to
            details.toJson()
        ))
    }

    override fun onClickQuiz() {
        findNavController().navigate(R.id.action_global_quizFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        ))
    }

    override fun onClickDisc() {

    }

    override fun onClickRates() {
        findNavController().navigate(R.id.action_global_rateCourseFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        ))
    }

    override fun onClickFiles() {

    }

    override fun onClickAds() {

    }
}