package com.platCourse.platCourseAndroid.auth.sales_policy

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.remote.CurrentRegisterState
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.data.remote.UserRegisterState
import com.rowaad.app.usecase.SplashUseCase
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.menu.MenuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
open class PagesViewModel @Inject constructor(private val menuUseCase: MenuUseCase) : BaseViewModel(){


    private val _termsFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val termsFlow= _termsFlow.asSharedFlow()

    private val _privacyFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val privacyFlow= _privacyFlow.asSharedFlow()

    private val _aboutUsFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val aboutUsFlow= _aboutUsFlow.asSharedFlow()


     fun terms() {
        executeApi(_termsFlow) {
            menuUseCase.terms()
                .onStart { _termsFlow.emit(NetWorkState.Loading) }
                .onCompletion { _termsFlow.emit(NetWorkState.StopLoading) }
                .catch { _termsFlow.emit(NetWorkState.Error(it.handleException())) }
                .collect{ _termsFlow.emit(NetWorkState.Success(it.record)) }
        }
    }
     fun aboutUs() {
        executeApi(_aboutUsFlow) {
            menuUseCase.aboutUs()
                .onStart { _aboutUsFlow.emit(NetWorkState.Loading) }
                .onCompletion { _aboutUsFlow.emit(NetWorkState.StopLoading) }
                .catch { _aboutUsFlow.emit(NetWorkState.Error(it.handleException())) }
                .collect{ _aboutUsFlow.emit(NetWorkState.Success(it.record)) }
        }
    }

     fun privacyPolicy() {
        executeApi(_privacyFlow) {
            menuUseCase.policy()
                .onStart { _privacyFlow.emit(NetWorkState.Loading) }
                .onCompletion { _privacyFlow.emit(NetWorkState.StopLoading) }
                .catch { _privacyFlow.emit(NetWorkState.Error(it.handleException())) }
                .collect{ _privacyFlow.emit(NetWorkState.Success(it.record)) }
        }
    }






}