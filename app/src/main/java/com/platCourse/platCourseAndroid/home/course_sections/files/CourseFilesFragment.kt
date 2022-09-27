package com.platCourse.platCourseAndroid.home.course_sections.files

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentFilesCourseBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.cache.fromJson
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.files.File
import com.rowaad.app.data.model.files.FilesModel
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class CourseFilesFragment : BaseFragment(R.layout.fragment_files_course) {

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

        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
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
        binding?.progressMore?.show()
    }

    private fun hideBottomProgress() {
        binding?.progressMore?.hide()
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