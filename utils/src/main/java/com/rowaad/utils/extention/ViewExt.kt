package com.rowaad.utils.extention

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.rowaad.utils.extention.dp


fun View.showSnackbar(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, messageRes, length).show()
}

fun View.tint(@ColorRes color: Int) {
    this.backgroundTintList= ColorStateList.valueOf(ContextCompat.getColor(context,color))
}
fun ImageView.tintImage(@ColorRes color: Int) {
    this.imageTintList= ColorStateList.valueOf(ContextCompat.getColor(context,color))
}
fun ImageView.tintImage( hexaColor: String) {
    this.imageTintList= ColorStateList.valueOf(Color.parseColor(hexaColor))
}
fun View.tint( colorHexa: String) {
    this.backgroundTintList= ColorStateList.valueOf(Color.parseColor(colorHexa))
}



/**
 * Show the view  (visibility = View.VISIBLE)
 */
fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.invisible(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

/**
 * Remove the view (visibility = View.GONE)
 */
fun TextView.setColor(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(context,color))
}

fun View.hide(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}
fun View.invis(): View {

        visibility = View.INVISIBLE

    return this
}
 fun MaterialButton.selectDrawing(@ColorRes color: Int,@DrawableRes bg:Int) {
    background=ContextCompat.getDrawable(context,bg)
    setTextColor(ContextCompat.getColor(context,color))

}
 fun MaterialButton.unSelectDrawing(@ColorRes color: Int,@DrawableRes bg:Int) {
     background=ContextCompat.getDrawable(context,bg)
    setTextColor(ContextCompat.getColor(context,color))
}
/**
 * Change view visibility
 */
fun View.changeVisibility(): View {
    visibility = (if (visibility != View.GONE) View.GONE else View.VISIBLE)
    return this
}

/**
 * Rotate view 180 degree
 */
fun View.rotate(): View {
    this.animate().rotationBy(180f).setDuration(350L).start()
    return this
}

/**
 * enable the view.
 */
fun View.enable(): View {
    this.isEnabled = true
    this.isClickable = true
    this.alpha = 1f
    return this
}

/**
 * disable the view.
 */
fun View.disable(): View {
    this.isEnabled = false
    this.isClickable = false
    this.alpha = 0.5f
    return this
}


/**
 *  Animate view from anim Res
 */
fun View.animate(@AnimRes anim: Int, startOffsetTime: Long) {
    val animation =
        AnimationUtils.loadAnimation(context, anim)
    animation.startOffset = startOffsetTime
    this.startAnimation(animation)
}

/**
 * set view width and height
 */
fun View.setDimentions(width: Int, height: Int){
    val params = this.layoutParams
    params.width = width.dp
    params.height = height.dp
    this.layoutParams = params
}



fun CardView.setCardBG(color: Int) {
    context?.let { it1 ->
        ContextCompat.getColor(it1, color)
    }?.let {
        this.setCardBackgroundColor(
            it
        )
    }
}
