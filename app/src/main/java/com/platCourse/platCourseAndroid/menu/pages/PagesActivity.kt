package com.platCourse.platCourseAndroid.menu.pages

import androidx.activity.viewModels
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.model.settings.Record
import com.rowaad.app.data.utils.Constants_Api
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.sales_policy.PagesViewModel
import com.platCourse.platCourseAndroid.databinding.FragmentSalesPolicyBinding

class PagesActivity : BaseActivity(R.layout.fragment_sales_policy) {

    private var binding: FragmentSalesPolicyBinding?=null
    private val viewModel: PagesViewModel by viewModels()

    override fun init() {
        binding= FragmentSalesPolicyBinding.bind(findViewById(R.id.scrollRoot))
        binding?.tvTerms?.settings?.builtInZoomControls = true
        binding?.tvTerms?.settings?.javaScriptEnabled = true
        binding?.tvTerms?.settings?.domStorageEnabled = true

        sendRequest()
        observePolicy()
        observeTerms()
        handleTitle()
        observeAboutUs()
        setupActions()
    }

    private fun handleTitle() {
        when(intent.getStringExtra("page")){
            "sales"->binding?.tvTitle?.text=getString(R.string.sales_policy_title)
            "aboutUs"->binding?.tvTitle?.text=getString(R.string.about_us)
            "terms"->binding?.tvTitle?.text=getString(R.string.privacy_terms)
        }

    }

    private fun sendRequest() {
        when(intent.getStringExtra("page")){
            "sales"->viewModel.privacyPolicy()
            "aboutUs"->viewModel.aboutUs()
            "terms"->viewModel.terms()
        }

    }
    private fun observePolicy() {
        handleSharedFlow(viewModel.privacyFlow,onSuccess = { it as Record

            binding?.tvTerms?.loadData(it.content ?: "","text/html", "utf-8")
        })
    }

    private fun observeTerms() {
        handleSharedFlow(viewModel.termsFlow,onSuccess = { it as Record
            binding?.tvTerms?.loadData(it.content ?: "","text/html", "utf-8")
        })
    }
    private fun observeAboutUs() {
        handleSharedFlow(viewModel.aboutUsFlow,onSuccess = { it as Record
            binding?.tvTerms?.loadData(it.content ?: "","text/html", "utf-8")
        })
    }




    private fun setupActions() {
        binding?.ivBack?.setOnClickListener {
            onBackPressed()
        }
    }


}