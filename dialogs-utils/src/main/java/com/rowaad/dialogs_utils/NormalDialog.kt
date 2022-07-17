/*
package com.rowaad.dialogs_utils


import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.WindowManager

object NormalDialog {

    fun show(activity: Activity, func: (() -> Unit)? = null,title:String?=null,description:String?=null) {
        val dialog = CustomDialogBuilder(activity, R.layout.dialog_normal)
            .cancelable(true)
            ?.gravity(Constants.Gravity.GRAVITY_CENTER,700,WindowManager.LayoutParams.WRAP_CONTENT)
            ?.background(android.R.color.transparent)
            ?.text(R.id.textView,title)
            ?.text(R.id.tv_dialog_title,description)
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

}*/
