package com.rowaad.app.data.model.lessons

import com.google.gson.annotations.SerializedName
import com.rowaad.app.data.model.quiz_model.QuizItem
import com.rowaad.app.data.model.quiz_model.QuizModel

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
    @SerializedName("file") val file: String?,
    @SerializedName("downloadable") val downloadable: Boolean?,
    @SerializedName("quizzes") val quizzes: List<QuizModel>,
    @SerializedName("videos") val videos: List<VideoModel>,
    @SerializedName("course") val course: Int,
    @SerializedName("can_start") val canStart: Boolean=false,
    @SerializedName("first_unsolved_required_quiz") val firstUnsolvedRequiredQuiz: QuizItem?=null,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    //for UI purpose
    var isExpanded:Boolean=false
)

data class VideoModel(
    @SerializedName("id") val id : Int,
    @SerializedName("content_type") val content_type : Int?,
    @SerializedName("video_link") val video_link : String?,
    @SerializedName("video_file") var video_file : String?,
    @SerializedName("video_id") var video_id : String?,
    @SerializedName("downloadable") var downloadable: Boolean?,
    var videoName: String? = null,
    var file: String? = null,
    var quizzes: List<QuizModel>? = null,
    var externalFileExists:Boolean=false,
)

data class Section(val id: Int, val title: String? = null, val description: String? = null)
