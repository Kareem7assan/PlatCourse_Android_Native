package com.rowaad.utils.extention

import java.util.*

/**
 *  Return one decimal places from double
 */
fun Double.formatDouble(): String {
    return this.toString().format(
        Locale.ENGLISH,
        "%.1f",
        this
    )
}
