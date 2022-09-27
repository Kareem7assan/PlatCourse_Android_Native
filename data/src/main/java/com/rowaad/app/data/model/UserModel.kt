package com.rowaad.app.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(
        val country: String? = null,
        val city: String? = null,
        val profile_image: String? = null,
        val birthDate: String? = null,
        val verificationCode: String? = null,
        val approved: String? = "pending",
        @SerializedName("active")
        val isActive: Boolean? = false,
        val isVerified: Boolean? = false,
        val published: Boolean? = false,
        val isFollowed: Boolean? = false,
        val isFollower: Boolean? = false,
        val bio: String? = null,
        val phone_number: String? = null,
        val email: String? = null,
        val name: String? = null,
        val username: String? = null,
        val user: Int? = null,
        val id: Int? = null,
        val courses:MutableList<Int> ?= null,
        val pending_courses:MutableList<Int>?= null,
        val login_times:Int?=null
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
