package com.rowaad.app.data.model.chanel_model

import com.rowaad.app.data.model.tweets_model.CreatedAt
import com.rowaad.app.data.model.tweets_model.PaginationInfo

data class RoomsModel (val records:List<RoomChat>?=null,val paginationInfo: PaginationInfo?=null)
data class MessagesModel (val records:List<Message>?=null,val tweet: TweetIds?,val otherCustomer: CustomerChat?=null)
data class MessageModel(val record:Message?=null)
data class Message(
        val id: Int,
        val channelId: Int,
        val type: String?="text",
        val message: String?=null,
        val file: List<String>?=null,
        val filePath: List<String>?=null,
        val createdAt:CreatedAt?=null,
        val createdBy:CustomerChat?=null
)

data class RoomChat(
        val id:Int,
        val totalUnseen:Int?=0,
        val tweet:TweetIds?=null,
        val createdAt: CreatedAt?=null,
        val otherCustomer: CustomerChat?=null,
        val file: List<String>?=null,
        val lastMessage: String?=null
)
data class TweetIds(
        val id:Int,
        val tweetId:Int,
        val whatsappNumber:String?=null
        )

data class CustomerChat(
        val id:Int,
        val name:String?=null,
        val email:String?=null,
        val phoneNumber:String?=null,
        val image:String?=null,
        val username:String?=null,
        val type:String?="customers"
)