package com.rowaad.utils.extention

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View



fun View.animateHide(){
    animate()
        .translationY(height.toFloat())
        .alpha(0.0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                visibility = View.GONE
            }
        })
}

fun View.animateHideOnly(){
    animate()
        .alpha(0.0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                visibility = View.GONE
            }
        })
}


fun View.animateShow(){
    animate()
        .translationY(0f)
        .alpha(1.0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                visibility = View.VISIBLE
            }
        })
}

fun View.animateShowOnly(){
    animate()
        .alpha(1.0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                visibility = View.VISIBLE
            }
        })
}

