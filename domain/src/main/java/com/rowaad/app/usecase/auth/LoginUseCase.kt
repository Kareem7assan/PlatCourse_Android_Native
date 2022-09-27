package com.rowaad.app.usecase.auth

import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.user.AuthRepository
import com.rowaad.app.usecase.Validations
import com.rowaad.app.usecase.transformResponse
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import javax.inject.Inject


class LoginUseCase @Inject constructor(private val baseRepository: BaseRepository, private val repository: AuthRepository) {

    fun validateLogin(email:String,pass:String):Boolean{
        return when {
            isValidEmail(email).not() -> return false
            isValidPass(pass).not() -> return false
            else -> true
        }
    }

    //get user utilities
    suspend fun login(email:String,pass: String,token:String?=null): Flow<RegisterModel> {
        baseRepository.deviceId=token ?: ""
        return repository.login(email = email,password = pass,fireBaseToken = token)
                         .transformResponse <RegisterModel,RegisterModel> { emit(it) }
                        .map {resp-> baseRepository.saveLogin(true)
                            resp
                         }
                        .onEach { resp->baseRepository.saveUser(resp.student)
                            .also { baseRepository.saveToken( resp.access_token!!) } }


    }


     fun isValidEmail(mail: String):Boolean = Validations.isValidMail(mail)
     fun isValidPass(pass: String):Boolean = Validations.isValidPass(pass)




}





