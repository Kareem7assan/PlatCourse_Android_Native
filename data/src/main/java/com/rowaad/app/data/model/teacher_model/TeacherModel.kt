package com.rowaad.app.data.model.teacher_model

data class TeacherModel(
        val name:String?=null,
        val full_name:String?=null,
        val biography:String?=null,
        val active:Boolean?=null,
        val verified:Boolean?=null,
        val accept_terms_and_conditions:Boolean?=null,
        val profile_image:String?=null,
        val phone_number:String?=null,
        val email:String?=null,
        val country:String?=null,
        val city:String?=null,
        val specialization:String?=null
)
