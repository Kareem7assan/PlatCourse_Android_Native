package com.rowaad.app.usecase.register

import android.util.Log
import com.rowaad.app.data.model.ImageDoc
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.user.AuthRepository
import com.rowaad.app.usecase.Validations
import com.rowaad.app.usecase.transformResponse
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val baseRepository: BaseRepository, private val repository: AuthRepository) {



    fun getUser(): UserModel? {
        return baseRepository.loadUser()
    }

    fun isValidFName(name: String): Boolean = Validations.isValidName(name)
    fun isValidNameNormal(name: String): Boolean = Validations.isValidNormal(name)
    fun isValidUserName(name: String): Boolean = Validations.isValidUserName(name)
    fun isValidLName(name: String): Boolean = Validations.isValidName(name)
    fun isValidPhone(phone: String): Boolean = Validations.isValidPhone(phone)
    fun isValidMail(mail: String): Boolean = Validations.isValidMail(mail)
    fun isValidId(id: String): Boolean = Validations.isValidId(id)
    fun isValidBirthDate(birthDate: String? = null): Boolean = Validations.isValidBirthDate(birthDate)
    fun isValidNationality(national: Int? = null): Boolean = Validations.isValidNational(national)
    fun isValidLocation(location: String? = null): Boolean = Validations.isValidLocation(location)
    fun isValidPass(pass: String): Boolean = Validations.isValidPass(pass)
    fun isPassMatched(pass: String, confirmPass: String): Boolean = Validations.isPassMatched(pass, confirmPass)

    fun validateRegister(username:String,
                         email:String, password:String,
                         name:String, phoneNumber:String,
                         country:String, city:String,
                         confirmPass: String): Boolean {
        return when {
            isValidUserName(username).not() -> return false
            isValidFName(name).not() -> return false
            isValidNameNormal(country).not() -> return false
            isValidNameNormal(city).not() -> return false
            isValidPhone(phoneNumber).not() -> return false
            isValidMail(email).not() -> return false
            isValidPass(password).not() -> return false
            isPassMatched(password, confirmPass).not() -> return false

            else -> true
        }
    }

    fun validateUser(imgPart: MultipartBody.Part? = null,
                     imgUrl: String? = null,
                     fName: String, lName: String,
                     phone: String, mail: String, id: String, birthDate: String?, national: Int?,
                     location: String?): Boolean {
        Log.e("isValidUser", (imgPart == null && imgUrl.isNullOrBlank()).toString() + "," + isValidFName(fName).not() + "," + isValidLName(lName).not()
                + "," + isValidPhone(phone).not() + "," + isValidMail(mail).not() + "," + isValidId(id).not() + "," + isValidBirthDate(birthDate).not()
                + "," + isValidNationality(national).not() + "," + isValidLocation(location).not())
        return when {
            (imgPart == null && imgUrl.isNullOrBlank()) -> return false
            isValidFName(fName).not() -> return false
            isValidLName(lName).not() -> return false
            isValidPhone(phone).not() -> return false
            isValidMail(mail).not() -> return false
            isValidId(id).not() -> return false
            isValidBirthDate(birthDate).not() -> return false
            isValidNationality(national).not() -> return false
            isValidLocation(location).not() -> return false
            else -> true
        }
    }


    fun sendRequestRegister(username:String,
                            email:String, password:String,
                            name:String, phoneNumber:String,
                            country:String, city:String,
                            fireBaseToken:String,
                            role:String?,
                            cv:MultipartBody.Part?,
    ): Flow<RegisterModel> {
        return repository.register(name = name,username = username,phoneNumber = phoneNumber,email = email,password = password,
        country = country,city = city,fireBaseToken = fireBaseToken,role = role,cv = cv
                )
            .transformResponse <RegisterModel,RegisterModel>{
            emit(it)
        }/*.onEach {resp->
                baseRepository.saveUser(resp.student).also { baseRepository.saveToken(resp.access_token!!) }.also { baseRepository.saveLogin(true) }
            }*/
    }

  /*  fun sendUpdateUser(imgPart: MultipartBody.Part? = null, fName: String, lName: String,
                       phone: String, mail: String, id: String, birthDate: String?, national: Int?, lat: String, lng: String,
                       location: String?, type: String? = null): Flow<Pair<String?, UserModel?>> {
        return repository.updateUser(firstName = fName, lastName = lName, phoneNumber = phone, email = mail, idNumber = id, birthDate = birthDate
                ?: "", nationality = national.toString(), lat = lat, lng = lng, address = location!!, img = imgPart, type = type
        ).transformResponseData<RegisterModel, Pair<String?, UserModel?>> {
            emit(Pair(it.resetCode, it.userModel))
        }.onEach { resp ->
            baseRepository.saveUser(resp.second!!).also { baseRepository.saveToken(resp.second!!) }
        }
    }*/






}





