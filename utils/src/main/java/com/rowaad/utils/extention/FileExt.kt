package com.rowaad.utils.extention

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import com.rowaad.utils.PixUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.util.*

fun Uri.convertMultiPart(name: String): MultipartBody.Part {
    Log.e("path", this.path + ",.." + this)
    val file = File(path)
    val imgBody =
        file.asRequestBody("image/*".toMediaTypeOrNull())
//request body for the image content
    return  MultipartBody.Part.createFormData(name, file.name, imgBody)
}
fun File.convertMultiPart(name: String): MultipartBody.Part {
    Log.e("path", this.path + ",.." + this)
    val imgBody =
        asRequestBody("image/*".toMediaTypeOrNull())
//request body for the image content
    return  MultipartBody.Part.createFormData(name, this.name, imgBody)
}
fun File.convertMultiPartPdf(name: String): MultipartBody.Part {
    Log.e("path", this.path + ",.." + this)
    val imgBody =
        asRequestBody("*/*".toMediaTypeOrNull())
//request body for the image content
    return  MultipartBody.Part.createFormData(name, this.name, imgBody)
}
fun File.convertMultiPartAudio(name: String): MultipartBody.Part {
    Log.e("path", this.path + ",.." + this)
    val imgBody =
        asRequestBody("audio/*".toMediaTypeOrNull())

//request body for the image content
    return  MultipartBody.Part.createFormData(name, this.name, imgBody)
}

fun String.convertToMultiPartAudio(voice: String?): MultipartBody.Part? {
    val file = File(this)
    // MultipartBody.Part is used to send also the actual file name

    return file.convertMultiPartAudio(name = voice!!)
}

fun Uri.convertFile(activity: Activity, name: String, callBack: (MultipartBody.Part) -> Unit){
    val selectedBitmap = getBitmap(activity, this)
    val selectedImgFile = File(
        activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        System.currentTimeMillis().toString() + "_selectedImg.jpg"
    )
    convertBitmapToFile(selectedImgFile, selectedBitmap!!, name){
           callBack.invoke(it)
    }
}
fun Uri.convertFilePdf(activity: Activity, name: String, callBack: (MultipartBody.Part,String) -> Unit){
    val frontPdfPath = getPDFPath(this, activity)
    val file = File(frontPdfPath)
    callBack.invoke(file.convertMultiPartPdf(name),file.path ?: "")
    //PixUtils.convertFileToPart(frontPdfPath)


}
/*
fun getMimeType(context: Context, uri: Uri): String? {

    //Check uri format to avoid null
    val extension: String = if (uri.scheme == ContentResolver.SCHEME_CONTENT) ({
        //If scheme is a content
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
    })!!
    else {
        //If scheme is a File
        //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
        MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
    }
    return extension
}*/
 fun getMimeType(context: Context, uri: Uri): String? {
    return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        val mime = MimeTypeMap.getSingleton()
        mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
    } else {
        MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
    }
}


fun getPDFPath(uri: Uri?, activity: Activity): String? {
    var absolutePath = ""
    try {
        val inputStream = activity.contentResolver.openInputStream(uri!!)
        val pdfInBytes = ByteArray(inputStream!!.available())
        inputStream.read(pdfInBytes)
        //val encodePdf: String = Base64.encodeToString(pdfInBytes, Base64.DEFAULT)
        var offset = 0
        var numRead = 0
        while (offset < pdfInBytes.size && inputStream.read(
                pdfInBytes,
                offset,
                pdfInBytes.size - offset
            ).also {
                numRead = it
            } >= 0
        ) {
            offset += numRead
        }
        var mPath = ""
        mPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            activity.getExternalFilesDir(Environment.DIRECTORY_DCIM)
                .toString() + "/" + Calendar.getInstance().getTime() + ".pdf"
        } else {
            Environment.getExternalStorageDirectory().toString() + "/" + Calendar.getInstance()
                .getTime() + ".pdf"
        }
        val pdfFile = File(mPath)
        val op: OutputStream = FileOutputStream(pdfFile)
        op.write(pdfInBytes)
        absolutePath = pdfFile.path
    } catch (ae: Exception) {
        ae.printStackTrace()
    }
    return absolutePath
}
fun getRecordPath(uri: Uri?, activity: Activity): String? {
    var absolutePath = ""
    try {
        val inputStream = activity.contentResolver.openInputStream(uri!!)
        val pdfInBytes = ByteArray(inputStream!!.available())
        inputStream.read(pdfInBytes)
        //val encodePdf: String = Base64.encodeToString(pdfInBytes, Base64.DEFAULT)
        var offset = 0
        var numRead = 0
        while (offset < pdfInBytes.size && inputStream.read(
                pdfInBytes,
                offset,
                pdfInBytes.size - offset
            ).also {
                numRead = it
            } >= 0
        ) {
            offset += numRead
        }
        var mPath = ""
        mPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            activity.getExternalFilesDir(Environment.DIRECTORY_DCIM)
                .toString() + "/" + Calendar.getInstance().getTime() + ".mp3"
        } else {
            Environment.getExternalStorageDirectory().toString() + "/" + Calendar.getInstance()
                .getTime() + ".mp3"
        }
        val pdfFile = File(mPath)
        val op: OutputStream = FileOutputStream(pdfFile)
        op.write(pdfInBytes)
        absolutePath = pdfFile.path
    } catch (ae: Exception) {
        ae.printStackTrace()
    }
    return absolutePath
}

fun convertBitmapToFile(
    destinationFile: File,
    bitmap: Bitmap,
    name: String,
    callBack: (MultipartBody.Part) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {        //create a file to write bitmap data
        destinationFile.createNewFile()        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
        val bitmapData = bos.toByteArray()        //write the bytes in file
        val fos = FileOutputStream(destinationFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
        callBack.invoke(destinationFile.convertMultiPart(name))
    }


}

fun getBitmap(context: Context, imageUri: Uri): Bitmap? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                context.contentResolver,
                imageUri
            )
        )

    } else {

        context
            .contentResolver
            .openInputStream(imageUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }

    }
}