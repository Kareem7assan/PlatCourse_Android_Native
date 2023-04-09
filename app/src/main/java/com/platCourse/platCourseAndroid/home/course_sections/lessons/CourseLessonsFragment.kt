package com.platCourse.platCourseAndroid.home.course_sections.lessons

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentLessonsCourseBinding
import com.platCourse.platCourseAndroid.home.course_sections.files.PdfReaderActivity
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.platCourse.platCourseAndroid.home.youtube.YoutubeActivity
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.lessons.LessonsResponse
import com.rowaad.app.data.model.lessons.VideoModel
import com.rowaad.dialogs_utils.DeleteDialog
import com.rowaad.dialogs_utils.SolveQuizDialog
import com.rowaad.dialogs_utils.SuccessDialog
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import java.net.URL

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
        adapter.onClickItemAssign = ::onClickItemAssign
        adapter.onClickItemVideo = ::onClickVideo
        adapter.onClickItemLink = ::onClickLink
        adapter.onClickItemYoutube = ::onClickItemYoutube
        adapter.onClickItemFirstQuiz = ::onClickItemFirstQuiz
        adapter.onDropDownClicked=::expandLesson
    }

    private fun onClickItemFirstQuiz(lessonsModel: LessonsModel, pos: Int) {
        SolveQuizDialog.show(requireActivity(),lessonsModel.firstUnsolvedRequiredQuiz?.quiz_title ?: ""){
            findNavController().navigate(R.id.action_global_quizDetailsFragment, bundleOf("quiz"
                    to lessonsModel.firstUnsolvedRequiredQuiz?.copy(quiz_questions = lessonsModel.firstUnsolvedRequiredQuiz?.quiz_questions ?: listOf()).toJson()))
        }
    }

    private fun onClickItemAssign(videoModel: VideoModel, pos: Int) {
        findNavController().navigate(R.id.action_global_quizDetailsFragment, bundleOf("quiz"
                to videoModel.quizzes?.map {quizModel->
            quizModel.copy(quiz = quizModel.quiz?.copy(solved = quizModel.solved,
                passed = quizModel.passed,score = quizModel.score,quiz_questions = quizModel.quiz_questions
            ))
        }?.first()?.quiz.toJson()))
    }
    private fun onClickDoc(videoModel: VideoModel, pos: Int) {

        //IntentUtils.openUrl(requireContext(), videoModel.file)

        /*startActivity(Intent( requireContext(), PdfReaderActivity::class.java).also {
            it.putExtra("pdf",videoModel.file)
        })*/

        if (videoModel.downloadable==true) {
            requireActivity().checkDownloadPermissions {
                if (it) {
                    openLink("https://platcourse.com/pdf_file?username=${viewModel.getUser()?.username}&phone_number=${viewModel.getUser()?.phone_number}&link=${videoModel.file!!}")
                }
            }
        }
        else{
            //preview
            startActivity(Intent(requireContext(), PdfReaderActivity::class.java).also {
                it.putExtra("pdf", videoModel.file)
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

    private fun onClickItemYoutube(videoModel: VideoModel, pos: Int,lessonId: Int?) {
        startActivity(Intent(requireContext(),YoutubeActivity::class.java).also {
            Log.e("video_id",videoModel.video_id.toString())
            it.putExtra("video_id",videoModel.video_id)
            it.putExtra("video_title",videoModel.videoName)
            it.putExtra("lesson_id",lessonId)
        })
/*
        findNavController().navigate(
            R.id.action_global_courseDetailsFragment,
            bundleOf(
                "details"
                        to
                        course.toJson(),
                "url"
                        to
                        videoModel.video_id,
                "youtube"
                        to
                        true,
                "lesson_id"
                        to
                        lessonId
            )

        )
*/
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
