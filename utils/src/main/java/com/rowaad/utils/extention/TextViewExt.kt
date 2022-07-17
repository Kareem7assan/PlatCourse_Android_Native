package com.rowaad.utils.extention

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.rowaad.utils.R
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onTouch
import java.util.*

fun TextView.textWithNegativeFlag() {
    paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}
fun TextView.textWithNormalFlag() {
    paintFlags=this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

fun TextView.setHtmlText(text: String?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
    } else {
        this.text = Html.fromHtml(text)
    }
}
fun String.searchHighLight(key: String,context: Context,@ColorRes color: Int): Spannable {
    val spannable: Spannable = SpannableString(this)
    val blueColor =
            ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(context, color)))
    val highlightSpan =
            TextAppearanceSpan(null, Typeface.NORMAL, -1, blueColor, null)
    if (indexOf(key) > -1)    spannable.setSpan(highlightSpan, indexOf(key), indexOf(key)+key.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannable
}

fun TextView.onClickWord( onInvoked:(String?)->Unit){
    setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val mOffset = getOffsetForPosition(motionEvent.x, motionEvent.y)
                    onInvoked.invoke(findWordForRightHanded(text.toString(), mOffset))

            }
        return@setOnTouchListener false

    }
    }


private fun findWordForRightHanded(
    str: String,
    offset: Int
): String? { // when you touch ' ', this method returns left word.
    var offset = offset
    if (str.length == offset) {
        offset-- // without this code, you will get exception when touching end of the text
    }
    if (str[offset] == ' ') {
        offset--
    }
    var startIndex = offset
    var endIndex = offset
    try {
        while (str[startIndex] != ' ' && str[startIndex] != '\n') {
            startIndex--
        }
    } catch (e: StringIndexOutOfBoundsException) {
        startIndex = 0
    }
    try {
        while (str[endIndex] != ' ' && str[endIndex] != '\n') {
            endIndex++
        }
    } catch (e: StringIndexOutOfBoundsException) {
        endIndex = str.length
    }

    // without this code, you will get 'here!' instead of 'here'
    // if you use only english, just check whether this is alphabet,
    // but 'I' use korean, so i use below algorithm to get clean word.
    val last = str[endIndex - 1]
    if (last == ',' || last == '.' || last == '!' || last == '?' || last == ':' || last == ';') {
        endIndex--
    }
    return str.substring(startIndex, endIndex)
}
fun TextView.setUnderLine() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG

}

fun TextView.setTextViewColor(color: Int) {
    this.setTextColor(ContextCompat.getColor(context, color))
}


fun View.viewMaterialTint(color: Int) {
    this.backgroundTintList =
        context?.let { it1 ->
            ContextCompat.getColorStateList(it1, color)
        }
}


fun TextView.textWithNegativeFlag(price: String, color: String? = null) {
    text=price
    if (color!=null) setTextColor(Color.parseColor(color))
    paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.underLine() {
    paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.parseHtml(title: String){
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(title)
    }
}


fun WebView.parseHtml(content: String){
    settings.builtInZoomControls = true
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    loadData(content,"text/html", "utf-8")
}


fun TextView.formatPrice(price: Float, currency: String = ""){
    if (price.toString().endsWith("0", true)){
        text=String.format(Locale.ENGLISH, "%.0f", price)+" "+currency
    }
    else{
        text=String.format(Locale.ENGLISH, "%.1f", price)+" "+currency
    }

}
fun TextView.formatFloat(number: Float){
    text = if (number.toString().endsWith("0", true)){
        String.format(Locale.ENGLISH, "%.0f", number)
    } else{
        String.format(Locale.ENGLISH, "%.2f", number)
    }
}
fun TextView.formatRate(rate: Float) {
    this.show()
    text = if (rate.toString().endsWith("0", true)) {
        String.format(Locale.ENGLISH, "%.0f", rate) + "/5"
    } else {
        String.format(Locale.ENGLISH, "%.1f", rate) + "/5"
    }

}
