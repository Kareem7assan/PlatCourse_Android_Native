package com.rowaad.app.data.model.announcement

data class AnnouncementModel(
    val id:Int,
    val title:String?=null,
    val description:String?=null,
    val link:String?=null,
    val created_at:String?=null,
)