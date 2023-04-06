package com.platCourse.platCourseAndroid.home.youtube


import android.content.res.Configuration
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.viewModels
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
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseActivity
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show


class YoutubeActivity : BaseActivity(R.layout.activity_youtube) {

    private var isSpeed: Boolean = false
    private lateinit var controller: PlayerUiController
    private lateinit var player: YouTubePlayerView
    private lateinit var tracker: YouTubePlayerTracker
    private var videoId: String? = null
    private var binding: ActivityYoutubeBinding? = null

    private val viewModel: CoursesViewModel by viewModels()


    private var currentDuration = 0f
    private var initialTime = 0L
    private var updateTime: Long? = null
    private var currentTime: Long? = null
    private var lessonId: Int? = null
    private var isWatched = false
    private var isPlayedForFirstTime = false
    private var isPlaying: Boolean = false
    override fun init() {
        binding = ActivityYoutubeBinding.bind(findViewById(R.id.rootPlayer))
        videoId = intent.getStringExtra("video_id") ?: ""
        val title = intent.getStringExtra("video_title") ?: ""
        lessonId = intent?.getIntExtra("lesson_id", 0)
        binding!!.progress.show()
        tracker = YouTubePlayerTracker()
        player = binding!!.youtubePlayerView
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
        binding!!.myView.setOnClickListener { }

        player.addYouTubePlayerListener(object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {

            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                if (isPlaying && viewModel.isUserLogin()) {
                    if (!isPlayedForFirstTime) {
                        //change flag
                        isPlayedForFirstTime = true
                        initialTime =
                            System.currentTimeMillis().div(SECOND_DURATION_INTERVAL)
                    } else {
                        if (updateTime != null) {
                            initialTime =
                                System.currentTimeMillis().div(SECOND_DURATION_INTERVAL)
                                    .minus(updateTime ?: 0L)
                            updateTime = null
                        }
                    }

                    // make this check every 15 sec
                    if (second.toInt().rem(15) == 0){
                        calculatePlayingTime()
                    }
                }
                if (!isPlaying) {
                    updateTime = currentTime?.minus((initialTime))
                }
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
                youTubePlayer.loadVideo(videoId!!, 0f)
                //player.enableBackgroundPlayback(true)


            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                when (state) {
                    PlayerConstants.PlayerState.ENDED -> {
                        binding!!.progress.show()
                    }
                    PlayerConstants.PlayerState.PLAYING -> {
                        binding!!.progress.hide()
                        binding!!.youtubePlayerView.show()
                        isPlaying=true
                        //

                    }
                    else -> {
                        isPlaying=false
                    }

                }
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
                currentDuration = duration* SECOND_DURATION_INTERVAL
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

    fun calculatePlayingTime() {
        val totalDuration = currentDuration.div(SECOND_DURATION_INTERVAL)
        currentTime = System.currentTimeMillis().div(SECOND_DURATION_INTERVAL)

        val difference = currentTime!! - initialTime

        val diff = (difference.div(totalDuration )).times(100)

        Log.e("Youtube Player", "calculatePlayingTime: $diff %", )
        //check if video pass 60% watching
        if (lessonId != 0 && isWatched.not() && diff >= WATCHED_DURATION) {
            //change flag
            isWatched = true
            //send lesson to server to mark video as watched
            lessonId?.let { viewModel.sendRequestMarkVideo(it) }
        }
    }

    override fun onConfigurationChanged(@NonNull newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding!!.youtubePlayerView.enterFullScreen()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding!!.youtubePlayerView.exitFullScreen()
            binding!!.rootPlayer.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            binding!!.youtubePlayerView.layoutParams = binding!!.rootPlayer.layoutParams
            // ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT)
            //binding!!.youtubePlayerView.layoutParams=binding!!.rootPlayer.layoutParams

        }
    }

    companion object {
        const val WATCHED_DURATION = 60.0
        const val SECOND_DURATION_INTERVAL = 1000L
    }
}

