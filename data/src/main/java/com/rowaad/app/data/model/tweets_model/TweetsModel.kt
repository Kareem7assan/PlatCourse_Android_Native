package com.rowaad.app.data.model.tweets_model

import com.google.gson.annotations.SerializedName
import com.rowaad.app.data.model.chanel_model.CustomerChat
import com.rowaad.app.data.model.orders.CategoriesItem

data class TweetsModel(
		val records: List<Tweets>? = null,
		val paginationInfo: PaginationInfo? = null
)

data class Customer(
	val image: String? = null,
	val phoneNumber: String? = null,
	val totalTweets: Int? = null,
	val name: String? = null,
	val bio: String? = null,
	val header: String? = null,
	val id: Int? = null,
	val published: Boolean? = null,
	val subscription: Any? = null,
	val email: String? = null,
	val username: String? = null
)

data class PaginationInfo(
	val totalRecords: Int? = null,
	val numberOfPages: Int? = null,
	val itemsPerPage: Int? = null,
	val currentResults: Int? = null,
	val currentPage: Int? = null
)

data class Tweets(
	val comments: List<Comment>? = null,
	val totalRetweets: Int? = null,
	val city: City? = null,
	var isLiked: Boolean? = null,
	var isReported: Boolean? = null,
	val title: String? = null,
	val photos: List<String>? = null,
	val tweetId: Int? = null,
	val whatsappNumber: String? = null,
	val createdAt: CreatedAt? = null,
	val totalComments: Int? = null,
	val price: Float? = null,
	val priceText: String? = null,
	val id: Int? = null,
	val totalLikes: Int? = null,
	val isPremium: Boolean? = null,
	@SerializedName("package")
	val packageSubscription: Any? = null,
	val category: CategoriesItem? = null,
	val region: Region? = null,
	var isRetweeted: Boolean? = null,
	val status: String? = null,
	val customer: CustomerChat? = null
)
data class Comment(val id:Int,val comment:String?=null,val createdAt: CreatedAt?,val createdBy: Customer?)


data class Location(
	val address: String? = null,
	val coordinates: List<Double>? = null,
	val type: String? = null
)

data class Region(
	val createdAt: Any? = null,
	val cities: List<City>? = null,
	val totalTweets: Int? = null,
	val location: Location? = null,
	val id: Int? = null,
	val published: Boolean? = null,
	var isChecked:Boolean=false,
	val title: String? = null,
	val updatedAt: Any? = null
)

data class City(
	val createdAt: Any? = null,
	val totalTweets: Int? = null,
	val location: Location? = null,
	val id: Int? = null,
	val published: Boolean? = null,
	var isChecked: Boolean=false,
	val title: String? = null,
	val region: Any? = null,
	val updatedAt: Any? = null
)





data class CreatedAt(
	val humanTime: String? = null,
	val format: String? = null,
	val text: String? = null,
	val timestamp: Int? = null
)

data class MessageFcm(val createdAt:String?=null,
					  val createdBy: CustomerChat?=null,
					  val id:String?=null,
					  val message:String?=null,
					  val file:List<String>?=null,
					  val type:String?=null,
					  val channelId:String?=null,
)