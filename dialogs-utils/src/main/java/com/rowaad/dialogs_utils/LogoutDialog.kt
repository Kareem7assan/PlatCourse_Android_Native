package com.rowaad.dialogs_utils


import android.app.Activity
import android.app.Dialog
import android.view.View

object LogoutDialog {

    fun show(activity: Activity, func: (() -> Unit)? = null) {
        val dialog = CustomDialogBuilder(activity, R.layout.dialog_logout)
            .cancelable(true)
            ?.background(android.R.color.transparent)
            ?.clickListener(R.id.btn_confirm, object : CustomDialogBuilder.OnClickListener {
                override fun onClick(dialog: Dialog?, view: View?) {
                    //dialog?.dismiss()
                    if (NetworkUtil.isOnline(activity)) {
                        dialog?.dismiss()
                        if (func != null) {
                            func()
                        }
                    }

                }
            })
            ?.clickListener(R.id.btn_cancel, object : CustomDialogBuilder.OnClickListener {
                override fun onClick(dialog: Dialog?, view: View?) {
                    dialog?.dismiss()
                }
            })
            ?.visibleOrGone(R.id.btn_confirm,if (func == null) View.GONE else View.VISIBLE)
            ?.build()
            ?.show()

    }

}