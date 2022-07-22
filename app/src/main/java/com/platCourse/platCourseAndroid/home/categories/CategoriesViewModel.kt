package com.platCourse.platCourseAndroid.home.categories

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
open class CategoriesViewModel @Inject constructor(private val categoriesUseCase: CategoriesUseCase) : BaseViewModel(){

    private var token: String?=null




    private val _categoriesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val categoriesFlow= _categoriesFlow.asSharedFlow()


    fun getCategories(){
            executeApi(_categoriesFlow){
                categoriesUseCase.categories()
                    .onStart { _categoriesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _categoriesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _categoriesFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _categoriesFlow.emit(NetWorkState.Success(it)) }

        }

    }

}