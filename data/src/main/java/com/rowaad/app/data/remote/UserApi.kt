package com.rowaad.app.data.remote

import com.rowaad.app.data.model.*
import com.rowaad.app.data.model.categories_model.CategoriesModel

import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.settings.SettingsModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

 @POST("login")
    suspend fun login(
        @Query("email") email:String,
        @Query("password") password:String,
        @Query("device[token]") fireBaseToken:String?=null,
        @Query("device[type]") type:String="Android"
 ): Response<EndPointResponse<RegisterModel>>

    @POST("login/guests")
    suspend fun guestToken(): Response<EndPointResponse<BaseResponse>>

  @GET("pages/privacy-policy")
    suspend fun privacy(): Response<EndPointResponse<SettingsModel>>

  @GET("pages/sale-policy")
    suspend fun salesPolicy(): Response<EndPointResponse<SettingsModel>>


  @GET("pages/about-us")
    suspend fun aboutUs(): Response<EndPointResponse<SettingsModel>>


    @POST("register")
    suspend fun register(
         @Query("name") name:String,
         @Query("email") email:String,
         @Query("phoneNumber") phoneNumber:String,
         @Query("password") password:String,
         @Query("passwordConfirmation") password_confirmation:String,
         @Query("device[token]") fireBaseToken:String,
         @Query("device[type]") type: String?="Android"
     ): Response<EndPointResponse<RegisterModel>>

    @POST("forget-password")
    suspend fun forgetPassword(
        @Query("email") email:String
    ): Response<EndPointResponse<RegisterModel>>

    @POST("verify-code")
    suspend fun verify(
        @Query("code") verificationCode:String,
        @Query("email") email:String,
        @Query("type") type:String?="register"
    ): Response<EndPointResponse<RegisterModel>>

    @POST("forget-password")
    suspend fun resend(
        @Query("email") email:String,
        @Query("type") type:String?="register"
    ): Response<EndPointResponse<RegisterModel>>

    @POST("reset-password")
    suspend fun resetPassword(
        @Query("password") password:String,
        @Query("passwordConfirmation") password_confirmation:String,
        @Query("email") email:String,
        @Query("code") code:String?="0",
    ): Response<EndPointResponse<RegisterModel>>

   @GET("courses")
   suspend fun newCourses(
           @Query("page") page:Int?=1
   ): Response<CoursesModel>

   @GET("courses/featured_courses")
   suspend fun featuredCourses(
           @Query("page") page:Int?=1
   ): Response<CoursesModel>


   @GET("categories")
   suspend fun categories(
   ): Response<CategoriesModel>


    @GET("contact-us")
    suspend fun contactUs(): Response<EndPointResponse<ContactUsModel>>

    @POST("contact-us/submit")
    suspend fun postContactUs(
        @Query("name") name:String,
        @Query("email") email:String,
        @Query("subject") subject:String,
        @Query("message") message:String
    ): Response<EndPointResponse<Any>>

    @GET("customers/{id}")
    suspend fun profile(
        @Path("id") id:String ): Response<EndPointResponse<RegisterModel>>


    @POST("follow")
    suspend fun follow(
        @Query("customerId") customerId:String
    ): Response<EndPointResponse<Any>>

    @Multipart
    @POST("update-account")
    suspend fun updateProfile(
         @Query("name") name:String,
         @Query("phone") phoneNumber:String,
         @Query("email") email:String,
         @Query("username") username:String?=null,
         @Query("bio") bio:String?=null,
         @Part image: MultipartBody.Part?=null,
         @Part header: MultipartBody.Part?=null,
        @Query("_method") method:String?="PUT"
     ): Response<EndPointResponse<RegisterModel>>

    @POST("update-account")
    suspend fun updateProfile(
        @Query("name") name:String,
        @Query("phone") phoneNumber:String,
        @Query("email") email:String,
        @Query("username") username:String?=null,
        @Query("bio") bio:String?=null,
        @Query("_method") method:String?="PUT"
     ): Response<EndPointResponse<RegisterModel>>

    @POST("logout")
    suspend fun logout(
     @Query("device[token]") fireBaseToken:String?=null,
     @Query("device[type]") type:String="Android",

     ): Response<EndPointResponse<RegisterModel>>

    @POST("update-password")
    suspend fun updatePassword(
        @Query("oldPassword") oldPassword:String,
        @Query("password") password:String,
        @Query("passwordConfirmation") password_confirmation:String,
        @Query("_method") _method:String?="PATCH"
        ): Response<EndPointResponse<RegisterModel>>

    @GET("me")
    suspend fun myProfile(): Response<EndPointResponse<RegisterModel>>
    @GET("notifications")
    suspend fun notifications(
            @Query("page") page:Int=1
    ): Response<EndPointResponse<NotificationModel>>

    @DELETE("notifications/{id}")
    suspend fun removeNotification(
        @Path("id") id:String

    ): Response<EndPointResponse<Any>>

















}