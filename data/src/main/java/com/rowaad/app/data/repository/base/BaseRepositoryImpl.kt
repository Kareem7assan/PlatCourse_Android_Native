package com.rowaad.app.data.repository.base

import android.os.Build
import com.rowaad.app.data.cache.PreferencesGateway
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.FIREBASE
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.HAS_REGISTER
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.INTERVAL
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.LANG
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.SESSION
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.TOKEN
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.USER
import javax.inject.Inject

open class BaseRepositoryImpl @Inject constructor(
    private val preferences: PreferencesGateway
) : BaseRepository {
    override fun isLogin(): Boolean {
        return preferences.load(SESSION, false) ?: false
    }

    override fun saveLogin(session: Boolean) {
        preferences.save(SESSION, session)
    }

    override fun loadUser(): UserModel? {
        return preferences.load(USER, UserModel())
    }

    override fun loadToken(): String {
        return preferences.load(TOKEN, "") ?: ""
    }

    override fun logout() {
        preferences.clearAll()
    }

    override fun hasSavedToken(): Boolean {
        return preferences.isSaved(TOKEN)
    }

    override fun saveToken(token: String) {
         preferences.save(TOKEN, "Bearer $token")
    }

    override fun saveUser(userData: UserModel) {
        return preferences.save(USER, userData)
    }

    override var updateLocationForSecond: Int
        get() = preferences.load(INTERVAL, 60) ?: 60
        set(value) { preferences.save(INTERVAL, value) }

    override var hasRegister: Boolean
        get() = preferences.load(HAS_REGISTER, true) ?: true
        set(value) { preferences.save(HAS_REGISTER, value) }

    override val mobBrand: String
        get() = Build.BRAND

    override val mobModel: String
        get() = Build.MODEL
    override val mobVersion: String
        get() = Build.VERSION.RELEASE

    override fun clearCache() {
        preferences.clearAll()
    }


    override var deviceId: String
        get() = preferences.load(FIREBASE, "") ?: ""
        set(value) {
            preferences.save(FIREBASE, value)
        }

    override var lang: String
        get() = preferences.load(LANG, "ar") ?: "ar"
        set(value) {
            preferences.save(LANG, value)
        }

}

