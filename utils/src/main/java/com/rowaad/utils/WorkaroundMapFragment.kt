package com.rowaad.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

class WorkaroundMapFragment : SupportMapFragment() {

    private var mListener: OnTouchListener? = null

    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstance: Bundle?): View {
        val layout = super.onCreateView(layoutInflater, viewGroup, savedInstance)
        val frameLayout = activity?.let { TouchableWrapper(it)}

        activity?.let { ContextCompat.getColor(it, android.R.color.transparent) }
            ?.let { frameLayout?.setBackgroundColor(it) }

        (layout as ViewGroup).addView(
                frameLayout,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )

        return layout
    }

    fun setListener(listener: OnTouchListener) {
        mListener = listener
    }

    interface OnTouchListener {
        fun onTouch()
        var isMultiTouch:Boolean?
    }

    inner class TouchableWrapper(context: Context) : FrameLayout(context) {

        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            try {
                Log.e("multiTouch",event.action.toString()+","+event.getPointerCount())

                when (event.action) {

                    MotionEvent.ACTION_DOWN -> mListener?.onTouch()
                    MotionEvent.ACTION_UP -> mListener?.onTouch()
                    MotionEvent.ACTION_POINTER_UP -> mListener?.onTouch()
                    MotionEvent.ACTION_POINTER_DOWN -> mListener?.onTouch()
                    MotionEvent.AXIS_X -> mListener?.onTouch()
                    MotionEvent.AXIS_SIZE -> mListener?.onTouch()
                    else->mListener?.onTouch()



/*
                if (event.action == MotionEvent.ACTION_UP) {
                    Log.e("multiTouch","up")

                    return super.dispatchTouchEvent(event);
                }
                if (event.action == MotionEvent.AXIS_X) {
                    Log.e("multiTouch","up")

                    return true
                }

                if (event.pointerCount > 1 && mListener?.isMultiTouch==true) {
                    Log.e("multiTouch","true")
                    return true
                }*/
            }



        }
            catch (e: Exception) {
                Log.e("multiTouch",e.message.toString())
            }
            return super.dispatchTouchEvent(event)

        }

    protected fun enableMultiTouch(): Boolean {
        return false //and override it to true in my activity
    }
}
}



