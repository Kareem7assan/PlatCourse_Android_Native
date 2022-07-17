package com.rowaad.utils.extention

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
