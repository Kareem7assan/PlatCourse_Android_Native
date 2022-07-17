package com.platCourse.platCourseAndroid.auth.terms

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.data.model.settings.Record
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.sales_policy.PagesViewModel
import com.platCourse.platCourseAndroid.databinding.FragmentTermsBinding
import com.rowaad.utils.extention.parseHtml

class TermsFragment : BaseFragment(R.layout.fragment_terms) {


    private var binding: FragmentTermsBinding?=null
    private val viewModel: PagesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentTermsBinding.bind(view)
        sendRequest()
        observeTerms()
        setupActions()
    }

    private fun setupActions() {
        binding?.ivBack?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeTerms() {
        handleSharedFlow(viewModel.termsFlow,onSuccess = { it as Record
            binding?.tvTerms?.parseHtml(it.content ?: "")
        })
    }

    private fun sendRequest() {
        viewModel.terms()
    }
}