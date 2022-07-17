package com.platCourse.platCourseAndroid.auth.forget_pass.viewmodel

import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.auth.ForgetUseCase
import com.rowaad.app.usecase.handleException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
open class ForgetViewModel @Inject constructor(private val forgetUseCase: ForgetUseCase) : BaseViewModel(){


    var timeCount=60000L
     var phone:String=""
     var email:String=""
    var code:String=""
    private val _isValidEmailFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidEmailFlow= _isValidEmailFlow.asSharedFlow()

    private val _isValidCodeFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidCode= _isValidCodeFlow.asSharedFlow()

    private val _isValidPassFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidPassFlow= _isValidPassFlow.asSharedFlow()

    private val _isValidNewPassFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidNewPassFlow= _isValidNewPassFlow.asSharedFlow()

    private val _isValidConfirmPassFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidConfirmPassFlow= _isValidConfirmPassFlow.asSharedFlow()

    private val _userFlow= MutableSharedFlow<NetWorkState>()
     val userFlow= _userFlow.asSharedFlow()

    private val _codeFlow= MutableSharedFlow<NetWorkState>()
     val codeFlow= _codeFlow.asSharedFlow()

    private val _resendFlow= MutableSharedFlow<NetWorkState>()
     val resendFlow= _resendFlow.asSharedFlow()

    private val _resetFlow= MutableSharedFlow<NetWorkState>()
     val resetFlow= _resetFlow.asSharedFlow()


    private val _updateFlow= MutableSharedFlow<NetWorkState>()
     val updateFlow= _updateFlow.asSharedFlow()


    fun isValidMail(email: String){
        _isValidEmailFlow.value=(Validation.IsValid(forgetUseCase.isValidMail(email)))
    }

    fun isValidCode(code: String){
        _isValidCodeFlow.value=(Validation.IsValid(forgetUseCase.isValidCode(code)))
    }

    fun isValidPass(pass: String){
            _isValidPassFlow.value=(Validation.IsValid(forgetUseCase.isValidPass(pass)))

    }
    fun isValidNewPass(pass: String){
            _isValidNewPassFlow.value=(Validation.IsValid(forgetUseCase.isValidPass(pass)))

    }

    fun isValidConfirmPass(pass: String,confirmPassword: String){
        _isValidConfirmPassFlow.value=(Validation.IsValid(
            forgetUseCase.isPassMatched(
                pass,
                confirmPassword
            )
        ))

    }

    fun sendRequestMail(email:String){
        if (forgetUseCase.isValidMail(email)){
            this.email=email
            executeSharedApi(_userFlow){
                forgetUseCase.forget(email)
                    .onStart { _userFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _userFlow.emit(NetWorkState.StopLoading) }
                    .catch { _userFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _userFlow.emit(NetWorkState.Success(it)) }
            }
        }
    }

    fun sendRequestCode(code:String,type:String="forgetPassword"){
        if (forgetUseCase.isValidCode(code)){
            executeSharedApi(_codeFlow){
                forgetUseCase.verify(code = code,email = email,type = type)
                    .onStart { _codeFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _codeFlow.emit(NetWorkState.StopLoading) }
                    .catch { _codeFlow.emit(NetWorkState.Error(it)) }
                    .collectLatest { _codeFlow.emit(NetWorkState.Success(it)) }
            }

        }
        else{
            _isValidCodeFlow.value=Validation.IsValid(false)
        }


    }

    fun sendRequestResendCode(type:String="forgetPassword"){
            executeSharedApi(_resendFlow){
                forgetUseCase.resend(email,type)
                    .onStart { _resendFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _resendFlow.emit(NetWorkState.StopLoading) }
                    .catch { _resendFlow.emit(NetWorkState.Error(it)) }
                    .collectLatest { _resendFlow.emit(NetWorkState.Success(it)) }
            }
    }

    fun sendRequestResetCode(password:String,confirmPassword:String,code: String){
        if (forgetUseCase.validateReset(password,confirmPassword)) {
            executeSharedApi(_resetFlow) {
                forgetUseCase.resetPassword(email = email, password = password,code)
                        .onStart { _resetFlow.emit(NetWorkState.Loading) }
                        .onCompletion { _resetFlow.emit(NetWorkState.StopLoading) }
                        .catch { _resetFlow.emit(NetWorkState.Error(it)) }
                        .collectLatest { _resetFlow.emit(NetWorkState.Success(it)) }
            }
        }
    }

    fun sendRequestUpdate(password:String,newPass:String,confirmPassword:String){
        if (forgetUseCase.validateUpdate(password,newPass,confirmPassword)) {
            executeSharedApi(_updateFlow) {
                forgetUseCase.updatePassword(password = password,newPass = newPass)
                        .onStart { _updateFlow.emit(NetWorkState.Loading) }
                        .onCompletion { _updateFlow.emit(NetWorkState.StopLoading) }
                        .catch { _updateFlow.emit(NetWorkState.Error(it)) }
                        .collectLatest { _updateFlow.emit(NetWorkState.Success(it)) }
            }
        }
    }

    sealed class Validation {
        data class IsValid(val isValid: Boolean): Validation()
        object Idle: Validation()
    }



}