package com.rowaad.app.data.model.lessons

import com.google.gson.annotations.SerializedName

data class LessonsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("course") val course: String,
    @SerializedName("description") val description: String,
    @SerializedName("lessons") val lessons: List<LessonsModel>
)

data class LessonsModel(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("lesson_no") val lesson_no: Int,
    @SerializedName("file") val file: String,
    @SerializedName("quizzes") val quizzes: List<String>,
    @SerializedName("videos") val videos: List<VideoModel>,
    @SerializedName("course") val course: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
)

data class VideoModel(
    @SerializedName("id") val id : Int,
    @SerializedName("content_type") val content_type : Int,
    @SerializedName("video_link") val video_link : String,
    @SerializedName("video_file") val video_file : String,
    var videoName: String? = null,
    var file: String? = null
)

data class Section(val id: Int, val title: String? = null, val description: String? = null)
