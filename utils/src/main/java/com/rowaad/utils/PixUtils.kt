package com.rowaad.utils

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

import java.io.File


object PixUtils {



    fun convertImgToFilePix(
            parm_name: String?,
            path: String?
    ): MultipartBody.Part? {
         var imgBody: RequestBody?=null
        val file = File(path)
        try {
         imgBody =
            file.asRequestBody("image/jpg".toMediaTypeOrNull())
        //request body for the image content

        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e("File upload", "failed")
        }
        return parm_name?.let { MultipartBody.Part.createFormData(parm_name, file.absolutePath, imgBody!!) }
    }

  fun convertImgToFile(
          parm_name: String?,
          path: String?
  ): MultipartBody {

      val featuredImage = File(path)
          //builder.addFormDataPart(parm_name!!, featured_image.name, RequestBody.create(MultipartBody.FORM, bos.toByteArray(),featured_image))
          //val builder = builder.addFormDataPart("profile_image", featured_image.name, featured_image.asRequestBody(MultipartBody.FORM))


      return MultipartBody.Builder()
              .setType(MultipartBody.FORM)
              .addFormDataPart("profile_image", featuredImage.name, featuredImage.asRequestBody())
              .build()
    }

    fun toRequestBody(value: String?): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value!!)
    }
}