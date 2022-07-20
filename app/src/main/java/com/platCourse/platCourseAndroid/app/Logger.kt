package com.platCourse.platCourseAndroid.app

import android.util.Log
import com.rowaad.app.base.BuildConfig

data class Logger(val key:String?= Logger::class.java.name, val value:String) {
    init {
        if (BuildConfig.DEBUG) Log.e(key,value)
    }
}