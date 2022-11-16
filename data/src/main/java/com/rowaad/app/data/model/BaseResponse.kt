package com.rowaad.app.data.model

data class BaseResponse(val authorization:Authorization?=null)

data class Authorization(val type: String?=null,val accessToken:String?=null)

data class AppVersionModel(val id:Int,val android:String?=null)
