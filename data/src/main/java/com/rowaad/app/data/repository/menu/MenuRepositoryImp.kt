package com.rowaad.app.data.repository.menu

import com.rowaad.app.data.cache.PreferencesGateway
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.WalletModel
import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.register_model.RegisterModel
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


    override fun notifications(page: Int): Flow<Response<EndPointResponse<NotificationModel>>> {
        return flow { emit(api.notifications(page)) }
    }

    override fun deleteNotification(id: Int): Flow<Response<EndPointResponse<Any>>> {
        return flow { emit(api.removeNotification(id.toString())) }
    }

    override fun contactUs(): Flow<Response<EndPointResponse<ContactUsModel>>> {
        return flow { emit(api.contactUs()) }
    }

    override fun postContactUs(
        name: String,
        email: String,
        subject: String,
        message: String
    ): Flow<Response<EndPointResponse<Any>>> {
        return flow { emit(api.postContactUs(name, email, subject, message)) }
    }

    override fun profile(userId: Int): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.profile(userId.toString())) }
    }

    override fun myProfile(): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.myProfile()) }
    }

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

    override fun follow(customerId: String): Flow<Response<EndPointResponse<Any>>> {
        return flow { emit(api.follow(customerId)) }
    }

    override fun getNotifications(page: Int): Flow<Response<EndPointResponse<NotificationModel>>> {
        return flow { emit(api.notifications(page)) }
    }

    override var isNotification: Boolean
        get() = db.load(SHOW_NOTIFICATION,true)!!
        set(value) {
            db.save(SHOW_NOTIFICATION,value)
        }

}