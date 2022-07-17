package com.rowaad.utils.extention

import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog

fun BottomSheetDialog.init(view: View) {
    setContentView(view)
    setCancelable(true)
    window?.setBackgroundDrawableResource(android.R.color.transparent)
    window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
}
