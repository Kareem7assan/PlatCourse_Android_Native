package com.rowaad.utils.extention


import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.RadioButton


fun RadioButton.setCircleColor(color: Int) {
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked) // checked
        ), intArrayOf(
            Color.GRAY, // unchecked color
            color // checked color
        )
    )
}
