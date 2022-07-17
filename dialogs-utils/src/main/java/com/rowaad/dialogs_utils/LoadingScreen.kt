package com.rowaad.dialogs_utils


import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.WindowManager

object LoadingScreen {


    private var dialog: Dialog?=null


    fun show(activity: Activity, func: (() -> Unit)? = null) {
         dialog = CustomDialogBuilder(activity, R.layout.dialog_loading)
            .cancelable(true)
            ?.gravity(
                Constants.Gravity.GRAVITY_CENTER,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            ?.background(android.R.color.transparent)
            ?.build()
            ?.show()


    }

    fun cancel(){
        dialog?.dismiss()
        dialog?.cancel()
    }

}