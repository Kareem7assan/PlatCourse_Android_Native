package com.platCourse.platCourseAndroid.home.course_sections.quiz

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.*
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentAdsBinding
import com.platCourse.platCourseAndroid.databinding.FragmentQuizBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.cache.fromJson
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.TOKENS
import org.jetbrains.anko.configuration
import org.jetbrains.anko.sdk27.coroutines.onTouch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.*

class QuizWebViewFragment : BaseFragment(R.layout.fragment_ads) {

    private var quizId: Int? = 0
    private var courseId: Int? = 0
    private var course: CourseItem? = null
    private val binding by viewBinding<FragmentAdsBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        course = arguments?.getString("course")?.fromJson<CourseItem>()
        quizId = arguments?.getInt("quiz")
        courseId = arguments?.getInt("course_id")
        setupWebView()
    }



    private fun setupWebView() {
        val mWebView = binding.webView
        val webSetting = mWebView.settings
        webSetting.javaScriptEnabled = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.domStorageEnabled = true
        webSetting.setAppCacheEnabled(true)
        webSetting.loadWithOverviewMode = true
        webSetting.loadsImagesAutomatically = true
        webSetting.builtInZoomControls = true
        webSetting.loadWithOverviewMode = true
        webSetting.loadsImagesAutomatically = true
        webSetting.useWideViewPort = true
        webSetting.displayZoomControls = false
        webSetting.setSupportMultipleWindows(true)
        webSetting.setSupportZoom(true)

        webSetting.setSupportMultipleWindows(true)
        webSetting.setSupportZoom(true)
        webSetting.defaultTextEncodingName = "utf-8"
        webSetting.setLightTouchEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setLoadWithOverviewMode(true);

        webSetting.setSupportZoom(true)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
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
        mWebView.loadUrl("https://platcourse.com/ios/quiz/${course?.id?:courseId}/$quizId/${viewModel.getToken()}")
        //mWebView.loadUrl("https://platcourse.com/ios/quiz/${course?.id}/$quizId/${viewModel.getToken()}")
       // mWebView.addJavascriptInterface(WebAppInterface(requireContext()), "androidInterface")
        mWebView.webViewClient = object : WebViewClient(), ValueCallback<String> {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val browserIntent = Intent(Intent.ACTION_VIEW)
                Log.e("url", url)
                browserIntent.data = Uri.parse(url)
                context?.startActivity(browserIntent)
                return true
            }

            override fun onReceiveValue(p0: String?) {

            }
        }


    }

    inner class WebAppInterface(private val mContext: Context) {


        /** Show a toast from the web page  */

        @JavascriptInterface
        fun accessFromJS(window: Window?) {

        }

        @JavascriptInterface
        fun download_pdf_file(url: String, name: String, phone: String) {
            Log.e("url", url)


        }

        internal fun getBase64StringFromBlobUrl(blobUrl: String): String? {
            return if (blobUrl.startsWith("blob")) {
                "javascript: var xhr = new XMLHttpRequest();" +
                        "xhr.open('GET', '" + blobUrl + "', true);" +
                        "xhr.setRequestHeader('Content-type','application/pdf');" +
                        "xhr.responseType = 'blob';" +
                        "xhr.onload = function(e) {" +
                        "    if (this.status == 200) {" +
                        "        var blobPdf = this.response;" +
                        "        var reader = new FileReader();" +
                        "        reader.readAsDataURL(blobPdf);" +
                        "        reader.onloadend = function() {" +
                        "            base64data = reader.result;" +
                        "            androidInterface.getBase64FromBlobData(base64data);" +
                        "        }" +
                        "    }" +
                        "};" +
                        "xhr.send();"
            } else "javascript: console.log('It is not a Blob URL');"
        }

    }
    }