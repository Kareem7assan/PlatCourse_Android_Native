package com.rowaad.dialogs_utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.transition.Slide
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetBuilder(context: Context?, @LayoutRes layoutRes: Int) {
    private val view: View = LayoutInflater.from(context).inflate(layoutRes, null)
    private val dialog: BottomSheetDialog = BottomSheetDialog(context!!)
    fun clickListener(@IdRes viewId: Int, onClickListener: OnClickListener): BottomSheetBuilder {
        view.findViewById<View>(viewId)
            .setOnClickListener { v: View? -> onClickListener.onClick(dialog, v) }
        return this
    }

    fun transparentBackground(transparent: Boolean): BottomSheetBuilder {
        if (transparent) {
            dialog.setOnShowListener { dialog: DialogInterface ->
                val d = dialog as BottomSheetDialog
                val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
                    ?: return@setOnShowListener
                bottomSheet.background = null
            }
        }
        return this
    }

    fun background(@DrawableRes drawableRes: Int): BottomSheetBuilder {
        val window = dialog.window
        if (window != null) {
            window.attributes
            window.setBackgroundDrawableResource(drawableRes)
        }
        return this
    }

    fun text(@IdRes viewId: Int, text: String?): BottomSheetBuilder {
        val view = view.findViewById<View>(viewId)
        view.visibility = View.VISIBLE
        (view as TextView).text = text
        return this
    }

    fun drawableEnd(@IdRes viewId: Int, drawable: Drawable?): BottomSheetBuilder {
        val view = view.findViewById<View>(viewId)
        view.visibility = View.VISIBLE
        (view as TextView).setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            drawable,
            null
        )
        return this
    }

    fun image(@IdRes viewId: Int, imageUrl: String?): BottomSheetBuilder {
        val view = view.findViewById<View>(viewId)
        view.visibility = View.VISIBLE
        return this
    }

    fun textColor(@IdRes viewId: Int, colorRes: Int): BottomSheetBuilder {
        val view = view.findViewById<View>(viewId)
        (view as TextView).setTextColor(colorRes)
        return this
    }

    fun setActivated(@IdRes viewId: Int, isFavorite: Boolean?): BottomSheetBuilder {
        val view = view.findViewById<View>(viewId)
        view.visibility = View.VISIBLE
        view.isActivated = isFavorite!!
        return this
    }

    fun setVisbility(@IdRes viewId: Int, isVisible: Boolean): BottomSheetBuilder {
        val view = view.findViewById<View>(viewId)
        if (isVisible) view.visibility = View.GONE else view.visibility = View.VISIBLE
        return this
    }

    fun cancelable(cancelable: Boolean): BottomSheetBuilder {
        dialog.setCancelable(cancelable)
        return this
    }

    fun build(): Dialog {
        return dialog
    }

    fun visibility(
        @IdRes viewId: Int,
        @IdRes viewIdTwo: Int,
        isDamaged: Boolean
    ): BottomSheetBuilder {
        if (isDamaged) {
            val view = view.findViewById<View>(viewId)
            view.visibility = View.VISIBLE
            val view2 = this.view.findViewById<View>(viewIdTwo)
            view2.visibility = View.INVISIBLE
        }
        return this
    }

    fun gravity(@Slide.GravityFlag gravity: Int): BottomSheetBuilder {
        val window = dialog.window
        if (window != null) {
            val attributes = window.attributes
            attributes.gravity = gravity
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = attributes
        }
        return this
    }

    interface OnClickListener {
        fun onClick(dialog: BottomSheetDialog?, view: View?)
    }

    init {
        dialog.setCancelable(false)
        val window = dialog.window
        if (window != null) {
            val attributes = window.attributes
            attributes.gravity = Gravity.CENTER
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            attributes.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = attributes
        }
        //Fonty.setFonts((ViewGroup) view);
        dialog.setContentView(view)
    }
}