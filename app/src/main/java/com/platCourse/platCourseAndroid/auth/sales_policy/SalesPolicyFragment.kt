package com.platCourse.platCourseAndroid.auth.sales_policy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.data.model.settings.Record
import com.rowaad.app.usecase.menu.MenuUseCase
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.splash.SplashViewModel
import com.platCourse.platCourseAndroid.databinding.FragmentSalesPolicyBinding
import com.platCourse.platCourseAndroid.databinding.FragmentSplashBinding
import com.rowaad.utils.extention.parseHtml
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalesPolicyFragment : BaseFragment(R.layout.fragment_sales_policy) {


    private var binding: FragmentSalesPolicyBinding?=null

    private val viewModel: PagesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSalesPolicyBinding.bind(view)
        sendRequest()
        observePolicy()
        setupActions()
    }

    private fun observePolicy() {
        handleSharedFlow(viewModel.privacyFlow,onSuccess = { it as Record
              binding?.tvTerms?.parseHtml(it.content ?: "")
        })
    }

    private fun sendRequest() {
        viewModel.privacyPolicy()
    }


    private fun setupActions() {
        binding?.ivBack?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}