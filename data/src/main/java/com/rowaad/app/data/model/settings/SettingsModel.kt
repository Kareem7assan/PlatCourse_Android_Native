package com.rowaad.app.data.model.settings

import com.google.gson.annotations.SerializedName
import com.rowaad.app.data.model.UserModel

data class SettingsModel(
    val address:String?=null,
    val email:String?=null,
    val phoneNumber:String?=null,
    val whatsapp:String?=null,
    val lat:String?=null,
    val lng:String?=null,
    val social:Social?=null,
    val maintenance:Boolean?=null,
    val record:Record?=null,
)
data class Record(val content:String?=null)
data class Social(val facebook:String?=null,
                  val twitter:String?=null,
                  val instagram:String?=null,
                  val whatsapp:String?=null,
                  val youtube:String?=null,
                  val snapchat:String?=null,
                  )


