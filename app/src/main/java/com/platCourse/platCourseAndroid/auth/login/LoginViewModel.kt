package com.platCourse.platCourseAndroid.auth.login

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.auth.LoginUseCase
import com.rowaad.app.usecase.handleException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
open class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : BaseViewModel(){

    private var token: String?=null

    init {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.e("token",it.toString())

            token=it
        }
    }



    private val _isValidMailFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidMailFlow= _isValidMailFlow.asSharedFlow()

    private val _isValidPassFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidPassFlow= _isValidPassFlow.asSharedFlow()

    private val _userFlow= MutableSharedFlow<NetWorkState>()
     val userFlow= _userFlow.asSharedFlow()


    fun isValidMail(mail: String){
        _isValidMailFlow.value=(Validation.IsValid(loginUseCase.isValidEmail(mail)))
    }

    fun isValidPass(pass: String){
            _isValidPassFlow.value=(Validation.IsValid(loginUseCase.isValidPass(pass)))

    }

    fun sendRequestLogin(email:String,pass:String){
        if (loginUseCase.validateLogin(email,pass)){
            executeSharedApi(_userFlow){
                loginUseCase.login(email = email,pass =  pass,token = token)
                    .onStart { _userFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _userFlow.emit(NetWorkState.StopLoading) }
                    .catch { _userFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _userFlow.emit(NetWorkState.Success(it.userModel)) }
            }

        }

    }
    sealed class Validation {
        data class IsValid(val isValid: Boolean):Validation()

        object Idle:Validation()
    }



}