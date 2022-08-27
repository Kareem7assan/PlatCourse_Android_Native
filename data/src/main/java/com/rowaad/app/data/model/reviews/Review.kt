package com.rowaad.app.data.model.reviews

import com.google.gson.annotations.SerializedName


data class Review(
    @SerializedName("review")
    var rate: Float? = null,
    var description: String? = null,
    var created_at: String? = null,
    var course_title: String? = null,
    var reviewer_name: String? = null,
    var id: Int

)