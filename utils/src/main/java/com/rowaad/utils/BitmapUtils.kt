package com.rowaad.utils

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URI
import java.net.URL

object BitmapUtils {

    fun getBitmapFromURL(src: String): Bitmap? {

        var url: URL? = null
        try {
            url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

     fun convertUrlToPart(context:Context,selectedUris: List<String>, partName:List<String>):
            List<MultipartBody.Part> {
         val multiParts: ArrayList<MultipartBody.Part> = ArrayList()
         for (i in selectedUris.indices) {
             // 1. Create File using image url (String)


             Glide.with(context).asFile().load(selectedUris[i])
                 .listener(object : RequestListener<File> {
                     override fun onLoadFailed(
                         e: GlideException?,
                         model: Any?,
                         target: Target<File>?,
                         isFirstResource: Boolean
                     ): Boolean {
                         return true
                     }

                     override fun onResourceReady(
                         file: File?,
                         model: Any?,
                         target: Target<File>?,
                         dataSource: DataSource?,
                         isFirstResource: Boolean
                     ): Boolean {
                         val requestFile = file?.asRequestBody("image/*".toMediaTypeOrNull())

                         // 3. Finally, Create MultipartBody using MultipartBody.Part.createFormData
                         val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                             partName[i], file?.name!!.trim(), requestFile!!
                         )
                         Log.e("bodyPart",body.body.toString())
                         multiParts.add(body)
                         return true
                     }

                 })


         }
         return multiParts
     }



@Suppress("DEPRECATION")
fun getRoundedBitmap(view: View): Bitmap? {
view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
view.layout(0, 0, view.measuredWidth, view.measuredHeight)
view.buildDrawingCache()
val returnedBitmap = Bitmap.createBitmap(
view.measuredWidth, view.measuredHeight,
Bitmap.Config.ARGB_8888
)
val canvas = Canvas(returnedBitmap)
canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
val drawable = view.background
drawable?.draw(canvas)
view.draw(canvas)
return returnedBitmap
}

fun modifyImgOrientation(img: Bitmap, path: String): Bitmap {
var postImg = img
try {

val exifInterface = ExifInterface(path)
val orientation =
    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
when (orientation) {
    ExifInterface.ORIENTATION_ROTATE_90 -> postImg = rotate(img, 90f)
    ExifInterface.ORIENTATION_ROTATE_180 -> postImg = rotate(img, 180f)
    ExifInterface.ORIENTATION_ROTATE_270 -> postImg = rotate(img, 270f)
    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> postImg = flip(img, true, false)
    ExifInterface.ORIENTATION_FLIP_VERTICAL -> postImg = flip(img, false, true)
    else -> return img
}
} catch (e: IOException) {
e.printStackTrace()
}

return postImg
}
fun modifyImgOrientation( path: String): Bitmap {
var postImg = BitmapFactory.decodeFile(path)
try {

val exifInterface = ExifInterface(path)
val orientation =
    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
when (orientation) {
    ExifInterface.ORIENTATION_ROTATE_90 -> postImg = rotate(postImg, 90f)
    ExifInterface.ORIENTATION_ROTATE_180 -> postImg = rotate(postImg, 180f)
    ExifInterface.ORIENTATION_ROTATE_270 -> postImg = rotate(postImg, 270f)
    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> postImg = flip(postImg, true, false)
    ExifInterface.ORIENTATION_FLIP_VERTICAL -> postImg = flip(postImg, false, true)
    else -> return postImg
}
} catch (e: IOException) {
e.printStackTrace()
}

return postImg
}

fun rotate(img: Bitmap, degree: Float): Bitmap {
val matrix = Matrix()
matrix.postRotate(degree)

return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
}

fun flip(img: Bitmap, horizental: Boolean, vertical: Boolean): Bitmap {

val matrix = Matrix()
matrix.preScale((if (horizental) -1 else 1).toFloat(), (if (vertical) -1 else 1).toFloat())
return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
}


}