package com.platCourse.platCourseAndroid.app
import android.app.Application
import android.provider.Settings
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.rowaad.app.data.utils.Constants_Api.VARIANT_URL
import com.platCourse.platCourseAndroid.BuildConfig
import com.platCourse.platCourseAndroid.fcm.ForceUpdateChecker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        VARIANT_URL = BuildConfig.BASE_URL
        setupFirebaseRemoteConfig()

    }


    private fun setupFirebaseRemoteConfig() {
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        val remoteConfigDefaults = mapOf(
            ForceUpdateChecker.KEY_CURRENT_VERSION to 1,
            ForceUpdateChecker.KEY_UPDATE_REQUIRED to Boolean
        )

        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults)
    }
}