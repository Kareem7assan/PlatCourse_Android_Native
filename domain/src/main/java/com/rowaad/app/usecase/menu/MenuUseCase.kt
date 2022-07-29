package com.rowaad.app.usecase.menu

import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.settings.SettingsModel
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.menu.MenuRepository
import com.rowaad.app.data.repository.user.AuthRepository
import com.rowaad.app.usecase.Validations
import com.rowaad.app.usecase.Validations.isValidPhone
import com.rowaad.app.usecase.transformResponse
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject


class MenuUseCase @Inject constructor(private val baseRepository: BaseRepository,
                                      private val repository: AuthRepository,
                                      private val menuRepository: MenuRepository,
                                      ) {


    val isLogin=baseRepository.isLogin()

    //logout
    suspend fun logout(token: String? = null): Flow<UserModel> {
        return repository.logout(fireBaseToken = baseRepository.deviceId)
                .transformResponseData<RegisterModel,UserModel>  { emit(it.userModel) }
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
    suspend fun notifications(page: Int): Flow<List<NotificationItem>> {
        return menuRepository.notifications(page = page)
                .transformResponseData { emit(it.records) }
    }



    //notifications
    suspend fun deleteNotification(id:Int): Flow<Any> {
        return menuRepository.deleteNotification(id)
                .transformResponseData { emit(it) }
    }
    //post contact-us
    suspend fun contactUsPost(name:String,email:String,subject:String,msg:String): Flow<Any> {
        return menuRepository.postContactUs(name = name,email = email,subject = subject,message = msg)
                .transformResponseData { emit(it) }
    }

    //contact-us
    suspend fun contactUsContacts(): Flow<SettingsModel> {
        return menuRepository.contactUs().transformResponseData {
            emit(it.settings)
        }
    }

    //articles
    suspend fun articles(): Flow<List<Article>> {
        return menuRepository.articles().transformResponse {
            emit(it)
        }
    }
    //articles
    suspend fun article(id: Int):Flow<Article> {
        return menuRepository.article(id).transformResponse {
            emit(it)
        }
    }
    //myProfile
    suspend fun myProfile(): Flow<UserModel> {
        return menuRepository.myProfile().transformResponseData<RegisterModel,UserModel>
        {
            emit(it.userModel)
        }.onEach {
                resp -> baseRepository.saveUser(resp)
        }
    }
    //editProfile
    suspend fun editProfile(
        name:String,phone:String,email: String,bio:String?=null,userName:String?=null,
        cover:MultipartBody.Part?=null,avatar:MultipartBody.Part?=null,
    ): Flow<UserModel> {
        return menuRepository.editProfile(name = name,phoneNumber = phone,email = email,
            username = userName, bio = bio,image = avatar,header = cover).transformResponseData<RegisterModel,UserModel>
        {
            emit(it.userModel)
        }.onEach {
                resp -> baseRepository.saveUser(resp)
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






