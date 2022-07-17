package com.rowaad.utils.extention

import com.google.gson.Gson

inline fun <reified R> String.fromJson(): R {
    return Gson().fromJson(this, R::class.java)
}

inline fun <reified R> R.toJson(): String {
    return Gson().toJson(this, R::class.java)
}
