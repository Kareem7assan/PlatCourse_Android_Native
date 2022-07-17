package com.rowaad.app.data.model.notification_model

import com.google.gson.annotations.SerializedName
import com.rowaad.app.data.model.CreatedAt
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.chanel_model.CustomerChat
import com.rowaad.app.data.model.tweets_model.PaginationInfo
import com.rowaad.app.data.model.tweets_model.Tweets

data class NotificationModel(
        val records:List<NotificationItem>,
        val paginationInfo: PaginationInfo?=null,
        val deliveryMen:UserModel?=null
)
data class NotificationItem(
        val id:Int,
        val title:String?=null,
        val body:String?=null,
        val type:String?=null,
        val extra:Extra?=null,
        val seen:Boolean=false,
        val tweet: Tweets?=null,
        val typeId:Int?=null,
        val customer: CustomerChat?=null,
        val createdAt: CreatedAt?=null
)
data class Extra(
        val orderId:String?=null,
        val type:String?=null,
        val status:String?=null,
        val statusColor:String?=null,
        val iconColor:String?=null,
        val statusIcon:String?=null
)