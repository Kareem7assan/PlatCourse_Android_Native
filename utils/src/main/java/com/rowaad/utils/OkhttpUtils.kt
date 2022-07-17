package com.rowaad.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object OkhttpUtils {

    fun getImagePartFromPath(
        paramName: String?,
        path: String?
    ): MultipartBody.Part? {
        val file = File(path)
        val imgBody =
            file.asRequestBody("image/*".toMediaTypeOrNull())
        //request body for the image content
        return paramName?.let { MultipartBody.Part.createFormData(it, file.name, imgBody) }
    }

}