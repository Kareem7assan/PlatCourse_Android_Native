package com.rowaad.utils

import android.content.Context
import android.location.Geocoder
import android.util.Log
import timber.log.Timber
import java.util.*

object AddressUtils {

    fun getAddress(context: Context,lat:Double,lng:Double,lang:String="ar"):String{
        var selectedAddress=""
        val geoCoder = Geocoder(context, Locale(lang))
        try {
           val addresses = geoCoder.getFromLocation(
                    lat,
                    lng,
                    1
            )
            selectedAddress=addresses[0].getAddressLine(0)
        }
        catch (ioException: Exception) {
            ioException.printStackTrace()
        }
        return selectedAddress


    }
}