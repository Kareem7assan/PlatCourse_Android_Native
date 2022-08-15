package com.platCourse.platCourseAndroid.home.course_details

import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityFullScreenBinding
import com.rowaad.app.base.BaseActivity

class FullScreenActivity : BaseActivity(R.layout.activity_full_screen), Player.Listener, MotionLayout.TransitionListener {

    private var simplePlayer: ExoPlayer? = null
    private var binding: ActivityFullScreenBinding? = null
    private var isPlay: Boolean = false

    override fun init() {
        binding=ActivityFullScreenBinding.bind(findViewById(R.id.rootFullScreen))
        val url=intent.getStringExtra("url")!!
        val pos=intent.getLongExtra("pos",0L)
        val isPlay=intent.getBooleanExtra("isPlay",false)
        setupVideo(url,pos,isPlay)

    }

    private fun setupVideo(url: String, pos: Long, isPlay: Boolean) {
        simplePlayer = ExoPlayer.Builder(this).build()
        binding?.styledVideo?.player= simplePlayer
        binding?.frameLayout?.addTransitionListener(this)

        simplePlayer!!.addMediaItem(MediaItem.fromUri(url))
        simplePlayer!!.prepare()
        simplePlayer!!.addListener(this)

        if (isPlay)
            setCurrentPosition(simplePlayer!!,pos).also { simplePlayer!!.play() }

        binding?.styledVideo?.setFullscreenButtonClickListener {
            onBackPressed()
        }

    }

    private fun setCurrentPosition(simplePlayer: ExoPlayer, pos: Long) {
        simplePlayer.seekTo(pos)
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
        binding?.frameLayout?.transitionToStart()
        binding?.frameLayout?.transitionToEnd()
    }

    override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {

    }
}