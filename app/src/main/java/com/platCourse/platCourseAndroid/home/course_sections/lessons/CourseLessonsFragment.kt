package com.platCourse.platCourseAndroid.home.course_sections.lessons

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentLessonsCourseBinding
import com.platCourse.platCourseAndroid.home.course_sections.files.PdfReaderActivity
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.lessons.LessonsResponse
import com.rowaad.app.data.model.lessons.VideoModel
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.fromJson
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import com.rowaad.utils.extention.toJson
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

class CourseLessonsFragment : BaseFragment(R.layout.fragment_lessons_course) {

    private var lessonId: Int? = null
    private var course: CourseItem? = null
    private var course_id: Int? = null
    private val binding by viewBinding<FragmentLessonsCourseBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()
    private val adapter by lazy {
        SectionAdapter()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        course = arguments?.getString("course")?.fromJson<CourseItem>()
        course_id = arguments?.getInt("course_id")
        lessonId = arguments?.getInt("lesson_id")
        handleToolbar()
        setupRec()
        sendRequest()
        handleLessonsFlow()
        setupActions()
    }

    private fun setupActions() {
        adapter.onClickItemDoc = ::onClickDoc
        adapter.onClickItemVideo = ::onClickVideo
        adapter.onClickItemLink = ::onClickLink
        adapter.onDropDownClicked=::expandLesson
    }

    private fun onClickDoc(videoModel: VideoModel, pos: Int) {

        //IntentUtils.openUrl(requireContext(), videoModel.file)
        startActivity(Intent( requireContext(), PdfReaderActivity::class.java).also {
            it.putExtra("pdf",videoModel.file)
        })
    }

    private fun expandLesson(parentPosition:Int){
        adapter.updateSelectedItem(parentPosition,lessonId)

    }
    private fun onClickVideo(videoModel: VideoModel, pos: Int, lessonId: Int?) {

        findNavController().navigate(
                R.id.action_global_courseDetailsFragment,
                bundleOf(
                        "details"
                                to
                                course.toJson(),
                        "url"
                                to
                                videoModel.video_file,
                        "lesson_id"
                                to
                                lessonId
                )

        )
    }

    private fun onClickLink(videoModel: VideoModel, pos: Int) {
        IntentUtils.openUrl(requireContext(), videoModel.video_link)
    }

    private fun handleToolbar() {
        binding.toolbar.tvTitle.text = getString(R.string.lessons)
        binding.toolbar.ivBack.onClick {
            findNavController().navigateUp()
        }
    }

    private fun handleLessonsFlow() {
        handleSharedFlow(viewModel.lessonsFlow, onSuccess = {
            val sections = it as List<LessonsResponse>
            val section=sections.find {section->
                section.lessons.find { it.id == lessonId }!=null
            }
            section?.lessons?.map {
                it.isExpanded=it.id==lessonId
            }


            if (sections.isEmpty())
                binding.rvLessonsTitles.hide().also { binding.tvEmpty.show() }
                        .also { binding.rvLessonsTitles.hide() }
            else
                binding.rvLessonsTitles.show().also {
                    adapter.swapData(sections)
                }
                        .also { binding.tvEmpty.hide() }

            if (lessonId!=null) expandLesson(sections.indexOf(section))
        })

    }

    private fun sendRequest() {
        viewModel.sendRequestLessons(courseId = course?.id ?: course_id?:0)
    }

    private fun setupRec() {
        binding.rvLessonsTitles.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLessonsTitles.adapter = adapter

    }
}
