package com.platCourse.platCourseAndroid.home.course_details.dialog

import android.app.Dialog
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.BottomContactBinding
import com.platCourse.platCourseAndroid.databinding.BottomForumBinding
import org.jetbrains.anko.support.v4.toast

class CouponPurchaseBottomDialog(private val onAction:(title:String)->Unit) : BottomSheetDialogFragment() {

    private var bindSheet: BottomContactBinding? = null

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.bottom_coupon_purchase, null)
        dialog.setContentView(view)
        bindSheet = BottomContactBinding.bind(view)
        setupActions()

    }

    private fun setupActions() {
        bindSheet?.addContactTeacher?.setOnClickListener {
            when {
                bindSheet?.etMsg?.text?.isBlank()==true -> {
                    toast(getString(R.string.code_required))
                }
                else -> {
                    onAction.invoke(bindSheet?.etMsg?.text?.toString() ?: "").also { dismiss() }
                }
            }
        }
    }
}