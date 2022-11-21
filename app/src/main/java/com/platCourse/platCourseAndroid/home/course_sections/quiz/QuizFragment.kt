package com.platCourse.platCourseAndroid.home.course_sections.quiz

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentQuizBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.quiz_model.QuizModel
import com.rowaad.utils.extention.fromJson
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import com.rowaad.utils.extention.toJson

class QuizFragment : BaseFragment(R.layout.fragment_quiz) {

    private var course: CourseItem? = null
    private var courseId:Int? = null
    private val binding by viewBinding<FragmentQuizBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()
    private val adapter by lazy {
        QuizTitleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        course = arguments?.getString("course")?.fromJson<CourseItem>()
        courseId = arguments?.getInt("course_id")
        setupRec()
        sendRequest()
        handleObservable()
        setupActions()
    }

    private fun setupActions() {
        adapter.onClickItem=::onClickQuiz
    }

    private fun onClickQuiz(quizModel: QuizModel, pos: Int) {
        findNavController().navigate(R.id.action_global_quizWebViewFragment,
                bundleOf(
                        "course"
                        to
                                course.toJson(),
                        "quiz"
                        to
                                quizModel.quiz?.id

                )
                )
    }

    private fun handleObservable() {
        handleSharedFlow(viewModel.quizzesFlow,onSuccess = { it as List<QuizModel>
            if (it.isNullOrEmpty())
                binding.rvQuizzes.hide().also { binding.tvEmpty.show() }
            else
                adapter.swapData(it)
        })
    }

    private fun sendRequest() {
        viewModel.sendRequestQuizzes(courseId = course?.id ?: courseId?:0)
    }

    private fun setupRec() {
        binding.rvQuizzes.layoutManager=LinearLayoutManager(requireContext())
        binding.rvQuizzes.adapter=adapter
    }


}