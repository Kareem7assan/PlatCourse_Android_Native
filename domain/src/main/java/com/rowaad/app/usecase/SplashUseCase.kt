package com.rowaad.app.usecase

import com.rowaad.app.data.model.BaseResponse
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.settings.SettingsModel
import com.rowaad.app.data.remote.UserRegisterState
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.user.AuthRepository
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject


class SplashUseCase @Inject constructor(private val baseRepository: BaseRepository, private val repository: AuthRepository) {

    //check user registration state
    val isLogin : Boolean = baseRepository.isLogin()
    val hasToken:Boolean=baseRepository.hasSavedToken()



    fun saveFirebaseToken(token:String?){
        baseRepository.deviceId=token ?: ""
    }

    //get user token
    suspend fun getGuestToken() : Flow<String> {
         return repository.guestToken()
                         .transformResponseData<BaseResponse,String> { emit(it.authorization?.accessToken ?: "") }
                         .onEach {token->
                             baseRepository.saveToken(token)

                         }
    }




}



