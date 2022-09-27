package com.rowaad.utils.extention

import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern


fun String.isArabic(): Boolean {
    val pattern = Pattern.compile("^[\u0621-\u064A\u0660-\u0669 ]+$")
    return pattern.matcher(this).matches()
}

fun String?.toCastInt(): Int? {
    return when {
        this.isNullOrEmpty() -> 0
        this.equals("00", true) -> 0
        else -> this.toInt()
    }
}/*2022-08-27T18:15:28.441846+02:00*/
fun String.convertDate():String{
    if (this.equals("الآن",true) || this.equals("now",true)){
        return this
    }
    else {
        val fmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date: Date = fmt.parse(this)

        val fmtOut = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH)
        return fmtOut.format(date)
    }


}
fun String.isEnglish(): Boolean {
    val pattern = Pattern.compile("""^[_A-z0-9]*((\s)*[_A-z0-9])*${'$'}""")
    return pattern.matcher(this).matches()
}

fun String.castFloat(): Float? {
    return if (this.isNullOrEmpty()) 0f
    else this.toFloat()
}

fun String.isImage(): Boolean = when {
    endsWith(".png") -> true
    endsWith(".jpg") -> true
    endsWith(".jpeg") -> true
    endsWith(".svg") -> true
    endsWith(".gif") -> true
    else -> false
}

