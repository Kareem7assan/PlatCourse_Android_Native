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
import com.rowaad.app.data.model.chanel_model.*
import com.rowaad.app.data.model.tweets_model.CreatedAt
import com.rowaad.app.data.model.tweets_model.MessageFcm
import com.rowaad.app.data.utils.Constants_Api
import com.platCourse.platCourseAndroid.R
import com.plat.platCourse.app.Logger
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.platCourse.platCourseAndroid.home.chat.room.ChatFragment
import com.rowaad.utils.extention.fromJson
import com.rowaad.utils.extention.toJson

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
        val data=msg?.fromJson<MessageFcm>()
        when(type){
            "message"->{
                val msgData=Message(
                        id = data?.id?.toInt() ?: 0,channelId = data?.channelId?.toInt() ?:0,
                        message = data?.message,type = data?.type,
                        file = data?.file,createdAt = CreatedAt(humanTime =timeToMillie(data?.createdAt!!.toLong())),
                        createdBy = data.createdBy
                )
                Log.e("fcm",(isAppIsInBackground(applicationContext) ).toString()+","+ChatFragment.isChatOpen+","+isSamePerson(data?.createdBy?.id ?: -1))
                if (isAppIsInBackground(applicationContext).not() && ChatFragment.isChatOpen && isSamePerson(data?.createdBy?.id ?: -1)){
                    Log.e("fcm","forGround")
                    val orderIntent = Intent(Constants_Api.INTENT.MSG_KEY)
                    orderIntent.putExtra(Constants_Api.INTENT.MSG, msgData.toJson())
                    sendBroadcast(orderIntent)
                }
                else{
                    Log.e("fcm","backGround")
                    val room = RoomChat(
                            id = 0,
                            tweet = TweetIds(id = id?.toInt() ?: 0, tweetId = 0),
                            createdAt = CreatedAt(humanTime = data.createdAt),
                            otherCustomer = data.createdBy,
                            file = data.file,
                            lastMessage = body
                    ).toJson()
                    sendMsgNotification(room,data.createdBy?.name,if (data.type=="text") body else getString(R.string.img_sent))
                }
            }
            "tweet"->{
                sendTweetNotification(id,title, body)
            }
            else->{
                sendNormalNotification(title, body)
            }
        }

    }

    private fun sendTweetNotification(tweetId: String?, title: String?, body: String?) {
        handleNotification(createPendingIntentTweet(tweetId),title, body)
    }

    private fun isSamePerson(id: Int): Boolean {
        return id==ChatFragment.otherId
    }

    private fun timeToMillie(timeStamp:Long):String{
        val ago: CharSequence = DateUtils.getRelativeTimeSpanString(timeStamp*1000L, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)
        return ago.toString()
    }

    private fun sendMsgNotification(room: String, title: String?, body: String?) {
        handleNotification(createPendingIntentMsg(room),title, body)
    }
    private fun sendNormalNotification(title: String?, body: String?) {
        handleNotification(createPendingIntentNotification(),title, body)
    }

    private fun handleNotification(pendingIntent: PendingIntent,title: String?,body: String?){
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.splash_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

    }

    private fun createPendingIntentMsg(room: String): PendingIntent {
        return NavDeepLinkBuilder(applicationContext)
                .setComponentName(HomeActivity::class.java)
                .setGraph(R.navigation.home_nav_graph)
                .setDestination(R.id.chatFragment)
                .setArguments(bundleOf("chat"
                        to
                        room
                ))
                .createPendingIntent()
    }
    private fun createPendingIntentTweet(tweetId: String?): PendingIntent {
        return NavDeepLinkBuilder(applicationContext)
                .setComponentName(HomeActivity::class.java)
                .setGraph(R.navigation.home_nav_graph)
                .setDestination(R.id.detailsFragment)
                .setArguments(bundleOf("tweetId"
                        to
                        tweetId
                ))
                .createPendingIntent()
    }

    private fun createPendingIntentNotification(): PendingIntent {
        return NavDeepLinkBuilder(applicationContext)
                .setComponentName(HomeActivity::class.java)
                .setGraph(R.navigation.home_nav_graph)
                .setDestination(R.id.notificationsFragment)
                .createPendingIntent()
    }

    private fun isAppIsInBackground(context: Context): Boolean {
      var isInBackground = true
      val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
      val runningProcesses = am.runningAppProcesses
      for (processInfo in runningProcesses) {
          if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
              for (activeProcess in processInfo.pkgList) {
                  if (activeProcess == context.packageName) {
                      isInBackground = false
                  }
              }
          }
      }

      return isInBackground
  }
}