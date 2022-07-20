package com.platCourse.platCourseAndroid.home.home

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.home.CoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
open class CoursesViewModel @Inject constructor(private val coursesUseCase: CoursesUseCase) : BaseViewModel(){

    private var token: String?=null

    init {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.e("token",it.toString())

            token=it
        }
    }



    private val _coursesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val coursesFlow= _coursesFlow.asSharedFlow()


    fun sendRequestCourses(){
            executeApi(_coursesFlow){
                coursesUseCase.allCourses(1)
                    .onStart { _coursesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _coursesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _coursesFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _coursesFlow.emit(NetWorkState.Success(it)) }

        }

    }

}