package com.rowaad.dialogs_utils

import android.app.Activity
import android.view.*
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import kotlin.system.exitProcess

object MaintenanceDialog {

/*

    fun show(activity: Activity) {
        val dialog = CustomDialogBuilder(activity, R.layout.maintenance_dialog)
            .cancelable(false)
            ?.gravity(Constants.Gravity.GRAVITY_CENTER, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT)
            ?.background(android.R.color.transparent)
            ?.build()
            ?.show()
    }
*/

    fun show(activity: Activity, retryAction: (() -> Unit)?=null) {
        val dialog = CustomDialogBuilder(activity, R.layout.maintenance_dialog,true)
            .cancelable(false)
            ?.gravity(
                Constants.Gravity.GRAVITY_CENTER,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            ?.build()
            ?.show()
        dialog?.findViewById<Button>(R.id.btn_retry)?.setOnClickListener {
            retryAction?.invoke().also { dialog.dismiss() }
        }
        /*dialog?.findViewById<AppCompatTextView>(R.id.btn_close_app)?.setOnClickListener {
            exitProcess(0);
        }*/
    }
}