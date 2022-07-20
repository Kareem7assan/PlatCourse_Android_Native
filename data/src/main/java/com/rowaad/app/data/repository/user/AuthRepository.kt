package com.rowaad.app.data.repository.user

import com.rowaad.app.data.model.BaseResponse
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.settings.SettingsModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Part
import retrofit2.http.Query


interface AuthRepository{
    fun getUser(): Flow<Response<EndPointResponse<RegisterModel>>>
    fun guestToken():Flow<Response<EndPointResponse<BaseResponse>>>
    fun privacy(): Flow<Response<EndPointResponse<SettingsModel>>>
    fun salesPolicy(): Flow<Response<EndPointResponse<SettingsModel>>>
    fun aboutUs(): Flow<Response<EndPointResponse<SettingsModel>>>
    fun login(email:String,password:String,fireBaseToken:String?=null):Flow<Response<EndPointResponse<RegisterModel>>>
    fun logout(fireBaseToken:String?=null):Flow<Response<EndPointResponse<RegisterModel>>>
    fun forgetPhone( email: String): Flow<Response<EndPointResponse<RegisterModel>>>
    fun verify(verificationCode:String, email: String, type:String?="register"): Flow<Response<EndPointResponse<RegisterModel>>>
    fun resend(email:String, type:String?="register"): Flow<Response<EndPointResponse<RegisterModel>>>
    fun resetPassword(password:String, email:String,code:String?="0"): Flow<Response<EndPointResponse<RegisterModel>>>


    fun register(
        name:String, email:String, phoneNumber:String,
        password:String, password_confirmation:String,
        fireBaseToken:String
    ): Flow<Response<EndPointResponse<RegisterModel>>>


    fun updatePassword(
        oldPassword:String,
        password:String
    ): Flow<Response<EndPointResponse<RegisterModel>>>

}