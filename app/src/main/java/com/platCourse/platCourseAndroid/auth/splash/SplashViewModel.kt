package com.platCourse.platCourseAndroid.auth.splash

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.remote.CurrentRegisterState
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.data.remote.UserRegisterState
import com.rowaad.app.usecase.SplashUseCase
import com.rowaad.app.usecase.handleException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
open class SplashViewModel @Inject constructor(private val splashUseCase: SplashUseCase) : BaseViewModel(){

    private val _navigationFlow= MutableSharedFlow<SplashNavigation>(
            replay = 1,
            extraBufferCapacity = 0,
            onBufferOverflow =  BufferOverflow.SUSPEND

    )
    val navigationFlow= _navigationFlow.asSharedFlow()

    private val _guestTokenFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)

    init {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            splashUseCase.saveFirebaseToken(it)

        }
    }


     fun handleNavigation(){
         when{
             splashUseCase.isLogin ->_navigationFlow.tryEmit(SplashNavigation.NavigateToHome)
             splashUseCase.isLogin.not() && splashUseCase.hasToken.not() -> sendRequestToken()
             splashUseCase.isLogin.not() && splashUseCase.hasToken ->_navigationFlow.tryEmit(SplashNavigation.NavigateToHome)
         }
    }

    private fun sendRequestToken() {
        executeApi(_guestTokenFlow) {
            splashUseCase.getGuestToken()
                .catch { _guestTokenFlow.emit(NetWorkState.Error(it)) }
                .collect{ _navigationFlow.emit(SplashNavigation.NavigateToHome) }
        }
    }


    sealed class SplashNavigation {
        object  NavigateToLogin : SplashNavigation()
        object  NavigateToHome : SplashNavigation()
    }




}