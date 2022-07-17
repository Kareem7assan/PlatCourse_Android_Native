package com.rowaad.dialogs_utils

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.provider.Settings
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton

object WarningPermissionDialog {


    fun show(activity: Activity, func : ( dialog:Dialog) -> Unit) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_connection)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val actvBtn: AppCompatButton = dialog.findViewById(R.id.active_location_btn)

        actvBtn.setOnClickListener {

            func(dialog)

        }
        dialog.show()
    }

}