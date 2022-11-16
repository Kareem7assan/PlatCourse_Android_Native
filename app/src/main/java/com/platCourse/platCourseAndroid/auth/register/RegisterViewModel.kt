package com.platCourse.platCourseAndroid.auth.register

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.platCourse.platCourseAndroid.auth.login.LoginViewModel
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.model.ImageDoc
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.auth.LoginUseCase
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.register.RegisterUseCase
import com.rowaad.utils.extention.isNullOrEmptyTrue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
open class RegisterViewModel @Inject constructor(
        private val registerUseCase: RegisterUseCase,
        private val loginViewModel: LoginUseCase

) : BaseViewModel(){


    private var token: String? = null
    var user: UserModel? = registerUseCase.getUser()

    private val _isValidNameFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidNameFlow= _isValidNameFlow.asSharedFlow()

    private val _isValidUserNameFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidUserNameFlow= _isValidUserNameFlow.asSharedFlow()

    private val _isValidCountryFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidCountryFlow= _isValidCountryFlow.asSharedFlow()

    private val _isValidCityFlow= MutableStateFlow<Validation>(Validation.Idle)
    val isValidCityFlow= _isValidCityFlow.asSharedFlow()


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


    private val _userNavigation= MutableSharedFlow<Pair<String,String>>()
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
    fun isValidUserNameFlow(name: String){
        _isValidUserNameFlow.value=(Validation.IsValid(registerUseCase.isValidUserName(name)))
    }

    fun isValidCityFlow(name: String){
        _isValidCityFlow.value=(Validation.IsValid(registerUseCase.isValidNameNormal(name)))
    }
    fun isValidCountryFlow(name: String){
        _isValidCountryFlow.value=(Validation.IsValid(registerUseCase.isValidNameNormal(name)))
    }


    fun isValidPassFlow(pass: String){
        _isValidPassFlow.value=(Validation.IsValid(registerUseCase.isValidPass(pass)))
    }
    fun isValidConfirmPassFlow(pass: String,confirmPassword: String){
        _isValidConfirmPassFlow.value=(Validation.IsValid(registerUseCase.isPassMatched(pass,confirmPassword)))
    }




    fun sendRequestRegister(
            username:String,
            email:String, password:String,
            name:String, phoneNumber:String,
            country:String, city:String,
            fireBaseToken:String,confirmPassword: String,
            role:String?,
            cv:MultipartBody.Part?
    ){



        if (registerUseCase.validateRegister(username,email,password,name,phoneNumber,country,city,confirmPassword)){
            executeApi(_userFlow){
                registerUseCase.sendRequestRegister(username=username, email=email, password=password, name=name, phoneNumber=phoneNumber, country=country, city=city, fireBaseToken=fireBaseToken, role=role,cv = cv)
                    .onStart { _userFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _userFlow.emit(NetWorkState.StopLoading).also {
                        _userFlow.emit(NetWorkState.Idle)
                    } }
                    .catch { _userFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _userFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest {resp->
                        //loginViewModel.login(email,password,token)
                        _userNavigation.emit(Pair(email,password))
                    } }
            }

        }





    sealed class Validation {
        data class IsValid(val isValid: Boolean):Validation()
        object Idle:Validation()
    }



}