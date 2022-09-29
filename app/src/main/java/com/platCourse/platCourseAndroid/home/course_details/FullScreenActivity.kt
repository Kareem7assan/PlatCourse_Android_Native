package com.platCourse.platCourseAndroid.home.course_details

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityFullScreenBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseActivity
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show


class FullScreenActivity : BaseActivity(R.layout.activity_full_screen), Player.Listener, MotionLayout.TransitionListener, WindowInsetsControllerCompat.OnControllableInsetsChangedListener {

    private var simplePlayer: ExoPlayer? = null
    private var binding: ActivityFullScreenBinding? = null
    private var isPlay: Boolean = false
    private val viewModel: CoursesViewModel by viewModels()

    private var initialTime = 0L
    private var updateTime:Long? = null
    private var currentTime:Long? = null
    private var lessonId:Int? = null
    private var isWatched=false
    private var isPlayedForFirstTime=false

    override fun init() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding=ActivityFullScreenBinding.bind(findViewById(R.id.rootFullScreen))
        hideBottomNavigation()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding?.root!!).show(WindowInsetsCompat.Type.systemBars())
        val url=intent.getStringExtra("url")!!
        val pos=intent.getLongExtra("pos", 0L)
        val name=intent.getStringExtra("name")
        lessonId=intent?.getIntExtra("lesson_id",0)
        handleWaterMark(name)
        val isPlay=intent.getBooleanExtra("isPlay", false)
        setupVideo(url, pos, isPlay)


    }

    @SuppressLint("NewApi")
    private fun hideBottomNavigation() {


            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            val windowInsetsController =
                    ViewCompat.getWindowInsetsController(window.decorView) ?: return
            // Configure the behavior of the hidden system bars
            windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // Hide both the status bar and the navigation bar
/*
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            windowInsetsController.removeOnControllableInsetsChangedListener(this)
*/
        windowInsetsController.isAppearanceLightNavigationBars=false
        window.navigationBarColor=Color.TRANSPARENT
        window.navigationBarDividerColor=Color.TRANSPARENT
       // windowInsetsController.isAppearanceLightNavigationBars=true
        /*WindowInsetsControllerCompat(window, window.decorView).apply {
            // Hide the status bar
            hide(WindowInsetsCompat.Type.systemBars())
            // Allow showing the status bar with swiping from top to bottom
            systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        ViewCompat.setOnApplyWindowInsetsListener(
                binding!!.root
        ) { view: View, windowInsets: WindowInsetsCompat ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.layoutParams = (view.layoutParams as FrameLayout.LayoutParams).apply {
                // draw on top of the bottom navigation bar
                bottomMargin = 0
            }

            // Return CONSUMED if you don't want the window insets to keep being
            // passed down to descendant views.
            WindowInsetsCompat.toWindowInsetsCompat(insets)
        }
*/

    }

    private fun handleWaterMark(name: String?) {

        if (viewModel.isUserLogin()){
            binding?.tvOwner?.text = viewModel.getUser()?.name
            binding?.tvPhone?.text = viewModel.getUser()?.phone_number
            binding?.markLay?.show()
        }
        else {
            binding?.markLay?.hide()
        }
    }

    private fun setupVideo(url: String, pos: Long, isPlay: Boolean) {
        simplePlayer = ExoPlayer.Builder(this).build()
        binding?.styledVideo?.player= simplePlayer
        binding?.frameLayout?.addTransitionListener(this)

        simplePlayer!!.addMediaItem(MediaItem.fromUri(url))
        simplePlayer!!.prepare()
        simplePlayer!!.addListener(this)


        val params: ConstraintLayout.LayoutParams = binding?.styledVideo?.layoutParams as ConstraintLayout.LayoutParams
        params.width = MATCH_PARENT
        params.height = MATCH_PARENT
        binding?.styledVideo?.layoutParams = params
        binding?.styledVideo?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL


        if (isPlay)
            setCurrentPosition(simplePlayer!!, pos).also { simplePlayer!!.play() }

        binding?.styledVideo?.setFullscreenButtonClickListener {
            onBackPressed()
        }
        simplePlayer?.addListener(object : Player.Listener{
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                this@FullScreenActivity.isPlay=isPlaying

                if (isPlaying && viewModel.isUserLogin()){
                    if (!isPlayedForFirstTime){
                        //change flag
                        isPlayedForFirstTime=true
                        initialTime=System.currentTimeMillis().div(CourseDetailsFragment.SECOND_DURATION_INTERVAL)
                    }else{
                        if (updateTime!=null){
                            initialTime= System.currentTimeMillis().div(CourseDetailsFragment.SECOND_DURATION_INTERVAL).minus(updateTime?:0L)
                            updateTime=null
                        }
                    }
                    binding?.styledVideo?.postDelayed(this@FullScreenActivity::calculatePlayingTime,
                        CourseDetailsFragment.SECOND_DURATION_INTERVAL
                    )
                }
                if (!isPlaying){
                    updateTime= currentTime?.minus((initialTime))
                }
            }

        })
    }

    fun calculatePlayingTime(){
        val totalDuration = simplePlayer?.duration?.toFloat()?.div(CourseDetailsFragment.SECOND_DURATION_INTERVAL)
        currentTime = System.currentTimeMillis().div(CourseDetailsFragment.SECOND_DURATION_INTERVAL)

        val difference = currentTime!! - initialTime

        val diff= (difference.div(totalDuration?:0f)).times(100)


        //check if video pass 70% watching
        if (lessonId!=0 && isWatched.not()&&diff>= CourseDetailsFragment.WATCHED_DURATION) {
            //change flag
            isWatched=true
            //send lesson to server to mark video as watched
            lessonId?.let { viewModel.sendRequestMarkVideo(it) }
        }

        if (simplePlayer?.isPlaying==true){
            Handler(Looper.getMainLooper()).postDelayed(this::calculatePlayingTime,
                CourseDetailsFragment.SECOND_DURATION_INTERVAL
            )
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

        if (isPlay && viewModel.isUserLogin()) playingWaterMark()


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

    override fun onControllableInsetsChanged(controller: WindowInsetsControllerCompat, typeMask: Int) {

    }
}

/*
fun Activity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }

            navigationBarColor = Color.TRANSPARENT
        }
    }
}

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}
*/
