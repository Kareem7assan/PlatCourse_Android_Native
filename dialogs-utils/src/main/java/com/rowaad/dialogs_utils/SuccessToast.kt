package com.rowaad.dialogs_utils

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast


object SuccessToast {


    fun showToast(context: Activity, title: String){
        val inflater: LayoutInflater = context.layoutInflater
        val layout: View = inflater.inflate(R.layout.toast_layout, /*context.findViewById(R.id.rootView) ,*/null)

        val tvTitle: TextView = layout.findViewById(R.id.tv_dialog_title) as TextView
        tvTitle.text = title

        val toast = Toast(context)
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()

    }


}