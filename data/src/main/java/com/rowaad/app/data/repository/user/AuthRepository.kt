package com.rowaad.app.data.repository.user

import com.rowaad.app.data.model.AppVersionModel
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
    //fun getUser(): Flow<Response<EndPointResponse<RegisterModel>>>
    fun guestToken():Flow<Response<EndPointResponse<BaseResponse>>>
    fun getAppVersion():Flow<Response<AppVersionModel>>
    fun privacy(): Flow<Response<EndPointResponse<SettingsModel>>>
    fun salesPolicy(): Flow<Response<EndPointResponse<SettingsModel>>>
    fun aboutUs(): Flow<Response<EndPointResponse<SettingsModel>>>
    fun login(email:String,password:String,fireBaseToken:String?=null):Flow<Response<RegisterModel>>
    fun logout(fireBaseToken:String?=null):Flow<Response<RegisterModel>>
    fun forgetPhone( email: String): Flow<Response<RegisterModel>>
    fun verify(verificationCode:String, email: String, type:String?="register"): Flow<Response<RegisterModel>>
    fun resend(email:String, type:String?="register"): Flow<Response<RegisterModel>>
    fun resetPassword(password:String, email:String,code:String?="0"): Flow<Response<RegisterModel>>


    fun register(
            username:String,
            email:String,
            password:String,
            name:String,
            phoneNumber:String,
            country:String,
            city:String,
            fireBaseToken:String,
            role:String?="student",
            cv:MultipartBody.Part?=null,
    ): Flow<Response<RegisterModel>>


    fun updatePassword(
        oldPassword:String,
        password:String
    ): Flow<Response<EndPointResponse<RegisterModel>>>

}