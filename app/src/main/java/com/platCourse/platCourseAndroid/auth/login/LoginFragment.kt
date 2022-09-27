package com.platCourse.platCourseAndroid.auth.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentLoginBinding
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.data.utils.Constants_Api
import com.rowaad.utils.extention.getContent
import com.rowaad.utils.extention.startActivityWithAnimationFinishAllStack
import com.rowaad.utils.extention.validateText
import com.rowaad.utils.extention.validateTextPass
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment(R.layout.fragment_login) {
    private val binding by viewBinding<FragmentLoginBinding>()
    private val viewModel: LoginViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getBoolean(Constants_Api.INTENT.LOGOUT,false)==true)
            preventBack()

        observeNavigation()
        setupInputsValidations()
        setupActions()


    }

    private fun setupInputsValidations() {
        observeEmailValidation()
        observePassValidation()
    }


    private fun observeEmailValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidMailFlow.collect { isMail->
                    if (isMail is LoginViewModel.Validation.IsValid){
                        binding.etEmail.validateText(isMail.isValid, null, binding.tvErrorMail)
                    }
                }
            }
        }
    }

    private fun observePassValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidPassFlow.collect { isCorrect->
                    if (isCorrect is LoginViewModel.Validation.IsValid){
                        binding.etPass.validateText(isCorrect.isValid, null, binding.tvErrorPass)
                    }
                }
            }
        }
    }

    private fun setupActions() {
        binding.loginBtn.setOnClickListener {
            viewModel.sendRequestLogin(email = binding.etEmail.getContent(),pass = binding.etPass.getContent())
                    .also {
                        //initialized
                        viewModel.isValidMail(binding.etEmail.getContent())
                        //watcher
                        binding.etEmail.doOnTextChanged { text, start, before, count ->
                            viewModel.isValidMail(text.toString())
                        }}.also {
                        //initialized
                        viewModel.isValidPass(binding.etPass.getContent())
                        //watcher
                        binding.etPass.doOnTextChanged { text, start, before, count ->
                            viewModel.isValidPass(text.toString())
                        }}
        }
        binding.tvForget.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetFragment)
        }
        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun preventBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireActivity().onBackPressed()
        }
    }

    private fun observeNavigation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFlow
                    .collectLatest { networkState ->
                        when(networkState){
                            is NetWorkState.Loading -> {
                                showProgress()
                            }
                            is NetWorkState.StopLoading -> {
                                hideProgress()
                            }
                            is NetWorkState.Success<*> -> {
                                handleNavigation(networkState.data as UserModel)
                            }
                            is NetWorkState.Error -> {
                                handleErrorGeneral(networkState.th)
                            }

                        }

                    }
            }
        }
    }

    private fun handleNavigation(userModel: UserModel) {
        showSuccessMsg(String.format(getString(R.string.attemps_times),userModel.login_times)).also {
                Handler(Looper.getMainLooper()).postDelayed({
                    requireActivity().startActivityWithAnimationFinishAllStack<HomeActivity>()
                },1000)
        }




    }




    private fun setupHint() {
        binding.etPass.validateTextPass(getString(R.string.enter_pass))
    }
}