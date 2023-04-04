package com.platCourse.platCourseAndroid.home.youtube


import android.content.res.Configuration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityYoutubeBinding
import com.rowaad.app.base.BaseActivity
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show


class YoutubeActivity : BaseActivity(R.layout.activity_youtube) {

    private var isSpeed: Boolean = false
    private lateinit var controller: PlayerUiController
    private lateinit var player: YouTubePlayerView
    private lateinit var tracker: YouTubePlayerTracker
    private var videoId: String?=null
    private var binding: ActivityYoutubeBinding? = null

    override fun init() {
        binding= ActivityYoutubeBinding.bind(findViewById(R.id.rootPlayer))
        videoId=intent.getStringExtra("video_id") ?: ""
        val title=intent.getStringExtra("video_title") ?: ""
        binding!!.progress.show()
        tracker = YouTubePlayerTracker()
        player=binding!!.youtubePlayerView
        //controller=binding!!.youtubePlayerView.getPlayerUiController()
        /*player.enableAutomaticInitialization=false
        player.addYouTubePlayerListener(tracker)
        player.initialize(tracker, IFramePlayerOptions.Builder()
            .controls(1)
            .rel(0)
            .build())
        */
        //DefaultPlayerUiController().rootView.
        binding!!.progressLay.show()
        binding!!.youtubePlayerView.hide()
        lifecycle.addObserver(player)
      //  player.getPlayerUiController().setVideoTitle(title)
       // controller.showUi(true)
        binding!!.myView.setOnClickListener {  }

        player.addYouTubePlayerListener(object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {

            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {

            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {

            }

            override fun onPlaybackQualityChange(
                youTubePlayer: YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {

            }

            override fun onPlaybackRateChange(
                youTubePlayer: YouTubePlayer,
                playbackRate: PlayerConstants.PlaybackRate
            ) {

            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                binding!!.youtubePlayerView.show()
                binding!!.progressLay.hide()
                youTubePlayer.loadVideo(videoId!!,0f)
                //player.enableBackgroundPlayback(true)


            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                when(state){
                    PlayerConstants.PlayerState.ENDED ->{
                        binding!!.progress.show()
                    }
                    PlayerConstants.PlayerState.PLAYING ->{
                        binding!!.progress.hide()
                        binding!!.youtubePlayerView.show()
                    }
                    else->{
                    }

                }
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {

            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
            }

            override fun onVideoLoadedFraction(
                youTubePlayer: YouTubePlayer,
                loadedFraction: Float
            ) {

            }
        })

    }



    override fun onConfigurationChanged(@NonNull newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding!!.youtubePlayerView.enterFullScreen()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding!!.youtubePlayerView.exitFullScreen()
            binding!!.rootPlayer.layoutParams= FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            binding!!.youtubePlayerView.layoutParams=binding!!.rootPlayer.layoutParams
           // ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT)
            //binding!!.youtubePlayerView.layoutParams=binding!!.rootPlayer.layoutParams

        }
    }
}