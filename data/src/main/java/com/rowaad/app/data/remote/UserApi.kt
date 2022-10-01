package com.rowaad.app.data.remote

import com.rowaad.app.data.model.*
import com.rowaad.app.data.model.announcement.AnnouncementModel
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.app.data.model.categories_model.CategoriesModel

import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.courses_model.CouponModel
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.app.data.model.discussions_model.Comment
import com.rowaad.app.data.model.discussions_model.DiscussionModel
import com.rowaad.app.data.model.files.FilesModel
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.lessons.LessonsResponse
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.quiz_model.QuizModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.reviews.Review
import com.rowaad.app.data.model.reviews.ReviewsModel
import com.rowaad.app.data.model.settings.SettingsModel
import com.rowaad.app.data.model.teacher_model.TeacherModel
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.TOKEN
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @FormUrlEncoded
 @POST("auth/login")
    suspend fun login(
            @Field("email") email:String,
            @Field("password") password:String,
            @Field("device[token]") fireBaseToken:String?=null,
            @Field("device[type]") type:String="Android"
 ): Response<RegisterModel>

    @POST("login/guests")
    suspend fun guestToken(): Response<EndPointResponse<BaseResponse>>

  @GET("pages/privacy-policy")
    suspend fun privacy(): Response<EndPointResponse<SettingsModel>>

  @GET("pages/sale-policy")
    suspend fun salesPolicy(): Response<EndPointResponse<SettingsModel>>


  @GET("pages/about-us")
    suspend fun aboutUs(): Response<EndPointResponse<SettingsModel>>


    @FormUrlEncoded
    @POST("auth/signup")
    suspend fun register(
         @Field("username") username:String,
         @Field("email") email:String,
         @Field("password") password:String,
         @Field("name") name:String,
         @Field("phone_number") phoneNumber:String,
         @Field("country") country:String,
         @Field("city") city:String,
         @Field("role") role:String?="student",
         @Field("device[token]") fireBaseToken:String,
         @Field("device[type]") type: String?="Android"
     ): Response<RegisterModel>

    @FormUrlEncoded
    @POST("auth/forget-password")
    suspend fun forgetPassword(
        @Field("email") email:String
    ): Response<RegisterModel>

    @FormUrlEncoded
    @POST("auth/get-password-token")
    suspend fun verify(
        @Field("otp") verificationCode:String,
        @Field("email") email:String,
        @Field("type") type:String?="register"
    ): Response<RegisterModel>

    @FormUrlEncoded
    @POST("forget-password")
    suspend fun resend(
            @Field("email") email:String,
            @Field("type") type:String?="register"
    ): Response<RegisterModel>

    @FormUrlEncoded
    @POST("auth/set-new-password")
    suspend fun resetPassword(
        @Field("new_password") newPassword:String,
        @Field("confirm_password") confirmPassword:String
    ): Response<RegisterModel>

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

   @GET("courses/pending_courses")
   suspend fun pendingCourses(
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
    ): Response<NotificationModel>


    @GET("auth/notifications/read_all")
    suspend fun readAllNotification(): Response<Any>


    @FormUrlEncoded
    @POST("auth/notifications/read")
    suspend fun readNotification(
            @Field("notification_ids")  notificationIds:List<Int>
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
    @PUT("auth/student-profile/{id}")
    suspend fun updateProfile(
        @Path("id") id:String,
        @Part image: MultipartBody.Part
     ): Response<UserModel>

    @PUT("auth/student-profile/{id}")
    /*@Headers(
            "Content-Type: multipart/form-data",
            "Accept: application/json"
    )*/
    suspend fun updateProfile(
        @Path("id") id:String,
        @Body image: RequestBody
     ): Response<UserModel>


    @Multipart
    @PUT("auth/student-profile/{id}")
    suspend fun updateProfile(
        @Path("id") id:String,
        @Part("profile_image") name:String?="profile_image",
        @PartMap body: HashMap<String,RequestBody>

    ): Response<UserModel>

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

     ): Response<RegisterModel>

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



    @GET("lessons/lessons_with_sections")
    suspend fun lessons(
        @Query("course_id") courseId:Int
    ): Response<List<LessonsResponse>>

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



 @GET("quizzes")
    suspend fun quizzes(
     @Query("course_id") courseId:Int
 ): Response<List<QuizModel>>

 @GET("courses/{course_id}/files")
    suspend fun files(
         @Path("course_id") courseId:Int,
         @Query("page") page:Int?=1
 ): Response<FilesModel>

    @FormUrlEncoded
    @POST("discussions/{discussion_id}/add_comment")
    suspend fun addComment(
         @Path("discussion_id") discussion_id:Int,
         @Field("comment") comment:String
    ): Response<Comment>

    @GET("discussions")
    suspend fun discussions(
         @Query("course_id") course_id:String
    ): Response<List<DiscussionModel>>

    @FormUrlEncoded
    @POST("discussions")
    suspend fun discussions(
            @Field("course_id") course_id:String,
            @Field("title") discTitle:String,
            @Field("post") discDesc:String,
    ): Response<DiscussionModel>

    @FormUrlEncoded
    @POST("courses/{course_id}/coupon_inquiry")
    suspend fun coupon(
            @Path("course_id") course_id:String,
            @Field("coupon") coupon:String
    ): Response<CouponModel>


    @PUT("lessons/{lesson_id}")
    suspend fun markAsWatch(
            @Path("lesson_id") lesson_id:String
    ): Response<Any>

    @FormUrlEncoded
    @POST("courses/{course_id}/buy_course")
    suspend fun buyCoupon(
            @Path("course_id") course_id:String,
            @Field("payment_code") payment_code:String
    ): Response<Any>



    @FormUrlEncoded
    @POST("courses/{course_id}/contact_teacher")
    suspend fun contactTeacher(
            @Path("course_id") courseId:Int,
            @Field("message") message:String
    ): Response<Any>


    @GET("auth/teacher-profile/{owner_id}")
    suspend fun teacher(
            @Path("owner_id") owner_id:Int
    ): Response<UserModel>



    @POST("courses/{course_id}/buy_course")
    suspend fun buyCourse(
            @Path("course_id") courseId:Int
    ): Response<Any>


    @POST("announcements")
    suspend fun announcements(
         @Query("course_id") courseId: Int
    ): Response<List<AnnouncementModel>>
















}