package com.rowaad.utils

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object TimeDateUtils {

    fun getCurrentDate(): String {
        val locale = Locale("en")
        val calendar = Calendar.getInstance()
        val c = calendar.time
        val df = SimpleDateFormat("yyyy-M-dd", locale)
        return df.format(c)
    }
     fun extractTimes(open: String?, close: String?):MutableList<Int> {
        val items=mutableListOf<Int>()
        var startHour = open?.split(":")?.get(0)
        val startMin = open?.split(":")?.get(1)?.toInt()
        var endHour = close?.split(":")?.get(0)
        val endMin = close?.split(":")?.get(1)?.toInt()
         if (startHour=="00"){
             startHour="24"
         }
         if (endHour=="00"){
             endHour="24"
         }
        val durationSize = abs(endHour!!.toInt() - startHour!!.toInt())
        for (i in 1..durationSize){
            items.add((startHour.toInt() + i).rem(24))
        }
        return items
    }

    object TimeIntervalHelper {
        fun generateTimeInterval(
            startHour: Int,
            endHour: Int,
            startMin: Int,
            endMin: Int,
            lang: String
        ): ArrayList<String> {
            val intervals= ArrayList<String>()
            var endHour = endHour
            var currentHour: Int
            val calendar = Calendar.getInstance()

            //Log.e("hours",startHour.toString()+","+calendar.get(Calendar.HOUR_OF_DAY))

            currentHour = if (startHour < calendar.get(Calendar.HOUR_OF_DAY)){
                calendar.get(Calendar.HOUR_OF_DAY)
            } else{
                startHour
            }
            calendar[Calendar.HOUR_OF_DAY] = currentHour
            calendar[Calendar.MINUTE] = startMin
            //calendar[Calendar.AM]
            endHour = if (endHour == 12) 0 else endHour
            while (calendar[Calendar.HOUR_OF_DAY] != endHour) intervals.add(
                getInterval(
                    calendar,
                    lang
                )
            )
            calendar[Calendar.MINUTE]=endMin
            intervals.add(getInterval(calendar, lang))
            return intervals
        }



        private fun getInterval(calendar: Calendar, lang: String): String {
            val amPm = if (calendar[Calendar.AM_PM]==0) if (lang=="ar") "ص" else "AM"
            else if (lang=="ar") "م" else "PM"

            val interval = java.lang.String.format(
                Locale(lang),
                "%d:%02d %s",
                if (calendar[Calendar.HOUR] != 0) calendar[Calendar.HOUR] else 12,
                calendar[Calendar.MINUTE], amPm
            )
            calendar.add(Calendar.MINUTE, 60)
            return interval
        }
    }
    fun convertFrom12To24Format(time: String?, lang: String):String?{
        return if (time.isNullOrEmpty()) null
        else {
            val sdf = SimpleDateFormat("hh:mm a", Locale(lang))
            val dateObj = sdf.parse(time)
            val res = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            res.format(dateObj)
        }

    }    fun convertFrom24To12Format(time: String?, lang: String):String?{
        return if (time.isNullOrEmpty()) null
        else {
            val sdf = SimpleDateFormat("HH:mm", Locale(lang))
            val dateObj = sdf.parse(time)
            val res = SimpleDateFormat("hh:mm a", Locale(lang))
            res.format(dateObj)
        }

    }


    fun getTodayName(locale: Locale = Locale.ENGLISH): String {
        val sdf = SimpleDateFormat("EEEE", locale)
        val d = Date()
        val dayOfTheWeek: String = sdf.format(d)
        return dayOfTheWeek
    }

    fun convertMilliToDate(milliSeconds: Long,locale: Locale=Locale.ENGLISH,dateFormat: String?="yyyy-MM-dd"): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat,locale)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

     fun convertDateToMilli(givenDateString:String): Long {
         /*dd-MM-yyyy HH:mm:ss a*/
        val sdf = SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH)
        try {
            val mDate: Date? = sdf.parse(givenDateString)
            val timeInMilliseconds = mDate?.time
            return timeInMilliseconds!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }
         return 0
    }

    fun getUpcomingDate(nextDays: Int): String {
        val locale = Locale("en")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, nextDays)
        val c = calendar.time
        val df = SimpleDateFormat("yyyy-M-dd", locale)
        return df.format(c)
    }

    fun getUpcomingTime(hourRange: Int): String {
        val locale = Locale("en")
        val sdf = SimpleDateFormat("HH:mm:ss", locale)
        return sdf.format(Date().time + hourRange * 3600000)
    }

    fun getCurrentTime(): String {
        val locale = Locale("en")
        val sdf = SimpleDateFormat("hh:mm aa", locale)
        return sdf.format(Date())
    }

    fun getDateTime(): String {
        val locale = Locale("en")
        val calendar = Calendar.getInstance()
        val c = calendar.time
        val df = SimpleDateFormat("yyyy-M-dd_hh:mm:ss", locale)
        return df.format(c)
    }

    fun getConstrains(min: Long, max: Long):CalendarConstraints{
        val constraintsBuilderRange = CalendarConstraints.Builder()
        val dateValidatorMin: CalendarConstraints.DateValidator =
            DateValidatorPointForward.from(min)
        val dateValidatorMax: CalendarConstraints.DateValidator =
            DateValidatorPointBackward.before(max)
        val listValidators = ArrayList<CalendarConstraints.DateValidator>()
        listValidators.add(dateValidatorMin)
        listValidators.add(dateValidatorMax)
        val validators: CalendarConstraints.DateValidator =
            CompositeDateValidator.allOf(listValidators)
        constraintsBuilderRange.setValidator(validators)
        return constraintsBuilderRange.build()
    }

}