package com.rowaad.app.usecase.auth

import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.user.AuthRepository
import com.rowaad.app.usecase.Validations
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ForgetUseCase @Inject constructor(private val baseRepository: BaseRepository, private val repository: AuthRepository) {

    //forget
    suspend fun forget(email:String): Flow<String> {
        return repository.forgetPhone(email = email)
            .transformResponseData { emit(it.code ?: "") }
    }
    //verify
    suspend fun verify(email:String,code:String, type:String="forgetPassword"): Flow<RegisterModel> {
        return repository.verify(email = email,verificationCode = code,type = type)
            .transformResponseData<RegisterModel, RegisterModel> { emit(it) }
            .onEach { user-> if (type=="register" || type=="updatePhone") {
                baseRepository.saveUser(user.userModel).also {if (type=="register") baseRepository.saveToken(user.authorization?.accessToken!!) }
            }
            }

    }

    //resend
    suspend fun resend(email:String,type:String="forgetPassword"): Flow<String> {
        return repository.resend(email = email,type = type)
            .transformResponseData { emit(it.code ?: "") }
    }
    //reset
    suspend fun resetPassword(email:String,password:String,code: String): Flow<RegisterModel> {
        return repository.resetPassword(email = email,password = password,code=code)
            .transformResponseData<RegisterModel, RegisterModel> { emit(it) }
            /*.map {user-> baseRepository.saveLogin(true)
                user
            }
            .onEach { user->baseRepository.saveUser(user).also { baseRepository.saveToken(user) } }*/

    }
    //update-password
    suspend fun updatePassword(password:String,newPass: String): Flow<UserModel> {
        return repository.updatePassword(oldPassword = password,password=newPass)
            .transformResponseData<RegisterModel, UserModel> { emit(it.userModel) }
            .map {user-> baseRepository.saveLogin(true)
                user
            }
            .onEach { user->baseRepository.saveUser(user)/*.also { baseRepository.saveToken(user) }*/ }

    }

    fun isValidMail(email: String):Boolean = Validations.isValidMail(email)
    fun isValidPhone(phone: String):Boolean = Validations.isValidPhone(phone)
    fun isValidPass(pass: String):Boolean = Validations.isValidPass(pass)
    fun isValidCode(code: String):Boolean = Validations.isValidCode(code)
    fun isPassMatched(pass: String,confirmPass: String):Boolean = Validations.isPassMatched(pass,confirmPass)
    fun validateReset(pass:String,confirmPass:String):Boolean{
        return when {
            isValidPass(pass).not() -> return false
            isPassMatched(pass, confirmPass).not() -> return false
            else -> true
        }
    }
    fun validateUpdate(pass:String,newPass: String,confirmPass:String):Boolean{
        return when {
            isValidPass(pass).not() -> return false
            isValidPass(newPass).not() -> return false
            isPassMatched(newPass, confirmPass).not() -> return false
            else -> true
        }
    }
}