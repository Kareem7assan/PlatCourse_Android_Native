package com.rowaad.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity


object MapUtils {
    fun navigateToGMap(context: Context, lat: Double, lng: Double) {
        val strUri =
                "http://maps.google.com/maps?q=loc:$lat,$lng"
        val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(strUri))

        intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
        )
        context.startActivity(intent)
    }

    fun makeURL(sourcelat: Double, sourcelog: Double, destlat: Double, destlog: Double): String {
        val urlString = StringBuilder()
        urlString.append("http://maps.googleapis.com/maps/api/directions/json")
        urlString.append("?origin=")// from
        urlString.append(java.lang.Double.toString(sourcelat))
        urlString.append(",")
        urlString.append(java.lang.Double.toString(sourcelog))
        urlString.append("&destination=")// to
        urlString.append(java.lang.Double.toString(destlat))
        urlString.append(",")
        urlString.append(java.lang.Double.toString(destlog))
        urlString.append("&sensor=false&mode=driving&alternatives=true")
        return urlString.toString()
    }


    fun openMapTracking(context: Context, sourceLat:Double, sourceLng:Double,
                        destLat:Double, destLng:Double) {
        val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=$sourceLat,$sourceLng&daddr=$destLat,$destLng"))
        context.startActivity(intent)
    }
}

