package com.platCourse.platCourseAndroid.fcm

import android.content.Context
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig


class ForceUpdateChecker(
    context: Context,
    onUpdateNeededListener: OnUpdateNeededListener?
) {
    private val onUpdateNeededListener: OnUpdateNeededListener?
    private val context: Context

    interface OnUpdateNeededListener {
        fun onUpdateNeeded(isUpdateRequired:Boolean,currentVersion: Int)
    }

    fun check() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        remoteConfig.fetch(30).addOnCompleteListener {
            if (it.isSuccessful) {
                remoteConfig.activate()
                val isUpdateRequired = remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)
                val currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION).toIntOrNull() ?: 1
                if (isUpdateRequired) {
                    onUpdateNeededListener?.onUpdateNeeded(isUpdateRequired,currentVersion)
                }
            }
        }
    }


    class Builder(context: Context) {
        private val context: Context
        private var onUpdateNeededListener: OnUpdateNeededListener? = null
        fun onUpdateNeeded(onUpdateNeededListener: OnUpdateNeededListener?): Builder {
            this.onUpdateNeededListener = onUpdateNeededListener
            return this
        }

        fun build(): ForceUpdateChecker {
            return ForceUpdateChecker(context, onUpdateNeededListener)
        }

        fun check(): ForceUpdateChecker {
            val forceUpdateChecker = build()
            forceUpdateChecker.check()
            return forceUpdateChecker
        }

        init {
            this.context = context
        }
    }

    companion object {
        const val KEY_UPDATE_REQUIRED = "required_update"
        const val KEY_CURRENT_VERSION = "app_version"

        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    init {
        this.context = context
        this.onUpdateNeededListener = onUpdateNeededListener
    }
}