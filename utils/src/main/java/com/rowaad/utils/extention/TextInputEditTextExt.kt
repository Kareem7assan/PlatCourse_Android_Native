package com.rowaad.utils.extention

import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.rowaad.utils.R
import org.jetbrains.anko.backgroundDrawable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun TextInputEditText.getContent(): String = this.text.toString().trim()

fun TextInputEditText.validateText(
    condition: Boolean,
    ivMarkGreen: ImageView? = null,
    tvError: TextView? = null,
    action: (() -> Unit)? = null
){
    ivMarkGreen?.isVisible=condition
    if (condition) tvError?.visibility= View.INVISIBLE else tvError?.visibility= View.VISIBLE
    backgroundDrawable = if (condition){
        ContextCompat.getDrawable(context, R.drawable.border_white)

    }
    else{
        ContextCompat.getDrawable(context, R.drawable.border_white_error)
    }
}

fun TextInputEditText.validateTextPass(hintTxt: String){
    hint=hintTxt
    textAlignment=View.TEXT_ALIGNMENT_VIEW_START
    transformationMethod=MyPasswordTransformationMethod()

    doOnTextChanged { text, start, before, count ->

        hint = if (text?.length ?: 0 > 0){

            textAlignment=View.TEXT_ALIGNMENT_VIEW_END
            ""
        } else{
            textAlignment=View.TEXT_ALIGNMENT_VIEW_START
            hintTxt
        }
    }

}

fun TextInputEditText.validateHintPhone(hintTxt: String
) {
    hint = hintTxt
    textAlignment = View.TEXT_ALIGNMENT_VIEW_START


    doOnTextChanged { text, start, before, count ->

        hint = if (text?.length ?: 0 > 0) {

            textAlignment = View.TEXT_ALIGNMENT_VIEW_END
            ""
        } else {
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            hintTxt
        }
    }
}

fun TextInputEditText.validateHint(hintTxt: String
){
    hint=hintTxt
    textAlignment=View.TEXT_ALIGNMENT_VIEW_START


    doOnTextChanged { text, start, before, count ->

        hint = if (text?.length ?: 0 > 0){

            textAlignment=View.TEXT_ALIGNMENT_VIEW_END
            ""
        } else{
            textAlignment=View.TEXT_ALIGNMENT_VIEW_START
            hintTxt
        }
    }
}

class MyPasswordTransformationMethod : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View?): CharSequence {
        return PasswordCharSequence(source)
    }

    class PasswordCharSequence(val mSource: CharSequence):CharSequence{
        override val length: Int
            get() = mSource.length

        override fun get(index: Int): Char {
            return '*'
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource.subSequence(startIndex, endIndex)
        }

    }

}


fun AppCompatAutoCompleteTextView.validateText(
    condition: Boolean,
    ivMarkGreen: ImageView? = null,
    tvError: TextView? = null,
    action: (() -> Unit)? = null
){
    ivMarkGreen?.isVisible=condition
    if (condition) tvError?.visibility= View.INVISIBLE else tvError?.visibility= View.VISIBLE
    backgroundDrawable = if (condition){
        ContextCompat.getDrawable(context, R.drawable.border_white)

    }
    else{
        ContextCompat.getDrawable(context, R.drawable.border_white_error)
    }
}


fun TextInputEditText.onClickShowBirthDatePickerDialog(
    maxDate: String? = null,
    selectedDate: (dateEn:String) -> Unit
) {
    setOnClickListener {
        val calInstance = Calendar.getInstance(Locale("ar"))
        DatePickerDialog(
            context,
            R.style.DialogTheme,
            { view, year, month, dayOfMonth ->
                val cal = Calendar.getInstance(Locale("ar"))

                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd-MM-yyyy" //0000-00-00
                val sdf = SimpleDateFormat(myFormat, Locale("ar"))
                val sdfEn = SimpleDateFormat(myFormat,Locale.ENGLISH)
                setText(sdf.format(cal.time))
                selectedDate(sdfEn.format(cal.time))
            },
            calInstance.get(Calendar.YEAR),
            calInstance.get(Calendar.MONTH),
            calInstance.get(Calendar.DAY_OF_MONTH)
        ).apply {
            //datePicker.minDate=System.currentTimeMillis()/*-1000*/
            if (maxDate!=null){
                val sdf =  SimpleDateFormat("yyyy-MM-dd", Locale("ar") /*Locale.ENGLISH*/);
                try {
                    val mDate = sdf.parse(maxDate)
                    val timeInMilliseconds = mDate.time
                    datePicker.maxDate=timeInMilliseconds
            }
            catch (e: ParseException){ Log.e("errorDate", e.message.toString()) }
            }
            show()
            getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.dark_green_blue
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

//fun TextInputEditText.addTextWatcher(textInputLayout: TextInputLayout,errorTextView: AppCompatTextView,button: AppCompatButton) {
//    addTextChangedListener(textInputLayout.getTextWatcher(this,errorTextView,button))
//}


fun    TextInputEditText.preventClearPrefixData() {

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
            val prefix = "966"
            val count = editable.toString()?.length ?: 0
            if (count < prefix.length) {
                this@preventClearPrefixData.setText(prefix)

                /*
         * This line ensure when you do a rapid delete (by holding down the
         * backspace button), the caret does not end up in the middle of the
         * prefix.
         */
                val selectionIndex = Math.max(count + 1, prefix.length)
                this@preventClearPrefixData
                    .setSelection(selectionIndex)
            }
        }
    })
}
