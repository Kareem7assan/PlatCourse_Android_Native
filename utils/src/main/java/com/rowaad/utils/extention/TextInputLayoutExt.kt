package com.rowaad.utils.extention

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rowaad.app.data.R


/**
 * handle text input layout error it takes errorTextView than need to be visible
 */
//fun TextInputLayout.error(errorTextView: AppCompatTextView) {
//    errorTextView.visible()
//    setBackgroundResource(R.drawable.bg_edit_text_error_red)
//    this.errorIconDrawable = null
//    this.requestFocus()
//}

/**
 * set error to text input layout
 * pass string error as a parameter
 */
//fun TextInputLayout.error(errorString: String) {
//    error = errorString
//    setBackgroundResource(R.drawable.bg_edit_text_error_red)
//    this.errorIconDrawable = null
//    this.requestFocus()
//}
//
//fun TextInputLayout.correct(errorText: TextView) {
//    errorText.gone()
//    setBackgroundResource(R.drawable.bg_edit_text_normal_white)
//    isErrorEnabled=false
//    error=null
//    errorIconDrawable = null
//}
//

//fun TextInputLayout.getTextWatcher(
//    editText: TextInputEditText,
//    errorLayout: AppCompatTextView,
//    button: AppCompatButton? = null
//) : TextWatcher {
//    val textInputLayout = this
//    return object : TextWatcher {
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//        }
//
//        override fun afterTextChanged(s: Editable?) {
//            textInputLayout.correct(errorLayout)
//            button.apply {
//                if(s.toString().isNotEmpty()) button?.enable()
//                else button?.disable()
//            }
//        }
//    }
//}


