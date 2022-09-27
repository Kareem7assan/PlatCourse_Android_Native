package com.platCourse.platCourseAndroid.home.course_sections.files

import androidx.lifecycle.lifecycleScope
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.ScrollHandle
import com.platCourse.platCourseAndroid.R
import com.rowaad.app.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class PdfReaderActivity : BaseActivity(R.layout.activity_pdf_reader) {

    private var pdfReader: PDFView? = null
    var url=""
    override fun init() {
         pdfReader = findViewById(R.id.pDFView)
        url=intent.getStringExtra("pdf") ?: ""
            previewStream()
    }

    private fun previewStream() {
        lifecycleScope.launch(Dispatchers.IO){
            var inputStream: InputStream? = null
            try {
                val url = URL(url)
                // below is the step where we are
                // creating our connection.
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
                if (urlConnection.responseCode == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            } catch (e: IOException) {
                // this is the method
                // to handle errors.
                e.printStackTrace()
            }
            withContext(Dispatchers.Main){
                pdfReader?.fromStream(inputStream)
                        ?.load()


            }


        }

    }

}