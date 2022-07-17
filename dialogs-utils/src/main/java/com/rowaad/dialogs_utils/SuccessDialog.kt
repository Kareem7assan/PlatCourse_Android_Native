package com.rowaad.dialogs_utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat


object SuccessDialog {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun show(
        activity: Activity, title: String,  @DrawableRes successRes:Int=R.drawable.ic_ok ,seconds: Int = 2,
        gravity: String = Constants.Gravity.GRAVITY_TOP,
        onSuccess:(()->Unit)?=null
    ) {

        val dialog = CustomDialogBuilder(activity, R.layout.status_dialog){
            onSuccess?.invoke()
        }
            .cancelable(false)
            ?.gravity(
                gravity, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            ?.background(android.R.color.transparent)
            ?.image(R.id.iv_icon, successRes )
            ?.text(R.id.tv_dialog_title, title)
            ?.build()
            ?.show(seconds)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        /*val toastIcon :AppCompatImageView = dialog?.findViewById(R.id.iv_icon)!!
        toastIcon.background = activity.getDrawable(if (status)  R.drawable.ic_true else R.drawable.ic_false)*/

        val iconBackground: ConstraintLayout = dialog?.findViewById(R.id.background_color)!!

        iconBackground.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                activity,
                R.color.apple
            )
        )

        val toastBackground: ConstraintLayout = dialog.findViewById(R.id.toast_container)!!
        toastBackground.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                activity,
                R.color.egg_shell_three
            )
        )

/*
        val titleTextView: AppCompatTextView = dialog.findViewById()!!
        titleTextView.text = title*/

    }


}