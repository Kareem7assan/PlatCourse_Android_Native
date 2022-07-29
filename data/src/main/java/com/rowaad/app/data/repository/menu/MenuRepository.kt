package com.rowaad.app.data.repository.menu

import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.settings.SettingsModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface MenuRepository{


    fun notifications(page: Int): Flow<Response<EndPointResponse<NotificationModel>>>
    fun deleteNotification(id: Int): Flow<Response<EndPointResponse<Any>>>

    fun contactUs(): Flow<Response<EndPointResponse<ContactUsModel>>>

     fun postContactUs(name:String, email:String, subject:String, message:String): Flow<Response<EndPointResponse<Any>>>

     fun profile(userId:Int): Flow<Response<EndPointResponse<RegisterModel>>>
     fun myProfile():Flow<Response<EndPointResponse<RegisterModel>>>
     fun articles():Flow<Response<List<Article>>>
     fun article(id: Int):Flow<Response<Article>>

     fun editProfile(
         name:String,
         phoneNumber:String,
         email:String,
         username:String?=null,
         bio:String?=null,
         image: MultipartBody.Part?=null,
         header: MultipartBody.Part?=null,
         ):Flow<Response<EndPointResponse<RegisterModel>>>

     fun follow(customerId:String): Flow<Response<EndPointResponse<Any>>>
     fun getNotifications(page: Int) : Flow<Response<EndPointResponse<NotificationModel>>>
    var isNotification:Boolean

}