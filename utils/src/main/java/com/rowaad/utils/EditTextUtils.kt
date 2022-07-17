package  com.rowaad.utils

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.EditText
import androidx.core.content.ContextCompat

object EditTextUtils {


    fun handleSeparatedColors(context: Context, smbol: String, textView: EditText, word: String) {
        val separated = word.split(smbol)

        val firstWord = separated[0]


        val SS = SpannableStringBuilder(word)

        SS.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(context, R.color.booger_Two)

            ), 0, firstWord.length,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )

        SS.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(context, R.color.almost_black)
            ), firstWord.length + 1,
            word.length,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )

        textView.setText(SS)
    }
}
