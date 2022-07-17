package com.rowaad.utils.extention

import android.util.Log

fun <E> Collection<E>?.isNullOrEmptyTrue(): Boolean {
    if(this==null){
        Log.e("collection","null")
        return true
    }
    else if (this.isEmpty()){
        Log.e("collection","empty")
        return true
    }
    return false
}