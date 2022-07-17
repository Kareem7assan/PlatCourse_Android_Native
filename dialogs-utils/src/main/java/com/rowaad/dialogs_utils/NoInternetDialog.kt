package com.rowaad.dialogs_utils


import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.WindowManager

object NoInternetDialog {

    fun show(activity: Activity, func: (() -> Unit)? = null) {
        val dialog = CustomDialogBuilder(activity, R.layout.dialog_no_internet)
            .cancelable(true)
            ?.gravity(
                Constants.Gravity.GRAVITY_CENTER,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            ?.background(android.R.color.transparent)
            ?.clickListener(R.id.btn_retry, object : CustomDialogBuilder.OnClickListener {
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
            ?.visibleOrGone(R.id.btn_retry,if (func == null) View.GONE else View.VISIBLE)
            ?.build()
            ?.show()

    }

}