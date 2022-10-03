package com.rowaad.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.rowaad.utils.extention.toast


object IntentUtils {

    fun callNumberIntent(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun openFacebookIntent(context: Context, uri: String?) {
        val intent: Intent = try {
            context.packageManager.getPackageInfo("com.facebook.katana", 0)
            Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        } catch (e: Exception) {
            Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        }
        context.startActivity(intent)
    }

    fun openTwitterIntent(context: Context, uri: String?) {
        var intent: Intent
        try {
            // get the Twitter app if possible
            context.packageManager.getPackageInfo("com.twitter.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } catch (e: Exception) {
            // no Twitter app, revert to browser
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        }
        context.startActivity(intent)
    }

    fun openUrl(context: Context, url: String?){
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)

        }
        catch (e: Exception) {
            context.toast("الرابط غير صحيح")
        }

}
    fun openUrlWithWaterMark(context: Context, url: String?,userName:String,phone: String){
        val url="https://platcourse.com/pdf_file?username=${userName}&phone_number=${phone}&link=${url}"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)

        }
        catch (e: Exception) {
            context.toast("الرابط غير صحيح")
        }

}

    fun openInstagramIntent(context: Context, uri: String?) {
        val likeIng = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        likeIng.setPackage("com.instagram.android")
        try {
            context.startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
    }

    fun openMapIntent(context: Context, lat: String, lng: String) {
        val uri: String = String.format("geo:$lat,$lng")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    }

    fun openMapWithDirectionIntent(context: Context, lat: String, lng: String) {
        val url =
            "https://www.google.com/maps/dir/?api=1&destination=$lat,$lng&travelmode=driving"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    fun openWhatsappIntent(number: String, context: Context){
        val url = "https://api.whatsapp.com/send?phone=$number"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        try {
            context.startActivity(i)
        }catch (e: Exception){
            context.toast("لم يتم العثور على برنامج واتساب على جهازك")
        }
    }
    fun openSnapAppIntent( context: Context,snapchatId:String){
        try {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "https://snapchat.com/add/$snapchatId"
                )
            )
            intent.setPackage("com.snapchat.android")
            context.startActivity(intent)
        } catch (e: Exception) {
            context.startActivity(Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "https://snapchat.com/add/$snapchatId"
                    )
                )
            )
        }

    }

    fun openEmailIntent(emai: String, context: Context){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$emai"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "App feedback")
        context.startActivity(intent)
    }





}