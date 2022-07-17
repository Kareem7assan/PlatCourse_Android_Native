package com.rowaad.utils

object TimeHelper {
    fun getTime( hours:Int):Int{

        // Initialize a new variable to hold 12 hour format hour value
        val hour_of_12_hour_format: Int

        if (hours > 11) {

            // If the hour is greater than or equal to 12
            // Then we subtract 12 from the hour to make it 12 hour format time

            if (hours==12) {
                hour_of_12_hour_format = 12
            }


            else{
                hour_of_12_hour_format = hours - 12
            }

        }

        else if (hours==0){
            hour_of_12_hour_format = 12
        }

        else {
            hour_of_12_hour_format = hours
        }
        return hour_of_12_hour_format
    }
    fun replaceArabicNumbers( original:String?):String {
        var res=original!!.
        replace("1","١")
                .replace("2","٢")
                .replace("3","٣")
                .replace("4","٤")
                .replace("5","٥")
                .replace("6","٦")
                .replace("7","٧")
                .replace("8","٨")
                .replace("9","٩")
                .replace("0","٠")
                .replace("am","صباحاً")
                .replace("pm","مساءاً")

        return res
    }

}