package com.platCourse.platCourseAndroid.home.course_sections.announcements

import android.annotation.SuppressLint
import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.webkit.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentAdsBinding
import com.platCourse.platCourseAndroid.databinding.FragmentQuizBinding
import com.platCourse.platCourseAndroid.home.course_sections.quiz.QuizWebViewFragment
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.cache.fromJsonPref
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.utils.extention.fromJson
import org.jetbrains.anko.configuration
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.*

class AnnouncementsFragment : BaseFragment(R.layout.fragment_ads) {

    private var course: CourseItem? = null
    private val binding by viewBinding<FragmentAdsBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        course = arguments?.getString("course")?.fromJson<CourseItem>()
        setupWebView()
    }


    private fun setupWebView() {
        val mWebView = binding.webView
        mWebView.isNestedScrollingEnabled = true
        val webSetting = mWebView.settings
        webSetting.javaScriptEnabled = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.domStorageEnabled = true
        //webSetting.setAppCacheEnabled(true)
        webSetting.loadWithOverviewMode = true
        webSetting.loadsImagesAutomatically = true
        webSetting.useWideViewPort = true
        webSetting.builtInZoomControls = true
        webSetting.displayZoomControls = false
        webSetting.setSupportMultipleWindows(true)
        webSetting.setSupportZoom(true)
        webSetting.defaultTextEncodingName = "utf-8"
        webSetting.builtInZoomControls = true
        webSetting.setSupportZoom(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when (requireActivity().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    webSetting.forceDark = WebSettings.FORCE_DARK_OFF
                } // Night mode is not active, we're using the light theme
                Configuration.UI_MODE_NIGHT_YES -> {
                    webSetting.forceDark = WebSettings.FORCE_DARK_ON
                } // Night mode is active, we're using dark theme
            }

        }
        showProgress()
        Handler(Looper.getMainLooper()).postDelayed({
            hideProgress()
        }, 3000)
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        val myChromeClient = CustomWebClient(requireActivity())
        mWebView.webChromeClient = myChromeClient
        mWebView.loadUrl("https://platcourse.com/ios/announcement/${course?.id}/${viewModel.getToken()}")
        mWebView.webViewClient = object : WebViewClient(), ValueCallback<String> {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.e("url", url)
                view.loadUrl(url)
                toast("kkk")
                toast(url + ",,,")
                val browserIntent = Intent(Intent.ACTION_VIEW)
                Log.e("url", url)
                browserIntent.data = Uri.parse(url)
                context?.startActivity(browserIntent)
                return false
            }

            override fun onReceiveValue(p0: String?) {
                toast(p0 + ",,,/")

            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                Log.e("url", url + ",,,,")
            }
        }


    }


    inner class CustomWebClient(val context: Activity)  // Constructor for CustomWebClient
        : WebChromeClient(), ValueCallback<String> {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null

        // Initially mOriginalOrientation is set to Landscape
        private var mOriginalOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        private var mOriginalSystemUiVisibility = 0



        @SuppressLint("NewApi")
        override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
        ): Boolean {
            val newWebView = WebView(context)
            view!!.addView(newWebView)
            val transport = resultMsg!!.obj as WebView.WebViewTransport
            transport.webView = newWebView
            resultMsg.sendToTarget()
            newWebView.settings.javaScriptEnabled=true

            newWebView.webViewClient = object : WebViewClient(), ValueCallback<String> {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    val browserIntent = Intent(Intent.ACTION_VIEW)
                    browserIntent.data = Uri.parse(url)
                    context.startActivity(browserIntent)
                    return true
                }



                override fun onReceiveValue(value: String?) {
                    Log.e("versions3", value.toString())
                }
            }

            return true
        }
        override fun getDefaultVideoPoster(): Bitmap? {
            return BitmapFactory.decodeResource(
                    context.resources, 2130837573
            )
        }

        override fun onShowCustomView(paramView: View, viewCallback: CustomViewCallback) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = paramView
            mOriginalSystemUiVisibility =
                    context.window.decorView.systemUiVisibility
            // When CustomView is shown screen orientation changes to mOriginalOrientation (Landscape).
            context.requestedOrientation = mOriginalOrientation
            // After that mOriginalOrientation is set to portrait.
            mOriginalOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            mCustomViewCallback = viewCallback
            (context.window.decorView as FrameLayout).addView(
                    mCustomView,
                    FrameLayout.LayoutParams(-1, -1)
            )
            context.window.decorView.systemUiVisibility = 3846
        }

        override fun onHideCustomView() {
            (context.window.decorView as FrameLayout).removeView(mCustomView)
            mCustomView = null
            context.window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
            // When CustomView is hidden, screen orientation is set to mOriginalOrientation (portrait).
            context.requestedOrientation = mOriginalOrientation
            // After that mOriginalOrientation is set to landscape.
            mOriginalOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            mCustomViewCallback!!.onCustomViewHidden()
            mCustomViewCallback = null
        }

        override fun onReceiveValue(value: String?) {
            Log.e("Version2", value.toString())
        }
    }

}