package com.rowaad.app.usecase.menu

import android.util.Log
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.settings.SettingsModel
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.course_details.CourseDetailsRepository
import com.rowaad.app.data.repository.menu.MenuRepository
import com.rowaad.app.data.repository.user.AuthRepository
import com.rowaad.app.usecase.Validations
import com.rowaad.app.usecase.Validations.isValidPhone
import com.rowaad.app.usecase.transformResponse
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


class MenuUseCase @Inject constructor(private val baseRepository: BaseRepository,
                                      private val repository: AuthRepository,
                                      private val menuRepository: MenuRepository,
                                      private val detailsRepository: CourseDetailsRepository
                                      ) {


    val isLogin=baseRepository.isLogin()

    //logout
    suspend fun logout(token: String? = null): Flow<UserModel> {
        return repository.logout(fireBaseToken = baseRepository.deviceId)
                .transformResponse <RegisterModel,UserModel>  { emit(it.student) }
    }



    //policy
    suspend fun policy():Flow<SettingsModel>{
        return repository.salesPolicy()
                .transformResponseData { emit(it) }
    }

    //terms
    suspend fun terms():Flow<SettingsModel>{
        return repository.privacy()
                .transformResponseData { emit(it) }
    }
    //about-us
    suspend fun aboutUs():Flow<SettingsModel>{
        return repository.aboutUs()
                .transformResponseData { emit(it) }
    }

    //notifications
    suspend fun notifications(page:Int): Flow<NotificationModel> {
        return menuRepository.getNotifications(page)
                .transformResponse { emit(it) }
    }


    //read Notifications
    suspend fun readAllNotifications(): Flow<Any> {
        return menuRepository.readAllNotifications()
                .transformResponse { emit(it) }
    }

    //read Notifications
    suspend fun readNotification(id: Int): Flow<Any> {
        return menuRepository.readNotification(listOf(id))
                .transformResponse { emit(it) }
    }



  /*  //notifications
    suspend fun deleteNotification(id:Int): Flow<Any> {
        return menuRepository.deleteNotification(id)
                .transformResponseData { emit(it) }
    }*/


    //contact-us
    suspend fun contactUsContacts(): Flow<SettingsModel> {
        return menuRepository.contactUs().transformResponseData {
            emit(it.settings)
        }
    }

    //articles
    suspend fun articles(): Flow<List<Article>> {
        return detailsRepository.articles().transformResponse {
            emit(it)
        }
    }
    //articles
    suspend fun article(id: Int):Flow<Article> {
        return detailsRepository.article(id).transformResponse {
            emit(it)
        }
    }
  //myProfile
    suspend fun myProfile(): Flow<UserModel> {
        return menuRepository.myProfile().transformResponse<UserModel,UserModel> {
            emit(it)
        }.onEach {
                resp -> baseRepository.saveUser(resp)
        }
    }

    //editProfile
    suspend fun editProfile(
        avatar:MultipartBody.Part?=null,
    ): Flow<UserModel> {
        Log.e("image_avatar",avatar?.body.toString())
        return menuRepository.editProfile(ids = baseRepository?.loadUser()?.id.toString(),image = avatar).transformResponse<UserModel,UserModel>{
            emit(it)
        }.onEach {
              //  resp -> baseRepository.saveUser(resp)
        }
    }

    //editProfile
    suspend fun editProfileBody(
             body: HashMap<String,RequestBody>,
    ): Flow<UserModel> {
        return menuRepository.editProfilePart(id = baseRepository?.loadUser()?.id.toString(),body = body).transformResponse<UserModel,UserModel>{
            emit(it)
        }.onEach {
              //  resp -> baseRepository.saveUser(resp)
        }
    }

    //editProfile body
    suspend fun editProfileBody(
             body: RequestBody,
    ): Flow<UserModel> {
        return menuRepository.editProfileBody(id = baseRepository.loadUser()?.id.toString(),image = body).transformResponse<UserModel,UserModel>{
            emit(it)
        }.onEach {
              //  resp -> baseRepository.saveUser(resp)
        }
    }


    fun validateEditProfile( name: String, phone: String, mail: String): Boolean {
        return when {
            isValidName(name).not() -> return false
            isValidPhone(phone).not() -> return false
            isValidMail(mail).not() -> return false
            else -> true
        }
    }

    fun isValidName(name: String): Boolean = Validations.isValidName(name)
    fun isValidMail(mail: String): Boolean = Validations.isValidMail(mail)
    fun isValidTitle(title: String): Boolean = Validations.isValidTitle(title)
    fun isValidBody(body: String): Boolean = Validations.isValidBody(body)


    fun enableDark(isEnable:Boolean){
        baseRepository.isEnableDark=isEnable
    }
    fun getUser(): UserModel? {
        return baseRepository.loadUser()
    }

    var showNotification: Boolean
        get() =menuRepository.isNotification
        set(value) {
            menuRepository.isNotification=value
        }


    fun clearPrefs(){
        baseRepository.logout()
    }


}






