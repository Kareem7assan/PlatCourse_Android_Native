package com.rowaad.utils.extention

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.style.ForegroundColorSpan
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.rowaad.utils.EditTextUtils.handleSeparatedColors
import com.rowaad.utils.R
import java.text.SimpleDateFormat
import java.util.*

fun EditText.error(@StringRes errorRes: Int) {
    error = resources.getString(errorRes)
    this.requestFocus()
}

fun EditText.isValidName(): Boolean {
    return text.toString().length > 2
}

fun EditText.isValidPhone(): Boolean {
    return text.toString().trim().length == 9
}

fun EditText.isValidEmail(): Boolean {
    val p = "[a-zA-Z0-9._\\-]+@[a-zA-Z0-9._\\-]+\\.+[a-z]+".toRegex()
    return text.toString().matches(p)
}

fun EditText.onClickShowDatePickerDialog(selectedDate: (date: String) -> Unit) {
    setOnClickListener {
        val calInstance = Calendar.getInstance()

        DatePickerDialog(
            context,
            R.style.DialogTheme,
            { view, year, month, dayOfMonth ->
                calInstance.set(Calendar.YEAR, year)
                calInstance.set(Calendar.MONTH, month)
                calInstance.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "yyyy-MM-dd" //0000-00-00
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                setText(sdf.format(calInstance.time))
                selectedDate(text.toString())
            },
            calInstance.get(Calendar.YEAR),
            calInstance.get(Calendar.MONTH),
            calInstance.get(Calendar.DAY_OF_MONTH)
        ).apply {
            //datePicker.minDate=System.currentTimeMillis()/*-1000*/
            show()
            getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.turquoise_blue
                )
            )
            getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.grey_900
                )
            )
        }
    }

}

fun EditText.clearTxt(){
    this.text.clear()
}
fun EditText.appendPreventClearData(context: Context, postFix: String) {
    this.setText(postFix)
    this.addTextChangedListener(object : TextWatcher {
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
            val count = editable.toString().length
            if (count < postFix.length) {
                this@appendPreventClearData.setText(postFix)

                /*
         * This line ensure when you do a rapid delete (by holding down the
         * backspace button), the caret does not end up in the middle of the
         * prefix.
         */
                val selectionIndex = Math.max(count + 1, postFix.length)
                this@appendPreventClearData.setSelection(selectionIndex)


                handleSeparatedColors(
                    context,   postFix ,
                    this@appendPreventClearData,
                    this@appendPreventClearData.text.toString()
                )
            }
        }
    })
}





