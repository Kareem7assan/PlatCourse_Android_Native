package com.platCourse.platCourseAndroid.auth.forget_pass

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.remote.NetWorkState
import com.platCourse.platCourseAndroid.auth.forget_pass.viewmodel.ForgetViewModel
import com.platCourse.platCourseAndroid.BuildConfig
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentForgetPassBinding
import com.rowaad.utils.extention.getContent
import com.rowaad.utils.extention.validateText
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast

class ForgetPassFragment:BaseFragment(R.layout.fragment_forget_pass) {

    private val binding by viewBinding<FragmentForgetPassBinding>()
    private val viewModel: ForgetViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation()
        observeValidationMail()
        setupActions()

    }

    private fun setupHint() {
       // binding.etPhone.validateHintPhone(getString(R.string.enter_phone_hint))

    }

    private fun observeNavigation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFlow.collectLatest { networkState ->
                    when(networkState){
                        is NetWorkState.Loading->{
                            showProgress()
                        }
                        is NetWorkState.StopLoading->{
                            hideProgress()
                        }
                        is NetWorkState.Success<*> ->{
                            handleNavigation(networkState.data as String)
                        }
                        is NetWorkState.Error->{
                            handleErrorGeneral(networkState.th)
                        }

                    }

                }
            }
        }
    }

    private fun handleNavigation(code: String) {

        if (BuildConfig.DEBUG) showSuccessMsg(String.format(getString(R.string.success_code),code)).also { navigateToCode() }
    }

    private fun navigateToCode() {
        findNavController().navigate(R.id.action_forgetFragment_to_verifyCodeFragment)
    }


    private fun observeValidationMail() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidEmailFlow
                    .collect { isMail ->
                        when(isMail){
                            is ForgetViewModel.Validation.IsValid -> {
                                binding.etEmail.validateText(isMail.isValid,binding.ivMarkEmail,binding.tvErrorMail)
                            }
                            else->{}
                        }

                    }
            }
        }
    }


    private fun setupActions() {
        binding.completeBtn.setOnClickListener {
            viewModel.sendRequestMail(binding.etEmail.getContent())
                .also {
                    //initialized
                    viewModel.isValidMail(binding.etEmail.getContent())
                    //watcher
                    binding.etEmail.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidMail(text.toString())
                    }}

        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }


    }

}