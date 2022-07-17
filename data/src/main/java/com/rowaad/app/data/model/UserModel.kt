package com.rowaad.app.data.model

import com.google.gson.annotations.SerializedName
import com.rowaad.app.data.model.orders.OrderItem
import com.rowaad.app.data.model.reasons_model.*


data class UserModel(
        val totalTweets: Int? = 0,
        val header: String? = null,
        val image: String? = null,
        val birthDate: String? = null,
        val verificationCode: String? = null,
        val approved: String? = "pending",
        @SerializedName("status")
        val isActive: Boolean? = false,
        val isVerified: Boolean? = false,
        val published: Boolean? = false,
        val isFollowed: Boolean? = false,
        val isFollower: Boolean? = false,
        val bio: String? = null,
        val username: String? = null,
        val phoneNumber: String? = null,
        val email: String? = null,
        val name: String? = null,
        val id: Int? = null,
        val newVerificationCode: String? = null,
        val newPhoneNumber: String? = null,
        val totalFollowers: String? = null,
        val totalFollowing: String? = null,
        val accessToken:String?=null
)



data class CreatedAt(
        val humanTime: String? = null,
        val format: String? = null,
        val text: String? = null,
        val timestamp: Long? = null
)



data class UserLocation(
    val type:String?=null,
    val address:String?=null,
    val coordinates:List<Double>?=null
)
