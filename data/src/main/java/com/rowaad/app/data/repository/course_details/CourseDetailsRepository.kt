package com.rowaad.app.data.repository.course_details

import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.discussions_model.Comment
import com.rowaad.app.data.model.discussions_model.DiscussionModel
import com.rowaad.app.data.model.files.FilesModel
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.quiz_model.QuizModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.reviews.Review
import com.rowaad.app.data.model.settings.SettingsModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface CourseDetailsRepository{

    fun articles():Flow<Response<List<Article>>>
     fun article(id: Int):Flow<Response<Article>>
    fun lessons(courseId:Int):Flow<Response<List<LessonsModel>>>
    fun reviews(courseId:Int):Flow<Response<List<Review>>>
    fun addReview(courseId:Int,review:Float,description:String?=null):Flow<Response<Any>>
    fun quizzes(courseId:Int): Flow<Response<List<QuizModel>>>
    fun files(courseId:Int,page:Int): Flow<Response<FilesModel>>
    fun addComment(
             discussion_id:Int,
             comment:String
    ): Flow<Response<Comment>>

    fun discussions(
            course_id:String
    ): Flow<Response<List<DiscussionModel>>>

    suspend fun addDiscussions(
            course_id:String,
            discTitle:String,
            discDesc:String
    ): Flow<Response<DiscussionModel>>

}