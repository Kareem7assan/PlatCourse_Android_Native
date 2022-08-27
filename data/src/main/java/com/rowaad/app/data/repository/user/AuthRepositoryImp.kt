package com.rowaad.app.data.repository.user

import com.rowaad.app.data.cache.PreferencesGateway
import com.rowaad.app.data.model.BaseResponse
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.settings.SettingsModel
import com.rowaad.app.data.remote.UserApi
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.base.BaseRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val api: UserApi,
    private val db: PreferencesGateway,
    private val baseRepository: BaseRepository,
): AuthRepository {

    /*override fun getUser(): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.myProfile()) }
    }
*/
    override fun guestToken(): Flow<Response<EndPointResponse<BaseResponse>>> {
        return flow { emit(api.guestToken()) }
    }

    override fun privacy(): Flow<Response<EndPointResponse<SettingsModel>>> {
        return flow { emit(api.privacy()) }
    }

    override fun salesPolicy(): Flow<Response<EndPointResponse<SettingsModel>>> {
        return flow { emit(api.salesPolicy()) }
    }

    override fun aboutUs(): Flow<Response<EndPointResponse<SettingsModel>>> {
        return flow { emit(api.aboutUs()) }
    }


    override fun login(
        email: String,
        password: String,
        fireBaseToken: String?
    ): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.login(email = email,password = password,fireBaseToken = fireBaseToken)) }
    }

    override fun logout(fireBaseToken: String?): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.logout(fireBaseToken)) }
    }

    override fun forgetPhone(email: String): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.forgetPassword(email = email)) }
    }

    override fun verify(
        verificationCode: String,
        email: String,
        type: String?
    ): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.verify(verificationCode = verificationCode,email =  email, type)) }
    }

    override fun resend(
        email: String,
        type: String?
    ): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.resend(email = email, type)) }
    }

    override fun resetPassword(
        password: String,
        email: String,
        code: String?
    ): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.resetPassword(password = password,password_confirmation = password,email = email,code = code)) }
    }


    override fun register(
        name:String, email:String, phoneNumber:String,
        password:String, password_confirmation:String,
        fireBaseToken:String
    ): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.register(name, email, phoneNumber, password, password_confirmation, fireBaseToken)) }
    }



    override fun updatePassword(
        oldPassword: String,
        password: String
    ): Flow<Response<EndPointResponse<RegisterModel>>> {
        return flow { emit(api.updatePassword(oldPassword, password, password)) }
    }
}