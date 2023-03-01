package com.platCourse.platCourseAndroid.home.course_sections.quiz

import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.model.quiz_model.AnswersModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.home.CourseDetailsUseCase
import com.rowaad.app.usecase.home.CoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class QuizViewModel @Inject constructor(private val coursesUseCase: CoursesUseCase,
                                             private val courseDetailsUseCase: CourseDetailsUseCase) : BaseViewModel() {
    private val _quizzesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val quizzesFlow= _quizzesFlow.asSharedFlow()

    private val _quizFlow = MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val quizFlow= _quizFlow.asSharedFlow()

    private val _answersFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val answersFlow= _answersFlow.asSharedFlow()

    fun sendRequestQuizzes(courseId:Int){
        executeSharedApi(_quizzesFlow){
            courseDetailsUseCase.quizzes(courseId)
                    .onStart { _quizzesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _quizzesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _quizzesFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _quizzesFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _quizzesFlow.emit(NetWorkState.Success(it)) }
        }
    }
    fun sendRequestQuiz(quizId:Int){
        executeSharedApi(_quizFlow){
            courseDetailsUseCase.quiz(quizId)
                    .onStart { _quizFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _quizFlow.emit(NetWorkState.StopLoading) }
                    .catch { _quizFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _quizFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _quizFlow.emit(NetWorkState.Success(it)) }
        }
    }
    fun sendRequestAnswers(quizId:Int,answers:AnswersModel){
        executeSharedApi(_answersFlow){
            courseDetailsUseCase.sendAnswers(quizId, answers)
                    .onStart { _answersFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _answersFlow.emit(NetWorkState.StopLoading) }
                    .catch { _answersFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _answersFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _answersFlow.emit(NetWorkState.Success(it)) }
        }
    }
    fun sendRequestAnswersBg(quizId:Int,answers:AnswersModel){
        GlobalScope.launch {
            courseDetailsUseCase.sendAnswers(quizId, answers)
                    /*.onStart { _quizzesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _quizzesFlow.emit(NetWorkState.StopLoading) }*/
                    .catch { _answersFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _answersFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _answersFlow.emit(NetWorkState.Success(it)) }
        }

    }



 }
