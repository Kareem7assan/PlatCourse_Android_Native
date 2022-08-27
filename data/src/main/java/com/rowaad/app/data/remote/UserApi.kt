package com.rowaad.app.data.remote

import com.rowaad.app.data.model.*
import com.rowaad.app.data.model.announcement.AnnouncementModel
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.app.data.model.categories_model.CategoriesModel

import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.quiz_model.QuizModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.reviews.Review
import com.rowaad.app.data.model.reviews.ReviewsModel
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

   @GET("courses")
   suspend fun coursesBasedCat(
           @Query("category_id") categoryId:Int?=null,
           @Query("sub_category_id") subCategoryId:Int?=null,
           @Query("page") page:Int?=1
   ): Response<CoursesModel>

   @GET("courses/featured_courses")
   suspend fun featuredCourses(
           @Query("page") page:Int?=1
   ): Response<CoursesModel>

   @GET("courses/owned_courses")
   suspend fun myCourses(
           @Query("page") page:Int?=1
   ): Response<CoursesModel>

   @GET("courses")
   suspend fun searchCourses(
           @Query("title") title:String?=null,
           @Query("page") page:Int?=1,
   ): Response<CoursesModel>

    @GET("auth/notifications")
    suspend fun notifications(
            @Query("page") page:Int=1
    ): Response<List<NotificationItem>>


    @GET("auth/notifications/read_all")
    suspend fun readAllNotification(): Response<Any>


    @POST("auth/notifications/read")
    suspend fun readNotification(
            @Query("notification_ids")  notificationIds:List<Int>
    ): Response<Any>



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

    @GET("auth/student-profile/{id}")
    suspend fun profile(
        @Path("id") id:Int ): Response<UserModel>


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

    @GET("auth/student-profile/{id}")
    suspend fun myProfile(
            @Path("id") id:Int
            ): Response<UserModel>

    @GET("blogs")
    suspend fun articles(): Response<List<Article>>

    @GET("blogs/{id}")
    suspend fun article(
        @Path("id") id:Int
    ): Response<Article>



    @GET("lessons")
    suspend fun lessons(
        @Query("course_id") courseId:Int
    ): Response<List<LessonsModel>>

    @GET("reviews")
    suspend fun reviews(
        @Query("course_id") courseId:Int
    ): Response<List<Review>>


    @FormUrlEncoded
    @POST("reviews")
    suspend fun rate(
            @Field("course_id") courseId:Int,
            @Field("review") review:Float,
            @Field("description") description:String?=null
    ): Response<Any>


 @POST("courses/{course_id}/contact_teacher")
    suspend fun contactTeacher(
     @Path("course_id") courseId:Int,
     @Query("message") message:String
    ): Response<Any>

 @GET("quizzes")
    suspend fun quizzes(
     @Query("course_id") courseId:Int
 ): Response<List<QuizModel>>


    @POST("discussions/{discussion_id}/add_comment")
    suspend fun addComment(
         @Path("discussion_id") discussion_id:Int,
         @Query("comment") comment:String
    ): Response<Any>


    @POST("announcements")
    suspend fun announcements(
         @Query("course_id") courseId: Int
    ): Response<List<AnnouncementModel>>



















}