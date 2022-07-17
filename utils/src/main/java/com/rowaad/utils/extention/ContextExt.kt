package com.rowaad.utils.extention

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.rowaad.app.data.utils.Constants_Api
import com.rowaad.utils.R
import com.rowaad.utils.TimeHelper
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

//<editor-fold desc="show toast">
/**
 * show toast for Context (from string.xml).
 */
fun Context?.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, textId, duration).show() }

/**
 * show string toast for Context.
 */
fun Context?.toast(textId: String, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, textId, duration).show() }

//</editor-fold>


//<editor-fold desc="get app configurations">
fun Context?.getAppLocale(): String {
    return if (Build.VERSION.SDK_INT >= 24) {
        this?.resources?.configuration?.locales?.get(0)?.language ?: "ar"
    } else {
        this?.resources?.configuration?.locale?.language ?: "ar"
    }
}

fun Context.getAppVersion(): String {
    var result = ""
    try {
        result = packageManager
            .getPackageInfo(packageName, 0).versionName
        result = result.replace("[a-zA-Z]|-".toRegex(), "")
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e.localizedMessage)
    }
    return result
}
//</editor-fold>

fun Context.setTime12Format(open: String?):String {
    var hours:String
    var am=getString(R.string.am)
    var mins:String
    if (open!=null && open.isNotEmpty()){
        if (open.contains(":")){
            hours=open.split(":")[0]
            mins=open.split(":")[1]
            if (hours.toInt()>=12) am=getString(R.string.pm)

            return "${TimeHelper.getTime(hours.toInt())}:$mins:$am"
        }

    }
    return ""
}
//<editor-fold desc="load files">
fun Context.getImageFileFromImageView(imageView: AppCompatImageView): File {
    var file: File? = null
    try {
        val bitmapDrawable =
            imageView?.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        file = File(this.cacheDir, Date().time.toString() + "file.PNG")
        val createNewFile = file.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bitmapData = bos.toByteArray()
        val fos = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
    } catch (e: Exception) {
        Timber.e(e.localizedMessage)
    }
    return file!!
}

fun Context.loadFileFromPath(path: String): File? {
    var file: File? = null
    try {
        file = File(path)
    } catch (e: Exception) {
        Timber.e(e.localizedMessage)
    }
    return file
}
//</editor-fold>


//<editor-fold desc="check gps is enabled">
fun Context.isGpsEnabled():Boolean{
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

}
//</editor-fold>


//<editor-fold desc="start activity">
/**
 * startActivity for Context.
 */
inline fun <reified T : Activity> Context?.startActivity() =
    this?.startActivity(Intent(this, T::class.java))

/**
 * startActivity with Animation for Context.
 */
inline fun <reified T : Activity> Context.startActivityWithAnimation(
    enterResId: Int = R.anim.slide_in_right,
    exitResId: Int = R.anim.slide_out_left
) {
    val intent = Intent(this, T::class.java)
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    ContextCompat.startActivity(this, intent, bundle)
}

/**
 * startActivity with extra Animation for Context.
 */
inline fun <reified T : Activity> Context.startActivityWithAnimation(
    enterResId: Int = R.anim.slide_in_right,
    exitResId: Int = R.anim.slide_out_left,
    extra: Bundle
) {
    val intent = Intent(this, T::class.java)
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    intent.putExtra(Constants_Api.INTENT_EXTRA, extra)
    ContextCompat.startActivity(this, intent, bundle)
}

/**
 * startActivity with Animation for Context.
 * finish all stack
 * reversed animation
 */
inline fun <reified T : Activity> Context.startActivityWithReversedAnimationFinishAllStack(
    enterResId: Int = R.anim.slide_in_left,
    exitResId: Int = R.anim.slide_out_right
) {
    val intent = Intent(this, T::class.java)
    intent.flags =
        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    ContextCompat.startActivity(this, intent, bundle)
}

/**
 * startActivity with Animation for Context.
 * finish all stack
 * reversed animation
 */
inline fun <reified T : Activity> Context.startActivityWithAnimationFinishAllStack(
    formLogout:Boolean=false,
            enterResId: Int = R.anim.slide_in_right,
    exitResId: Int = R.anim.slide_out_left
) {
    val intent = Intent(this, T::class.java)

    intent.flags =
        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    intent.putExtra(Constants_Api.INTENT.LOGOUT,formLogout)
    ContextCompat.startActivity(this, intent, bundle)
}
inline fun <reified T : Activity> Context.startActivityWithAnimation(
    formLogout:Boolean=false,
            enterResId: Int = R.anim.slide_in_right,
    exitResId: Int = R.anim.slide_out_left
) {
    val intent = Intent(this, T::class.java)

    //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    intent.putExtra(Constants_Api.INTENT.LOGOUT,formLogout)
    ContextCompat.startActivity(this, intent, bundle)
}
//</editor-fold>


fun Context.isOnline(){
    fun isOnline(): Boolean {
        var result = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }
}



