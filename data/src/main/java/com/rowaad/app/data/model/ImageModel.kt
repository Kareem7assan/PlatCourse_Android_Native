package com.rowaad.app.data.model

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import java.io.Serializable

data class ImageModel(
        val path:String?=null,
        val part:MultipartBody.Part?=null
):Serializable