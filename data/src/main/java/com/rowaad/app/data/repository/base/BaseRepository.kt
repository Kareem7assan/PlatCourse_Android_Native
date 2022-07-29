package com.rowaad.app.data.repository.base

import com.rowaad.app.data.model.UserModel


interface BaseRepository{


    fun isLogin(): Boolean

    fun saveLogin(session:Boolean)

    fun loadUser(): UserModel?


    fun logout()

    fun hasSavedToken(): Boolean

    fun loadToken():String
    fun saveToken(token: String)

    fun saveUser(userData: UserModel)
    val mobBrand:String
    var isEnableDark:Boolean
    val mobModel:String
    val mobVersion:String
    var updateLocationForSecond:Int
    var hasRegister:Boolean

    fun clearCache()

    var deviceId: String
    var lang: String

}