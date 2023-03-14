package com.platCourse.platCourseAndroid.home.course_sections.files

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentFilesCourseBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.files.File
import com.rowaad.app.data.model.files.FilesModel
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.util.*


class CourseFilesFragment : BaseFragment(R.layout.fragment_files_course) {

    private lateinit var myExternalFile: java.io.File
    private var file: File? = null
    private var course: CourseItem? = null
    private val binding by viewBinding<FragmentFilesCourseBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()
    private val adapter by lazy { FileAdapter() }
    private var pageNumber: Int = 1
    private var next:String?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        course=arguments?.getString("course")?.fromJson<CourseItem>()
        handlePage()
        setupRec()
        sendRequestMyFiles()
        handleFilesFlow()
        setupActions()
    }

    private fun setupActions() {
        adapter.onClickItem=::onClickDownload
    }

    private fun handlePage() {
        binding.nestedScrollView.handlePagination {
            pageNumber++
            if (hasNext()) sendRequestMyFiles()

        }
    }

    private fun onClickLink(item: File, pos: Int) {
        IntentUtils.openUrl(requireContext(), item.file)
    }

    private fun onClickDownload(item: File, pos: Int) {
        if (item.downloadable==true) {
            file = item
            requireActivity().checkDownloadPermissions {
                if (it) {
                    openLink("https://platcourse.com/pdf_file?username=${viewModel.getUser()?.username}&phone_number=${viewModel.getUser()?.phone_number}&link=${item.file!!}")
                    ///downloadLink("https://platcourse.com/pdf_file?username=${viewModel.getUser()?.username}&phone_number=${viewModel.getUser()?.phone_number}&link=${item.file!!}")
                }
            }
        }
        else{
            //preview
                startActivity(Intent(requireContext(), PdfReaderActivity::class.java).also {
                    it.putExtra("pdf", item.file)
                })
            //IntentUtils.openUrl(requireContext(),item.file)
        }
    }

    private fun openLink(url: String?) {
        lifecycleScope.launch(Dispatchers.IO) {
             URL(url).openStream()

        }
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        i.flags=  Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
        startActivity(i)
    }

    private fun downloadLink(url: String) {
        Log.e("url",url)

        lifecycleScope.launch(Dispatchers.IO) {
            //convertBase64StringToFileAndStoreIt(getBase64StringFromBlobUrl())
           /* val url = URL(url)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.instanceFollowRedirects=true
            try {
                val inputStream: InputStream = urlConnection.inputStream
                //val saveFilePath: String = requireActivity().externalCacheDir?.path + File.separator + fileName
                if (isExternalStorageAvailable()){
                    myExternalFile =  java.io.File(requireActivity().getExternalFilesDir("platcourse"), System.currentTimeMillis().toString()+".pdf")
                }
                else{
                    Log.e("isAvailable","false")
                }
                // opens an output stream to save into file
                // opens an output stream to save into file
                val outputStream = FileOutputStream(myExternalFile)
                var bytesRead = -1
                val buffer = ByteArray(1024 * 1024)
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                outputStream.close()
                inputStream.close()


            } finally {
                urlConnection.disconnect()
            }*/
    }
    }

    fun getBase64StringFromBlobUrl(blobUrl: String): String? {
        if (blobUrl.startsWith("blob")) {
            //fileMimeType = mimeType
            return "javascript: var xhr = new XMLHttpRequest();" +
                    "xhr.open('GET', '" + blobUrl + "', true);" +
                    "xhr.setRequestHeader('Content-type','" + "application/pdf" + ";charset=UTF-8');" +
                    "xhr.responseType = 'blob';" +
                    "xhr.onload = function(e) {" +
                    "    if (this.status == 200) {" +
                    "        var blobFile = this.response;" +
                    "        var reader = new FileReader();" +
                    "        reader.readAsDataURL(blobFile);" +
                    "        reader.onloadend = function() {" +
                    "            base64data = reader.result;" +
                    "            Android.getBase64FromBlobData(base64data);" +
                    "        }" +
                    "    }" +
                    "};" +
                    "xhr.send();"
        }
        return "javascript: console.log('It is not a Blob URL');"
    }

    private fun convertBase64StringToFileAndStoreIt(base64PDf:String) {
        val notificationId = 1
        val currentDateTime = DateFormat.getDateTimeInstance().format( Date())
        val newTime = currentDateTime.replaceFirst(", ","_").replace(" ","_").replace(":","-")
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val extension = mimeTypeMap.getExtensionFromMimeType("application/pdf")
        val dwldsPath =  java.io.File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS).absolutePath + "/" + newTime + "_." + extension)
        val regex = "^data:" + "application/pdf" + ";base64,"
        val pdfAsBytes = Base64.getDecoder().decode(base64PDf.replaceFirst(regex, ""))
        try {
            val os =  FileOutputStream(dwldsPath)
            os.write(pdfAsBytes)
            os.flush()
            os.close()
        } catch (e:Exception) {
            Toast.makeText(context, "FAILED TO DOWNLOAD THE FILE!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        if (dwldsPath.exists()) {
            val intent =  Intent()
            intent.action = Intent.ACTION_VIEW
            val apkURI = FileProvider.getUriForFile(requireContext(),requireContext().applicationContext.packageName + ".provider", dwldsPath)
            intent.setDataAndType(apkURI, MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val pendingIntent = PendingIntent.getActivity(context,1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val  CHANNEL_ID = "MYCHANNEL"
            val  notificationManager =  requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val  notificationChannel=  NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_LOW)
            val notification =  Notification.Builder(context,CHANNEL_ID)
                .setContentText("You have got something new!")
                .setContentTitle("File downloaded")
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .build()
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel)
                notificationManager.notify(notificationId, notification)
            }
        }
        Toast.makeText(context, "FILE DOWNLOADED!", Toast.LENGTH_SHORT).show()
    }

    private fun isExternalStorageAvailable():Boolean{
        val extStorageState: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }

    private fun paramsToString(params: Map<String, String>?): String? {
        if (params == null || params.isEmpty()) {
            return ""
        }
        val builder = Uri.Builder()

        return builder.build().encodedQuery
    }







    private interface MyDownloadService{
        @GET("pdf_file")
        suspend fun download(
            @Query("username") username:String,
            @Query("phone_number") phone_number:String,
            @Query("link") link:String
        )
    }
    private fun hasNext(): Boolean {
        return next!=null
    }

    private fun handleFilesFlow() {
        handleSharedFlow(viewModel.filesFlow,
                onShowProgress = {
                    if (pageNumber > 1) showBottomProgress() /*else showProgress()*/
                }, onHideProgress = {
            if (pageNumber > 1) hideBottomProgress() /*else hideProgress()*/

        }, onSuccess = {
            val fileModel = it as FilesModel
            if (fileModel.results?.isEmpty() == true)
                binding.rvFiles.hide().also { binding.tvEmpty.show() }.also { binding.rvFiles.hide() }
            else
                binding.rvFiles.show().also { adapter.addData(fileModel.results!!) }.also { binding.tvEmpty.hide() }
        })
    }

    private fun sendRequestMyFiles() {
        viewModel.sendRequestFiles(courseId = course?.id ?: 0, pageNumber = pageNumber)
    }

    private fun setupRec() {
        binding.rvFiles.layoutManager=LinearLayoutManager(requireContext())
        binding.rvFiles.adapter=adapter

    }
    private fun showBottomProgress() {
        binding.progressMore.show()
    }

    private fun hideBottomProgress() {
        binding.progressMore.hide()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty())
            onClickDownload(file!!, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.clear()
    }
}