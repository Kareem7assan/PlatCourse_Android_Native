package com.platCourse.platCourseAndroid.home.youtube

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityYoutubeBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseActivity
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show


class YoutubeActivity : BaseActivity(R.layout.activity_youtube) , MotionLayout.TransitionListener,
    FullscreenListener {

    private var view: View? = null
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
        hideSystemUI()
        videoId = intent.getStringExtra("video_id") ?: ""
        val title = intent.getStringExtra("video_title") ?: ""
        lessonId = intent?.getIntExtra("lesson_id", 0)
        tracker = YouTubePlayerTracker()
        player = binding!!.youtubePlayerView
        handleWaterMark()
        binding!!.progressLay.show()
        binding!!.youtubePlayerView.hide()
        lifecycle.addObserver(player)
        binding!!.myView.setOnClickListener { }
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder()
            .fullscreen(1)
            .enablejsapi()
            .disableKeyBoard()
            .rel(0)
            .controls(1).build()

        player.addFullscreenListener(this)


        val listener= object : AbstractYouTubePlayerListener() {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {
                //youTubePlayer.setPlaybackRate()
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
                    if (second.toInt().rem(15) == 0) {
                        calculatePlayingTime()
                    }
                }
                if (!isPlaying) {
                    updateTime = currentTime?.minus((initialTime))
                }
            }

            override fun onError(
                youTubePlayer: YouTubePlayer,
                error: PlayerConstants.PlayerError
            ) {

            }

            override fun onPlaybackQualityChange(
                youTubePlayer: YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {
                //toast(playbackQuality.name)
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
                binding!!.frameLayout.addTransitionListener(this@YoutubeActivity)
                playingWaterMark()

            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                when (state) {
                    PlayerConstants.PlayerState.ENDED -> {
                    }

                    PlayerConstants.PlayerState.PLAYING -> {
                        binding!!.youtubePlayerView.show()
                        isPlaying = true
                        //

                    }
                    else -> {
                        isPlaying = false
                    }

                }
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
                currentDuration = duration * SECOND_DURATION_INTERVAL
            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
            }

            override fun onVideoQuality(youTubePlayer: YouTubePlayer, quality: String) {
                youTubePlayer.setPlaybackQuality(quality)
            }


            override fun onVideoLoadedFraction(
                youTubePlayer: YouTubePlayer,
                loadedFraction: Float
            ) {

            }
        }

        player.initialize(listener,options)
    }

    private fun handleScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding!!.rootPlayer.setOnApplyWindowInsetsListener(onApplyWindowInsetsListener)

    }

    private val onApplyWindowInsetsListener = View.OnApplyWindowInsetsListener { view, windowInsets ->
        binding?.rootPlayer?.setPadding(
            0,
            windowInsets.systemWindowInsetTop,
            0,
            windowInsets.systemWindowInsetBottom)


        return@OnApplyWindowInsetsListener windowInsets.consumeSystemWindowInsets()

    }
    private fun handleWaterMark() {
        if (viewModel.isUserLogin()){
            binding!!.tvOwner.text = viewModel.getUser()?.username
            binding!!.tvPhone.text = viewModel.getUser()?.phone_number
            binding!!.markLay.show()
        }
        else {
            binding!!.markLay.hide()
        }
    }

    override fun onResume() {
        super.onResume()

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
                    //rightMargin = -150
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



    fun calculatePlayingTime() {
        val totalDuration = currentDuration.div(SECOND_DURATION_INTERVAL)
        currentTime = System.currentTimeMillis().div(SECOND_DURATION_INTERVAL)

        val difference = currentTime!! - initialTime

        val diff = (difference.div(totalDuration )).times(100)

        Log.e("Youtube Player", "calculatePlayingTime: $diff %")
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
            //binding!!.youtubePlayerView.enterFullScreen()

            binding!!.rootPlayer.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //binding!!.youtubePlayerView.exitFullScreen()

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
        playingWaterMark()

    }

    private fun playingWaterMark() {
        binding!!.frameLayout.transitionToStart()
        binding!!.frameLayout.transitionToEnd()
    }
    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {

    }

    override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
        view=fullscreenView
        player.addView(fullscreenView)
        binding!!.rootPlayer.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        player.matchParent()
        //player.layoutParams = binding!!.rootPlayer.layoutParams
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onExitFullscreen() {
        player.removeView(view)
        binding!!.rootPlayer.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        player.wrapContent()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}

