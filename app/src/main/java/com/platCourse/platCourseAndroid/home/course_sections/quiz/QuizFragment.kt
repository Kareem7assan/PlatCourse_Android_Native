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
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentQuizBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.cache.fromJson
import com.rowaad.app.data.cache.toJson
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.quiz_model.QuizModel
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import org.jetbrains.anko.configuration
import org.jetbrains.anko.sdk27.coroutines.onTouch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.*

class QuizFragment : BaseFragment(R.layout.fragment_quiz) {

    private var course: CourseItem? = null
    private val binding by viewBinding<FragmentQuizBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()
    private val adapter by lazy {
        QuizTitleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        course = arguments?.getString("course")?.fromJson<CourseItem>()
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
        viewModel.sendRequestQuizzes(courseId = course?.id ?: 0)
    }

    private fun setupRec() {
        binding.rvQuizzes.layoutManager=LinearLayoutManager(requireContext())
        binding.rvQuizzes.adapter=adapter
    }


}