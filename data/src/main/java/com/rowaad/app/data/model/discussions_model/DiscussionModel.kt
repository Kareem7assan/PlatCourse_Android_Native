package com.rowaad.app.data.model.discussions_model

data class DiscussionModel(val id:Int,
                           val title:String?=null,
                           val post:String?=null,
                           val course_title:String?=null,
                           val owner_name:String?=null,
                           val created_at:String?=null,
                           val course:Int?=null,
                           val comments:List<Comment>?=null
)
data class Comment(
        val id:Int,
        val owner_name:String?=null,
        val comment:String?=null,
        val owner_id:Int?=null,
        val created_at:String?=null,
        val updated_at:String?=null

)
