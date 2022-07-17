package com.rowaad.utils.extention

import java.util.regex.Pattern


fun String.isArabic(): Boolean {
    val pattern = Pattern.compile("^[\u0621-\u064A\u0660-\u0669 ]+$")
    return pattern.matcher(this).matches()
}

fun String?.toCastInt(): Int? {
    return when {
        this.isNullOrEmpty() -> 0
        this.equals("00",true) -> 0
        else -> this.toInt()
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

