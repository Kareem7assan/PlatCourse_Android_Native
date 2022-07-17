package com.rowaad.utils.extention

fun Boolean.intValue(): Int {
    return if (this) 1 else 0
}

fun Boolean.toInt(): Int {
    return if (this) 1
    else 0
}