package com.platCourse.platCourseAndroid.home.youtube

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.platCourse.platCourseAndroid.R
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import com.rowaad.utils.extention.toast


class YoutubeActivity : AppCompatActivity() {
    private lateinit var youTubePlayerView: YouTubePlayerView
    private var progress: ProgressBar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)
        youTubePlayerView = findViewById(R.id.youtube_player_view)
         progress = findViewById(R.id.progress)
        lifecycle.addObserver(youTubePlayerView)
        val tracker = YouTubePlayerTracker()
        val iFramePlayerOptions =  IFramePlayerOptions.Builder()
        iFramePlayerOptions.controls(1)
        iFramePlayerOptions.rel(0)
        val build = iFramePlayerOptions.build()
        youTubePlayerView.addYouTubePlayerListener(tracker)
        //IFramePlayerOptions.Builder().rel()
        //youTubePlayerView.addYouTubePlayerListener(listener)

        //youTubePlayerView.initializeWithWebUi(listener,true)
        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                //toast("ready")
          //      toast(tracker.currentSecond.toString()+)

                tracker.onPlaybackQualityChange(youTubePlayer,PlayerConstants.PlaybackQuality.SMALL)
            }
        })
        youTubePlayerView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
            }

            override fun onYouTubePlayerExitFullScreen() {

            }
        })
        //controller.showYouTubeButton(false)

    }

    override fun onConfigurationChanged(@NonNull newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.enterFullScreen()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            youTubePlayerView.exitFullScreen()
        }
    }
}