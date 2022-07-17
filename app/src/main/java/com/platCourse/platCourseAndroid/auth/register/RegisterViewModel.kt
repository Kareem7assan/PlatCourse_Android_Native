package com.platCourse.platCourseAndroid.auth.register

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.model.ImageDoc
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.register.RegisterUseCase
import com.rowaad.utils.extention.isNullOrEmptyTrue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
open class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) : BaseViewModel(){


    private var token: String? = null
    var user: UserModel? = registerUseCase.getUser()

    private val _isValidNameFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidNameFlow= _isValidNameFlow.asSharedFlow()


    private val _isValidEmailFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidEmailFlow= _isValidEmailFlow.asSharedFlow()

    private val _isValidPhoneFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidPhoneFlow= _isValidPhoneFlow.asSharedFlow()

    private val _isValidPassFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidPassFlow= _isValidPassFlow.asSharedFlow()


    private val _isValidConfirmPassFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidConfirmPassFlow= _isValidConfirmPassFlow.asSharedFlow()



    private val _isValidTermFlow= MutableSharedFlow<Validation>(replay = 1)
    val isValidTermFlow= _isValidTermFlow.asSharedFlow()


    private val _userFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val userFlow= _userFlow.asSharedFlow()


    private val _userNavigation= MutableSharedFlow<Pair<Boolean,String>>()
     val userNavigation= _userNavigation.asSharedFlow()

init {
    FirebaseMessaging.getInstance().token.addOnSuccessListener {
        token=it
    }
}

    fun isValidPhoneFlow(phone: String){
        _isValidPhoneFlow.value=(Validation.IsValid(registerUseCase.isValidPhone(phone)))
    }
    fun isValidMailFlow(email: String){
        _isValidEmailFlow.value=(Validation.IsValid(registerUseCase.isValidMail(email)))
    }
    fun isValidNameFlow(name: String){
        _isValidNameFlow.value=(Validation.IsValid(registerUseCase.isValidFName(name)))
    }

    fun isValidTermFlow(isValidTerm: Boolean){
        _isValidTermFlow.tryEmit(Validation.IsValid(isValidTerm))
    }
    fun idleTermFlow(){
        _isValidTermFlow.tryEmit(Validation.Idle)
    }
    fun isValidPassFlow(pass: String){
        _isValidPassFlow.value=(Validation.IsValid(registerUseCase.isValidPass(pass)))
    }
    fun isValidConfirmPassFlow(pass: String,confirmPassword: String){
        _isValidConfirmPassFlow.value=(Validation.IsValid(registerUseCase.isPassMatched(pass,confirmPassword)))
    }




    fun sendRequestRegister( name:String,
                         phone: String, mail: String, pass: String, confirmPass: String,hasTerms:Boolean){

        if (registerUseCase.validateRegister(name, phone, mail, pass, confirmPass, hasTerms)){
            executeApi(_userFlow){
                registerUseCase.sendRequestRegister(name, phone, mail, pass, token!!)
                    .onStart { _userFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _userFlow.emit(NetWorkState.StopLoading) }
                    .catch { _userFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest {resp->  _userNavigation.emit(Pair(true,"")) } }
            }

        }





    sealed class Validation {
        data class IsValid(val isValid: Boolean):Validation()
        object Idle:Validation()
    }



}