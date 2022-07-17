package com.rowaad.app.data.model.register_model

import com.google.gson.annotations.SerializedName
import com.rowaad.app.data.model.Authorization
import com.rowaad.app.data.model.UserModel

data class RegisterModel(
    @SerializedName("record")
    val userModel: UserModel,
    val authorization: Authorization?=null,
    val resetCode:String?=null,
    val verificationCode:String?=null,
    val code:String?=null,
    val countDeliveryNotifications:Int?=0
)
