package com.rowaad.app.data.repository.menu

import com.rowaad.app.data.cache.PreferencesGateway
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.WalletModel
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.app.data.model.notification_model.NotificationModel
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

class MenuRepositoryImp @Inject constructor(
    private val api: UserApi,
    private val db: PreferencesGateway,
    private val baseRepository: BaseRepository,
): MenuRepository {



/*
    override fun deleteNotification(id: Int): Flow<Response<EndPointResponse<Any>>> {
        return flow { emit(api.removeNotification(id.toString())) }
    }
*/

    override fun contactUs(): Flow<Response<EndPointResponse<ContactUsModel>>> {
        return flow { emit(api.contactUs()) }
    }

    override fun profile(userId: Int): Flow<Response<UserModel>> {
        return flow { emit(api.profile(userId)) }
    }

    override fun myProfile(): Flow<Response<UserModel>> {
        return flow { emit(api.myProfile(baseRepository.loadUser()?.id ?: 0)) }
    }
/*

    override fun articles(): Flow<Response<List<Article>>> {
        return flow { emit(api.articles()) }
    }
    override fun article(id: Int): Flow<Response<Article>> {
        return flow { emit(api.article(id)) }
    }
*/

    override fun editProfile(
        name: String,
        phoneNumber: String,
        email: String,
        username: String?,
        bio: String?,
        image: MultipartBody.Part?,
        header: MultipartBody.Part?
    ): Flow<Response<EndPointResponse<RegisterModel>>> {
        return if (image==null && header==null) {
            flow {  emit(api.updateProfile(name, phoneNumber, email, username, bio)) }
        } else{
            flow {  emit(api.updateProfile(name, phoneNumber, email, username, bio, image, header)) }
        }
    }

   /* override fun lessons(courseId: Int): Flow<Response<List<LessonsModel>>> {
        return flow { emit(api.lessons(courseId = courseId)) }
    }

    override fun reviews(courseId: Int): Flow<Response<List<Review>>> {
        return flow { emit(api.reviews(courseId)) }
    }

    override fun addReview(courseId: Int, review: Float, description: String?): Flow<Response<Any>> {
        return flow { emit(api.rate(courseId,review, description)) }
    }*/

    override fun getNotifications(page: Int): Flow<Response<List<NotificationItem>>> {
        return flow {
            emit(api.notifications())
        }
    }

    override fun readAllNotifications(): Flow<Response<Any>> {
        return flow {
            emit(api.readAllNotification())
        }
    }

    override fun readNotification(ids: List<Int>): Flow<Response<Any>> {
        return flow {
            emit(api.readNotification(ids))
        }
    }


    override var isNotification: Boolean
        get() = db.load(SHOW_NOTIFICATION,true)!!
        set(value) {
            db.save(SHOW_NOTIFICATION,value)
        }

}