package com.platCourse.platCourseAndroid.home.my_courses

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.categories.CategoriesUseCase
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.home.CoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
open class MyCoursesViewModel @Inject constructor(private val coursesUseCase: CoursesUseCase) : BaseViewModel(){


    val isLogin:Boolean=coursesUseCase.isUserLogin()



    private val _myCoursesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val myCoursesFlow= _myCoursesFlow.asSharedFlow()


    fun getMyCourses(page:Int){
            executeApi(_myCoursesFlow){
                coursesUseCase.myCourses(page)
                    .onStart { _myCoursesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _myCoursesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _myCoursesFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _myCoursesFlow.emit(NetWorkState.Success(it)) }

        }

    }

    fun getCoursesBaseCategories(category:Int?=null,subCategory:Int?=null,page:Int){
            executeApi(_myCoursesFlow){
                coursesUseCase.coursesBasedCategories(category, subCategory, page)
                    .onStart { _myCoursesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _myCoursesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _myCoursesFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _myCoursesFlow.emit(NetWorkState.Success(it)) }

        }

    }

}