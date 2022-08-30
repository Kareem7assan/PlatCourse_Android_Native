package com.rowaad.utils.extention

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.rowaad.utils.R


fun Activity.copy(word: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("word", word)
    clipboard.setPrimaryClip(clip)
}

fun FragmentActivity.handleCustomBack(view: Fragment, action: () -> Unit) {
    onBackPressedDispatcher.addCallback(view) {
        action()
    }
}


fun Activity.checkLocationPermissions(requestPermission:Int=100,onLocationGranted:(granted:Boolean)-> Unit){
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ||
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
/*
        ||
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
*/

    ) {


        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION

            ),
            requestPermission
        )
        onLocationGranted(false)

    } else {
        onLocationGranted(true)
    }

}
fun Activity.downloadPdfFile(url: String,title:String?) {


    val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url + ""))
    request.setTitle(title)
    request.setMimeType("application/pdf")
    request.allowScanningByMediaScanner()
    request.setAllowedOverMetered(true)
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "coffee/$title")
    val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
    downloadManager!!.enqueue(request)

}
fun FragmentActivity.checkDownloadPermissions(requestPermission:Int=1101, onDownloadGranted:(granted:Boolean)-> Unit){
    if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
            ) != PackageManager.PERMISSION_GRANTED

    ) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NOTIFICATION_POLICY

                    ),
                    requestPermission
            )
        }
        onDownloadGranted(false)

    } else {
        onDownloadGranted(true)
    }

}

fun FragmentActivity.checkLocationPermissions(requestPermission:Int=100, onLocationGranted:(granted:Boolean)-> Unit){
    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ||
            ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

    ) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION

                ),
                requestPermission
            )
        }
        onLocationGranted(false)

    } else {
        onLocationGranted(true)
    }

}
fun Activity.checkLocationPermissionsAndroid11(requestPermission:Int=100,onLocationGranted:(granted:Boolean)-> Unit){
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ||
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED


    ) {


        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION

            ),
            requestPermission
        )
        onLocationGranted(false)

    } else {
        onLocationGranted(true)
    }

}
fun Activity.checkBackgroundLocationPermissionAPI30(backgroundLocationRequestCode: Int) {
    var dialog :AlertDialog?=null
    if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
         dialog = AlertDialog.Builder(this)
            .setTitle(R.string.background_location_permission_title)
            .setMessage(R.string.background_location_permission_message)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                // this request will take user to Application's Setting page
                dialog.dismiss()
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    backgroundLocationRequestCode
                )


            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()

    }
    else{
        dialog?.dismiss()
    }


}


fun FragmentActivity.replaceFragmentToActivity
        (fragment: Fragment, isSaved: Boolean = true, transitionView: View? = null) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.container, fragment)
    if (isSaved) transaction.addToBackStack(null)
    transitionView?.let {
        transaction.addSharedElement(it, ViewCompat.getTransitionName(it)!!)
    }

    transaction.commit()
}
fun FragmentActivity.showHideFragment
        (fragment: Fragment,currentFragment:Fragment, isSaved: Boolean = true, transitionView: View? = null) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(R.id.container, fragment)
    transaction.hide(currentFragment)
    transaction.show(fragment)
    transaction.addToBackStack(null)
    transaction.commit()
}

fun FragmentActivity.addFragmentToActivity(fragment: Fragment, isSaved: Boolean = true) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(R.id.container, fragment)
    if (isSaved) transaction.addToBackStack(null)
    transaction.commit()
}
