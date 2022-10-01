package com.rowaad.app.data.repository.menu

import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.reviews.Review
import com.rowaad.app.data.model.settings.SettingsModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface MenuRepository{


    //fun notifications(page: Int): Flow<Response<EndPointResponse<NotificationModel>>>
    //fun deleteNotification(id: Int): Flow<Response<EndPointResponse<Any>>>

    fun contactUs(): Flow<Response<EndPointResponse<ContactUsModel>>>

     //fun postContactUs(name:String, email:String, subject:String, message:String): Flow<Response<EndPointResponse<Any>>>

     fun profile(userId:Int): Flow<Response<UserModel>>
     fun myProfile():Flow<Response<UserModel>>
     //fun articles():Flow<Response<List<Article>>>
     //fun article(id: Int):Flow<Response<Article>>
     fun editProfile(
         ids: String,
         image: MultipartBody.Part?=null
         ):Flow<Response<UserModel>>
     fun editProfileBody(
         id: String,
         image: RequestBody
         ):Flow<Response<UserModel>>
  fun editProfilePart(
         id: String,
         body: HashMap<String,RequestBody>
         ):Flow<Response<UserModel>>

   /* fun lessons(courseId:Int):Flow<Response<List<LessonsModel>>>
    fun reviews(courseId:Int):Flow<Response<List<Review>>>
    fun addReview(courseId:Int,review:Float,description:String?=null):Flow<Response<Any>>
*/
     fun getNotifications(page: Int) : Flow<Response<NotificationModel>>
     fun readAllNotifications() : Flow<Response<Any>>
     fun readNotification(ids:List<Int>) : Flow<Response<Any>>
    var isNotification:Boolean

}