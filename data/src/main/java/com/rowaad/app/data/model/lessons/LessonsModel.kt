package com.rowaad.app.data.model.lessons

data class LessonsModel(val id:Int,
                        val title:String?=null,
                        val description:String?=null,
                        val lesson_no:Int?=null,
                        val file:String?=null,
                        val section:Section?=null,
                        val videos:List<VideoModel>?=null,
                        val course:Int?=null,
                        val created_at:String?=null,
                        val updated_at:String?=null
                        )

data class VideoModel(val id:Int,val content_type:Int=0,val video_link:String?=null,val video_file:String?=null,var videoName:String?=null,var file:String?=null)

data class Section(val id: Int,val title:String?=null,val description:String?=null)
