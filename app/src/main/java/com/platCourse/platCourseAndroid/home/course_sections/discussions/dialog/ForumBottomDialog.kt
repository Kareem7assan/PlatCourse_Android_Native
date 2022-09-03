package com.platCourse.platCourseAndroid.home.course_sections.discussions.dialog

import android.app.Dialog
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.BottomForumBinding
import org.jetbrains.anko.support.v4.toast

class ForumBottomDialog(private val onAction:(title:String, desc:String)->Unit) : BottomSheetDialogFragment() {

    private var bindSheet: BottomForumBinding? = null

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.bottom_forum, null)
        dialog.setContentView(view)
        bindSheet = BottomForumBinding.bind(view)
        setupActions()

    }

    private fun setupActions() {
        bindSheet?.addDiscBtn?.setOnClickListener {
            when {
                bindSheet?.etTitle?.text?.isBlank()==true -> {
                    toast(getString(R.string.title_required))
                }
                bindSheet?.etDesc?.text?.isBlank()==true -> {
                    toast(getString(R.string.desc_required))
                }
                else -> {
                    onAction.invoke(bindSheet?.etTitle?.text?.toString() ?: "",bindSheet?.etDesc?.text?.toString() ?: "").also { dismiss() }
                }
            }
        }
    }
}