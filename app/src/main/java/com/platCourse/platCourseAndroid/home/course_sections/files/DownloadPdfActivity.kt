package com.platCourse.platCourseAndroid.home.course_sections.files

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Base64
import android.util.Log
import android.view.Window
import android.webkit.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityDownloadPdfBinding
import com.rowaad.app.base.BaseActivity
import com.rowaad.utils.extention.toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.*

class DownloadPdfActivity : BaseActivity(R.layout.activity_download_pdf) {

    private var binding: ActivityDownloadPdfBinding? = null

    override fun init() {
        binding = ActivityDownloadPdfBinding.bind(findViewById(R.id.frameRoot))
        val webSetting = binding!!.courseWebView.settings
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
        webSetting.javaScriptEnabled = true

        val url=intent.getStringExtra("url")
       // binding!!.courseWebView.webViewClient = MyWebViewClient()
        //binding!!.courseWebView!!.addJavascriptInterface(WebAppInterface(this), "androidInterface")



        binding!!.courseWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(WebAppInterface(this@DownloadPdfActivity).getBase64StringFromBlobUrl(url!!)!!)
                //val url = "http://www.example.com"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)

                return false
            }
        }


        Log.e("url11", url ?: "")


    }


    inner class MyWebViewClient : WebViewClient(), ValueCallback<String> {

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            Log.e("url4", url + ",,,,")
        }

        override    fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

            //view!!.loadUrl(url!!)
            Log.e("url", url.toString())
            if (url!!.contains("blob")) {
                toast("contains")
                //binding!!.courseWebView.loadUrl(WebAppInterface(this@DownloadPdfActivity).getBase64StringFromBlobUrl(url!!)!!)
                Log.e("url3", url.toString())
               // val url = "https://docs.google.com/viewer?embedded=true&url=blob:https://platcourse.com/94e2ffdf-9a86-417e-92fd-a465b04d3d3f"
                binding!!.courseWebView!!.addJavascriptInterface(WebAppInterface(this@DownloadPdfActivity), "androidInterface")

                //WebAppInterface(this@DownloadPdfActivity).getBase64StringFromBlobUrl(url!!)
                view?.loadUrl(WebAppInterface(this@DownloadPdfActivity).getBase64StringFromBlobUrl(url!!)!!)
                return true

            }
            return false
        }



        override fun onReceiveValue(value: String?) {
            Log.e("version", value.toString() + ",")
        }
    }

    inner class WebAppInterface(private val mContext: Context) {


        /** Show a toast from the web page  */

        @JavascriptInterface
        fun accessFromJS(window: Window?) {

        }

        @JavascriptInterface
        fun download_pdf_file(url: String, name: String, phone: String) {
            Log.e("url2", url)
            getBase64StringFromBlobUrl(url)

        }

        internal fun getBase64StringFromBlobUrl(blobUrl: String): String? {

            return if (blobUrl.startsWith("blob")) {
                Log.e("blod", "yes")
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


        @JavascriptInterface
        @Throws(IOException::class)
        fun getBase64FromBlobData(base64Data: String?) {
            convertBase64StringToPdfAndStoreIt(base64Data ?: "")
        }

        @Throws(IOException::class)
        private fun convertBase64StringToPdfAndStoreIt(base64PDf: String) {
            val notificationId = 1
            val currentDateTime = DateFormat.getDateTimeInstance().format(Date())
            val dwldsPath = File(getExternalFilesDir(
                    Environment.DIRECTORY_DOWNLOADS).toString() + "/YourFileName_" + currentDateTime.replace(" ", "") + "_.pdf")
            val pdfAsBytes = Base64.decode(base64PDf.replaceFirst("^data:application/pdf;base64,".toRegex(), ""), 0)
            val os: FileOutputStream = FileOutputStream(dwldsPath, false)
            os.write(pdfAsBytes)
            os.flush()
            if (dwldsPath.exists()) {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val apkURI = FileProvider.getUriForFile(this@DownloadPdfActivity, applicationContext.packageName + ".provider", dwldsPath)
                intent.setDataAndType(apkURI, MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"))
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)

                val pendingIntent = PendingIntent.getActivity(this@DownloadPdfActivity, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                val CHANNEL_ID = "MYCHANNEL"
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notificationChannel = NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_LOW)
                    val notification = Notification.Builder(this@DownloadPdfActivity, CHANNEL_ID)
                            .setContentText("You have got something new!")
                            .setContentTitle("File downloaded")
                            .setContentIntent(pendingIntent)
                            .setChannelId(CHANNEL_ID)
                            .setSmallIcon(android.R.drawable.sym_action_chat)
                            .build()
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(notificationChannel)
                        notificationManager.notify(notificationId, notification)
                    }
                } else {
                    val b = NotificationCompat.Builder(this@DownloadPdfActivity, CHANNEL_ID)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(android.R.drawable.sym_action_chat) //.setContentIntent(pendingIntent)
                            .setContentTitle("MY TITLE")
                            .setContentText("MY TEXT CONTENT")
                    if (notificationManager != null) {
                        notificationManager.notify(notificationId, b.build())
                        val h = Handler()
                        val delayInMilliseconds: Long = 1000
                        h.postDelayed({ notificationManager.cancel(notificationId) }, delayInMilliseconds)
                    }
                }
            }
            Toast.makeText(this@DownloadPdfActivity, "PDF FILE DOWNLOADED!", Toast.LENGTH_SHORT).show()
        }


    }
}