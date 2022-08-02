package com.platCourse.platCourseAndroid.menu.terms

import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.webkit.WebSettings
import android.webkit.WebSettings.FORCE_DARK_OFF
import android.webkit.WebSettings.FORCE_DARK_ON
import androidx.activity.viewModels
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityTermsBinding
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.rowaad.app.base.BaseActivity
import org.jetbrains.anko.configuration

class TermsActivity : BaseActivity(R.layout.activity_terms) {

    private lateinit var binding: ActivityTermsBinding

    override fun init() {
        binding = ActivityTermsBinding.bind(findViewById(R.id.rootView))
        setupActions()
        setupWebView()

    }

    private fun setupActions() {
        binding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.tvTitle.text=""
    }

    private fun setupWebView() {
            val mWebView=binding.webView
            val webSetting = mWebView.settings
            webSetting.javaScriptEnabled = true
            webSetting.javaScriptCanOpenWindowsAutomatically = true
            webSetting.domStorageEnabled = true
            webSetting.setAppCacheEnabled(true)
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            when (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {webSetting.forceDark= FORCE_DARK_OFF} // Night mode is not active, we're using the light theme
                Configuration.UI_MODE_NIGHT_YES -> {webSetting.forceDark=FORCE_DARK_ON} // Night mode is active, we're using dark theme
            }

        }
        showProgress()
            Handler(Looper.getMainLooper()).postDelayed({
                hideProgress()
            },3000)
            webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)
            webSetting.javaScriptCanOpenWindowsAutomatically = true
            mWebView.loadUrl("https://platcourse.com/terms")
        }


}