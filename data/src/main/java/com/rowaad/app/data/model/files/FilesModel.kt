package com.rowaad.app.data.model.files

data class FilesModel (
        val count:Int?=null,
        val next:String?=null,
        val previous:String?=null,
        val results:List<File>?=null
        )
data class File(
        val id:Int,
        val course:Int,
        val file_name:String?=null,
        val file:String?=null,
        val created_at:String?=null,
        val updated_at:String?=null,
        val downloadable:Boolean?=false
)


