package com.platCourse.platCourseAndroid.fcm

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rowaad.app.data.utils.Constants_Api
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.app.Logger
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.rowaad.utils.extention.fromJson

class MyFirebaseMessagingService : FirebaseMessagingService() {




    override fun onNewToken(token: String) {

    }


    override fun onMessageReceived(message: RemoteMessage) {
        Logger("notification_fcm",message.data.toString())
        val type = message.data["type"]
        val id = message.data["typeId"]
        val title = message.data["title"]
        val body = message.data["body"]
        val msg=message.data["message"]

    }


}