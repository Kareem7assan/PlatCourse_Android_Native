package com.platCourse.platCourseAndroid.home.course_details

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentDetailsCourseBinding
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.cache.fromJson
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import org.jetbrains.anko.support.v4.toast
import kotlin.random.Random


class CourseDetailsFragment : BaseFragment(R.layout.fragment_details_course), MotionLayout.TransitionListener, Player.Listener {

    private var isPlay: Boolean = false
    private var simplePlayer: ExoPlayer? = null
    private var details: CourseItem? = null
    private val binding by viewBinding<FragmentDetailsCourseBinding>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        details=arguments?.getString("details")?.fromJson<CourseItem>()
        binding.frameLayout.addTransitionListener(this)
        setupVideo()

    }





    private fun setupVideo() {
         simplePlayer = ExoPlayer.Builder(requireContext()).build()
         binding.styledVideo.player= simplePlayer
         simplePlayer!!.addMediaItem(MediaItem.fromUri(details?.intro!!))
         simplePlayer!!.prepare()
         simplePlayer?.addListener(this)


        binding.styledVideo.setFullscreenButtonClickListener {
            startActivity(Intent(requireContext(), FullScreenActivity::class.java).also {
                it.putExtra("pos", simplePlayer!!.currentPosition)
                it.putExtra("isPlay", simplePlayer!!.isPlaying)
                it.putExtra("url", details?.intro!!)

            })
        }

    }

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

    }

    override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {

    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

             if (isPlay) playingWaterMark()



    }

    override fun onPause() {
        super.onPause()
        simplePlayer?.pause()
        isPlay=false
    }



    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        this.isPlay=isPlaying
        if (isPlaying){
            playingWaterMark()
        }

    }

    private fun playingWaterMark() {
        binding.frameLayout.transitionToStart()
        binding.frameLayout.transitionToEnd()
    }

    override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {

    }
}