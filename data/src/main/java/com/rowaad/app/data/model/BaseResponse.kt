package com.rowaad.app.data.model

data class BaseResponse(val authorization:Authorization?=null)

data class Authorization(val type: String?=null,val accessToken:String?=null)

