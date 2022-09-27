package com.rowaad.app.data.repository.course_details

import com.rowaad.app.data.cache.PreferencesGateway
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.WalletModel
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.courses_model.CouponModel
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
import com.rowaad.app.data.remote.UserApi
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.base.BaseRepositoryImpl
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.SHOW_NOTIFICATION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class CourseDetailsRepositoryImp @Inject constructor(
    private val api: UserApi,
    private val db: PreferencesGateway,
    private val baseRepository: BaseRepository,
): CourseDetailsRepository {

    override fun articles(): Flow<Response<List<Article>>> {
        return flow { emit(api.articles()) }
    }

    override fun lessons(courseId: Int): Flow<Response<List<LessonsModel>>> {
        return flow { emit(api.lessons(courseId = courseId)) }
    }

    override fun reviews(courseId: Int): Flow<Response<List<Review>>> {
        return flow { emit(api.reviews(courseId)) }
    }

    override fun addReview(courseId: Int, review: Float, description: String?): Flow<Response<Any>> {
        return flow { emit(api.rate(courseId,review, description)) }
    }

    override fun article(id: Int): Flow<Response<Article>> {
        return flow { emit(api.article(id)) }
    }


    override fun quizzes(courseId: Int): Flow<Response<List<QuizModel>>> {
        return flow {
            emit(api.quizzes(courseId))
        }
    }

    override fun files(courseId: Int,page:Int): Flow<Response<FilesModel>> {
        return flow {
            emit(api.files(courseId,page))
        }
    }

    override fun coupon(courseId: Int, coupon: String): Flow<Response<CouponModel>> {
        return flow {
            emit(api.coupon(courseId.toString(),coupon))
        }
    }

    override fun buyCoupon(courseId: Int, coupon: String): Flow<Response<Any>> {
        return flow {
            emit(api.buyCoupon(courseId.toString(),coupon))
        }
    }

    override fun markAsWatch(lesson_id: Int): Flow<Response<Any>> {
        return flow {
            emit(api.markAsWatch(lesson_id.toString()))
        }
    }

    override fun addComment(discussion_id: Int, comment: String): Flow<Response<Comment>> {
        return flow {
            emit(api.addComment(discussion_id, comment))
        }
    }

    override fun discussions(course_id: String): Flow<Response<List<DiscussionModel>>> {
        return flow {
            emit(api.discussions(course_id))
        }
    }

    override suspend fun addDiscussions(course_id: String, discTitle: String, discDesc: String): Flow<Response<DiscussionModel>> {
        return flow {
            emit(api.discussions(course_id,discTitle, discDesc))
        }
    }


}