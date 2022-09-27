package com.platCourse.platCourseAndroid.auth.register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.login.LoginViewModel
import com.platCourse.platCourseAndroid.databinding.FragmentRegisterBinding
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.UserModel
import com.rowaad.utils.extention.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    private val binding by viewBinding<FragmentRegisterBinding>()
    private val viewModel: RegisterViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupHint()
        setupObservables()
        observeRegister()
        setupController()
        observeNavigation()
        observeLogin()

    }

    private fun observeLogin() {
        handleSharedFlow(loginViewModel.userFlow,onSuccess = {it as UserModel
            handleNavigation(it)
        })
    }
    private fun handleNavigation(userModel: UserModel) {
        showSuccessMsg(String.format(getString(R.string.attemps_times),userModel.login_times)).also {
            Handler(Looper.getMainLooper()).postDelayed({
                requireActivity().startActivityWithAnimationFinishAllStack<HomeActivity>()
            },1000)
        }




    }

    private fun setupController() {
        binding.registerBtn.setOnClickListener {
            viewModel.sendRequestRegister(name = binding.etName.getContent(),phoneNumber = binding.etPhone.getContent(),
                email = binding.etEmail.getContent(),password = binding.etPass.getContent(),confirmPassword = binding.etConfirmPass.getContent(),username = binding.etUsername.getContent(),
                    country = binding.etCountry.getContent(),city = binding.etCity.getContent(),fireBaseToken = "asdakjjza",role = "student"
                )
                .also {
                    //initialized
                    viewModel.isValidNameFlow(binding.etName.getContent())
                    //watcher
                    binding.etName.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidNameFlow(text.toString())
                    }
                }.also {
                    //initialized
                    viewModel.isValidUserNameFlow(binding.etUsername.getContent())
                    //watcher
                    binding.etUsername.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidUserNameFlow(text.toString())
                    }
                    }
                    .also {
                    //initialized
                    viewModel.isValidCountryFlow(binding.etCountry.getContent())
                    //watcher
                    binding.etCountry.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidCountryFlow(text.toString())
                    }
                    }.also {
                    //initialized
                    viewModel.isValidCityFlow(binding.etCity.getContent())
                    //watcher
                    binding.etCity.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidCityFlow(text.toString())
                    }
                    }.also {
                    //initialized
                    viewModel.isValidPhoneFlow(binding.etPhone.getContent())
                    //watcher
                    binding.etPhone.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidPhoneFlow(text.toString())
                    }
                    }.also {
                    //initialized
                    viewModel.isValidMailFlow(binding.etEmail.getContent())
                    //watcher
                    binding.etEmail.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidMailFlow(text.toString())
                    }}.also {
                    //initialized
                    viewModel.isValidPassFlow(binding.etPass.getContent())
                    //watcher
                    binding.etPass.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidPassFlow(text.toString())
                    }}
                .also {
                    //initialized
                    viewModel.isValidConfirmPassFlow(binding.etPass.getContent(),binding.etConfirmPass.getContent())
                    //watcher
                    binding.etConfirmPass.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidConfirmPassFlow(binding.etPass.getContent(),text.toString())
                    }}

        }

        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_global_loginFragment)
        }

    }


    private fun setupHint() {
        binding.etPass.validateTextPass(getString(R.string.enter_pass))
        binding.etConfirmPass.validateTextPass(getString(R.string.reenter_pass))
        binding.etPhone.validateHintPhone(getString(R.string.phone_hint))
        binding.etUsername.validateHintPhone(getString(R.string.user_hint))
        binding.etName.validateHintPhone(getString(R.string.full_name_hint))
        binding.etEmail.validateHintPhone(getString(R.string.email))
        binding.etCountry.validateHintPhone(getString(R.string.country))
        binding.etCity.validateHintPhone(getString(R.string.city))
    }

    private fun setupObservables() {
        observeNameValidation()
        observeUserNameValidation()
        observeCountryValidation()
        observeCityValidation()
        observePhoneValidation()
        observeEmailValidation()
        observePassValidation()
        observeConfirmPassValidation()
        //observeTermValidation()

        //watcher
        binding.etName.addTextChangedListener {
            if (binding.etName.text?.length ?: 0 > 1)  viewModel.isValidNameFlow(binding.etName.text.toString())

    }.also {
        //watcher
        binding.etUsername.addTextChangedListener {
            if (binding.etUsername.text?.length ?: 0 > 1)  viewModel.isValidUserNameFlow(binding.etUsername.text.toString())

        }
    }
    .also {
                //watcher
        binding.etCountry.addTextChangedListener {
            if (binding.etCountry.text?.length ?: 0 > 1)  viewModel.isValidCountryFlow(binding.etCountry.text.toString())
        }
    }.also {
                //watcher
        binding.etCity.addTextChangedListener {
            if (binding.etCity.text?.length ?: 0 > 1)  viewModel.isValidCityFlow(binding.etCity.text.toString())

        }
    }.also {
        //watcher
        binding.etPhone.addTextChangedListener {
            if (binding.etPhone.text?.length ?: 0 > 1)   viewModel.isValidPhoneFlow(binding.etPhone.text.toString())

        }
    }.also {
        //watcher
        binding.etEmail.addTextChangedListener {
            if (binding.etEmail.text?.length ?: 0 > 1)  viewModel.isValidMailFlow(binding.etEmail.text.toString())
        }}.also {
        //watcher
        binding.etPass.addTextChangedListener {
            if (binding.etPass.text?.length ?: 0 > 1)  viewModel.isValidPassFlow(binding.etPass.text.toString())
        }}
    .also {
        //watcher
        binding.etConfirmPass.addTextChangedListener {
            if (binding.etConfirmPass.text?.length ?: 0 > 1) viewModel.isValidConfirmPassFlow(binding.etPass.getContent(),binding.etConfirmPass.text.toString())
        }}

}

    private fun observeNameValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidNameFlow.collect { isName->
                    if (isName is RegisterViewModel.Validation.IsValid){
                        binding.etName.validateText(isName.isValid, null, binding.tvErrorName)
                    }
                }
            }
        }
    }

    private fun observeUserNameValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidUserNameFlow.collect { isName->
                    if (isName is RegisterViewModel.Validation.IsValid){
                        if (isName.isValid)
                            binding.tvErrorUserName.text=""
                        else
                            binding.tvErrorUserName.text=getString(R.string.invalid_user_name)
                        binding.etUsername.validateText(isName.isValid, null, binding.tvErrorUserName)
                    }
                }
            }
        }
    }
    private fun observeCountryValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidCountryFlow.collect { isName->
                    if (isName is RegisterViewModel.Validation.IsValid){
                        binding.etCountry.validateText(isName.isValid, null, binding.tvErrorCountry)
                    }
                }
            }
        }
    }

    private fun observeCityValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidCityFlow.collect { isName->
                    if (isName is RegisterViewModel.Validation.IsValid){
                        binding.etCity.validateText(isName.isValid, null, binding.tvErrorCity)
                    }
                }
            }
        }
    }

    private fun observePhoneValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidPhoneFlow.collect { isPhone->
                    if (isPhone is RegisterViewModel.Validation.IsValid){
                        binding.etPhone.validateText(isPhone.isValid, null, binding.tvErrorPhone)
                    }
                }
            }
        }
    }

    private fun observeEmailValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidEmailFlow.collect { isMail->
                    if (isMail is RegisterViewModel.Validation.IsValid){
                        binding.etEmail.validateText(isMail.isValid, binding.ivMarkEmail, binding.tvErrorMail)
                    }
                }
            }
        }
    }

    private fun observePassValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidPassFlow.collect { isCorrect->
                    if (isCorrect is RegisterViewModel.Validation.IsValid){
                        if (binding.etPass.text.isNullOrEmpty()) binding.tvErrorPass.text=getString(R.string.empty_field)
                        else binding.tvErrorPass.text=getString(R.string.check_field_pass)

                        binding.etPass.validateText(isCorrect.isValid, null, binding.tvErrorPass)
                    }
                }
            }
        }
    }


    private fun observeConfirmPassValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidConfirmPassFlow.collect { isCorrect->
                    if (isCorrect is RegisterViewModel.Validation.IsValid){
                        binding.etConfirmPass.validateText(isCorrect.isValid, null, binding.tvErrorConfirmPass)
                    }
                }
            }
        }
    }


    private fun observeTermValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isValidTermFlow.collect { isCorrect->
                    if (isCorrect is RegisterViewModel.Validation.IsValid){
                        if (isCorrect.isValid.not()){
                            showErrorMessage(getString(R.string.accept_terms))
                        }
                        else{
                            Log.e("terms","not error")
                        }
                    }
                }
            }
        }
    }

    private fun observeRegister() {
        handleSharedFlow(viewModel.userFlow,onShowProgress = {},onHideProgress = {},onSuccess = {},lifeCycle = Lifecycle.State.CREATED)
    }

    private fun observeNavigation() {
        handleFlow(viewModel.userNavigation){
            //navigateToHome(it.second)
            loginViewModel.sendRequestLogin(email = it.first,pass = it.second)
        }
    }





}