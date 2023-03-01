package com.platCourse.platCourseAndroid.home.course_sections.quiz.quiz_details

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentDetailsQuizBinding
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.platCourse.platCourseAndroid.home.course_sections.quiz.QuizViewModel
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.quiz_model.*
import com.rowaad.utils.extention.fromJson
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.invisible
import com.rowaad.utils.extention.show
import org.jetbrains.anko.support.v4.toast
import java.text.DecimalFormat
import kotlin.math.roundToInt

class QuizDetailsFragment : BaseFragment(R.layout.fragment_details_quiz) {
    private var hasAnswered: Boolean = false
    private var timer: CountDownTimer? = null
    private var quiz: QuizItem? = null
    private val binding by viewBinding<FragmentDetailsQuizBinding>()
    private val viewModel: QuizViewModel by viewModels()
    private val adapter by lazy {
        QuestionAdapter()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quiz = arguments?.getString("quiz")?.fromJson<QuizItem>()
        Log.e("quiz",quiz.toString())
        setupRec()
        //sendRequestQuiz()
        handleExamPage()
        handleRespQuiz()
        handleRespAnswers()
        (requireActivity() as HomeActivity).setupTitle(quiz?.quiz_title!!)
        setupActions()

    }

    private fun setupActions() {
        binding.finishBtn.setOnClickListener {
            sendRequestFinish(true)
        }
    }

    private fun setupRec() {
        binding.rvQuestions.layoutManager=LinearLayoutManager(requireContext())
        binding.rvQuestions.adapter=adapter
    }

    private fun handleRespQuiz() {
        handleSharedFlow(viewModel.quizFlow,onSuccess = {it as QuizItem
            quiz=it
            handleExamPage()
        })
    }
    private fun handleRespAnswers() {
        handleSharedFlow(viewModel.answersFlow,onSuccess = {val resp=it as QuizModel
            quiz=resp.quiz?.copy(questions = resp.quiz?.questions,solved = resp.solved,passed = resp.is_passed ?: resp.passed,score = resp.score,quiz_questions = resp.quiz_questions)
            handleSolvedQuestions()
        })
    }
    private fun handleSolvedQuestions() {
        if (quiz?.solved==true){
            Log.e("quiz",quiz.toString())
            if (quiz?.passed==true) {
                binding.finishBtn.hide()
                binding.ivTimer.hide()
                binding.tvTimer.invisible()
                showSuccessMsg(getString(R.string.solved_successfully))
            }
            else{
                showErrorMessage(getString(R.string.not_solved_successfully))
            }
            resetTimer()
            binding.tvAnswers.show()
            binding.tvQuesNums.show()
            binding.tvAnswers.text=String.format(getString(R.string.result_mark),quiz?.score?.roundToInt())+"/"+quiz?.questions?.size.toString()
            adapter.swapData(quizQuestions = quiz!!.questions as MutableList<QuestionItem>,studentAnswers = quiz!!.quiz_questions as MutableList<QuizQuestion>,solved = quiz!!.solved ?: false,passed = quiz!!.passed ?: false)
            binding.tvQuesNums.text= String.format(getString(R.string.questions_num),quiz?.questions?.size ?: 0)
            hasAnswered=true
        }

    }

    private fun resetTimer() {
        timer?.cancel()
        binding.ivTimer.invisible()
        binding.tvTimer.invisible()
        binding.finishBtn.hide()
        timer=null

    }
    private fun handleExamPage() {
        if (quiz?.solved==true){
            if (quiz?.passed==true) {
                binding.finishBtn.hide()
                binding.ivTimer.hide()
                binding.tvTimer.invisible()
            }
            else {
                handleTimer(quiz?.time ?: 10f)
            }
            binding.tvAnswers.show()
            binding.tvAnswers.text=String.format(getString(R.string.result_mark),quiz?.score?.roundToInt())+"/"+quiz?.questions?.size.toString()
            adapter.swapData(quizQuestions = quiz!!.questions as MutableList<QuestionItem>,studentAnswers = quiz!!.quiz_questions as MutableList<QuizQuestion>,solved = quiz!!.solved ?: false,passed = quiz!!.passed ?: false)
            hasAnswered=true
        }
        else{
            binding.finishBtn.show()
            binding.ivTimer.show()
            binding.tvTimer.show()
            binding.tvAnswers.hide()
            handleTimer(quiz?.time ?: 10f)
            adapter.swapData(quizQuestions = quiz!!.questions as MutableList<QuestionItem>,studentAnswers = quiz!!.quiz_questions as MutableList<QuizQuestion>,solved = quiz!!.solved ?: false,passed = quiz!!.passed ?: false)
        }
        binding.tvQuesNums.text= String.format(getString(R.string.questions_num),quiz?.questions?.size ?: 0)
    }
    private fun handleTimer(time: Float) {
        timer = object : CountDownTimer(time.toLong() * 1000*10*6, 1000) {
            override fun onTick(tick: Long) {
                val format = DecimalFormat("00")
                val min = (tick / 60000) % 60
                val sec = (tick / 1000) % 60
                binding.tvTimer.text = format.format(min)+":"+format.format(sec)
            }

            override fun onFinish() {

                sendRequestFinish(true)
            }

        }
        timer?.start()
    }

    private fun sendRequestFinish(watchChanges:Boolean){
        adapter.quizQuestions.map {
            it.selected_answer
        }
        val data = adapter.quizQuestions.map {
            PostAnswers(id = it.id, answer = it.selected_answer)
        }
        Log.e("data",data.toString())
        if (watchChanges)
            viewModel.sendRequestAnswers(quizId = quiz!!.id,answers = AnswersModel(data))
        else
            viewModel.sendRequestAnswersBg(quizId = quiz!!.id,answers = AnswersModel(data))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (hasAnswered.not()) sendRequestFinish(false)
        timer?.cancel()
    }
}