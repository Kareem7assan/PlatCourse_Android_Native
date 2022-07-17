package com.rowaad.dialogs_utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide

class CustomDialogBuilder() {
    private var view: View? = null
    private var context: Context? = null
    private var counterFinish: OnCounterFinish? = null
    private var countDownTimer: CountDownTimer? = null
    private var dialog: Dialog? = null
    private  var onSuccess: (() -> Unit)? =null

    // private lateinit var customDialog: CustomDialogBuilder
    constructor(
        context: Activity,
        @LayoutRes layoutRes: Int,
        isFullScreen: Boolean = false,
        onSuccess: (() -> Unit)? =null
    ) : this() {
        view = LayoutInflater.from(context).inflate(layoutRes, null)
        dialog = if(isFullScreen) Dialog(context,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        else Dialog(context)
        dialog!!.setCancelable(true)
        this.context = context
        dialog!!.setContentView(view!!)
        this.onSuccess=onSuccess

    }
    constructor(
        context: Context,
        @LayoutRes layoutRes: Int,
        onSuccess: (() -> Unit)? =null
    ) : this() {
        view = LayoutInflater.from(context).inflate(layoutRes, null)
        dialog = Dialog(context)
        dialog!!.setCancelable(true)
        this.context = context
        dialog!!.setContentView(view!!)
        this.onSuccess=onSuccess
    }

    fun clickListener(@IdRes viewId: Int, onClickListener: OnClickListener): CustomDialogBuilder? {
        val view = view!!.findViewById<View>(viewId)
        view.visibility = View.VISIBLE
        view.setOnClickListener { v: View? ->
            onClickListener.onClick(
                dialog,
                v
            )
        }
        return this
    }

    fun editText(
        @IdRes viewId: Int,
        onTextChangeListener: OnTextChangeListener
    ): CustomDialogBuilder? {
        val view = view!!.findViewById<EditText>(viewId)
        view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                onTextChangeListener.onTextChange(editable.toString())
            }
        })
        return this
    }

    fun rateChangeListener(
        @IdRes viewId: Int,
        onRateChangeListener: OnRateChangeListener
    ): CustomDialogBuilder? {
        val view = view!!.findViewById<RatingBar>(viewId)
        view.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar: RatingBar?, rating: Float, fromUser: Boolean ->
                onRateChangeListener.onRateChange(
                    rating
                )
            }
        return this
    }

    fun background(@DrawableRes drawableRes: Int): CustomDialogBuilder? {
        val window = dialog!!.window
        if (window != null) {
            window.attributes
            window.setBackgroundDrawableResource(drawableRes)
        }
        return this
    }

    fun backgroundTint(@DrawableRes drawableRes: Int): CustomDialogBuilder? {
        val window = dialog!!.window
        if (window != null) {
            window.attributes
            window.setBackgroundDrawableResource(drawableRes)
        }
        return this
    }

    fun text(@IdRes viewId: Int, text: String?): CustomDialogBuilder? {
        if (!TextUtils.isEmpty(text)) {
            val view = view!!.findViewById<View>(viewId)
            view.visibility = View.VISIBLE
            (view as TextView).text = text
        } else {
            val view = view!!.findViewById<View>(viewId)
            view.visibility = View.GONE
        }
        return this
    }

    fun image(@IdRes viewId: Int, @DrawableRes drawable: Int): CustomDialogBuilder? {
        if (drawable != 0) {
            val view = view!!.findViewById<View>(viewId)
            view.visibility = View.VISIBLE
            context?.let { Glide.with(it).load(drawable).into((view as ImageView)) }
        } else {
            val view = view!!.findViewById<View>(viewId)
            view.visibility = View.GONE
        }
        return this
    }

    fun visibleOrGone(@IdRes viewId: Int, visibilty: Int): CustomDialogBuilder? {
        val view = view!!.findViewById<View>(viewId)
        view.visibility = visibilty
        return this
    }


    fun gravity(gravity: String, width: Int, height: Int): CustomDialogBuilder? {
        val window = dialog!!.window
        if (window != null) {
            val attributes = window.attributes
            when (gravity) {
                Constants.Gravity.GRAVITY_CENTER -> attributes.gravity = Gravity.CENTER
                Constants.Gravity.GRAVITY_BOTTOM -> attributes.gravity = Gravity.BOTTOM
                Constants.Gravity.GRAVITY_TOP -> attributes.gravity = Gravity.TOP
//                if ("en".equals(new PreferencesUtil (context).getString(BaseActivity.LOCALE_KEY))) {
//                    window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//                } else {
//                    window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                }
            }
            attributes.width = width
            attributes.height = height
            //            if ("en". equals(new PreferencesUtil(context).getString(BaseActivity.LOCALE_KEY))) {
//                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//            } else {
//                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//            }
            window.attributes = attributes
        }
        return this
    }

    fun cancelable(cancelable: Boolean): CustomDialogBuilder? {
        dialog!!.setCancelable(cancelable)
        return this
    }

    fun background(drawable: Drawable?): CustomDialogBuilder? {
        val window = dialog!!.window
        if (window != null) {
            dialog!!.window!!.setBackgroundDrawable(drawable)
        }
        return this
    }

    fun build(): CustomDialogBuilder? {

        return this
    }

    fun show(durationInSeconds: Int): Dialog? {
        dialog?.show()

        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog != null)
                dialog?.dismiss().also { onSuccess?.invoke() }
            onSuccess
        }, (durationInSeconds * 1000).toLong())
        return dialog
    }

    fun show(): Dialog? {
        dialog!!.show()
        return dialog
    }

    fun counterFinishListener(counterFinish: OnCounterFinish?): CustomDialogBuilder? {
        this.counterFinish = counterFinish
        return this
    }


    interface OnRateChangeListener {
        fun onRateChange(rate: Float)
    }

    interface OnClickListener {
        fun onClick(dialog: Dialog?, view: View?)
    }

    interface OnCounterFinish {
        fun onFinish()
    }

    interface OnTextChangeListener {
        fun onTextChange(text: String?)
    }
}