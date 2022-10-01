package com.rowaad.app.data.model.notification_model

import com.google.gson.annotations.SerializedName
import com.rowaad.app.data.model.CreatedAt
import com.rowaad.app.data.model.UserModel

data class NotificationModel(
    @SerializedName("count")
    val count:Int?,
    @SerializedName("next")
    val next:String?,
    @SerializedName("previous")
    val previous:String?,
    @SerializedName("results")
    val result:List<NotificationItem>
)

data class NotificationItem(
    val id: Int,
    val user_id: Int,
    val notification: Extra? = null,
    var read: Boolean = false,
    @SerializedName("created_at")
    val createdAt: String? = null
)

data class Extra(
    val name: String? = null,
    val body: String? = null,
    val notification_type: Int? = null,
    val course_id: Int? = null,
    val object_id: Int? = null,
    val object_type: String? = null,
    val action_url: String? = null,

)

enum class NotificationType {
    QUIZ,
    LESSON,
    COURSE_FILE,
    ANNOUNCEMENT,
    UNKNOWN
}

fun NotificationItem.getNotificationTypeEnum():NotificationType{
    return when(this.notification?.object_type){
        "Quiz" -> NotificationType.QUIZ
        "Announcement" -> NotificationType.ANNOUNCEMENT
        "Lesson" -> NotificationType.LESSON
        "CourseFile" -> NotificationType.COURSE_FILE
        else ->NotificationType.UNKNOWN
    }
}