package com.platCourse.platCourseAndroid.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.TweetAdModel
import com.rowaad.app.data.model.tweets_model.TweetsModel
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentRegisterBinding
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.platCourse.platCourseAndroid.home.add_ad.AddAdActivity
import com.platCourse.platCourseAndroid.home.add_ad.AddAdFragment
import com.platCourse.platCourseAndroid.home.add_ad.viewmodel.AddAdViewModel
import com.rowaad.utils.extention.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    private val binding by viewBinding<FragmentRegisterBinding>()
    private val viewModel: RegisterViewModel by viewModels()
    private val addViewModel: AddAdViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHint()
        setupObservables()
        observeRegister()
        setupController()
        observeNavigation()
        binding.cbTerms.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.isValidTermFlow(b)
        }
    }

    private fun setupController() {
        binding.registerBtn.setOnClickListener {
            viewModel.sendRequestRegister(name = binding.etFname.getContent(),phone = binding.etPhone.getContent(),
                mail = binding.etEmail.getContent(),pass = binding.etPass.getContent(),confirmPass = binding.etConfirmPass.getContent(),hasTerms = binding.cbTerms.isChecked
                )
                .also {
                    //initialized
                    viewModel.isValidNameFlow(binding.etFname.getContent())
                    //watcher
                    binding.etFname.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidNameFlow(text.toString())
                    }

                    viewModel.isValidTermFlow(binding.cbTerms.isChecked)


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

        binding.tvTermsDelegate.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_termsFragment).also { viewModel.idleTermFlow() }
        }
        binding.tvSalesPolicy.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_salesPolicy).also { viewModel.idleTermFlow() }
        }

        }


    private fun setupHint() {
        binding.etPass.validateTextPass(getString(R.string.enter_pass))
        binding.etConfirmPass.validateTextPass(getString(R.string.reenter_pass))
        binding.etPhone.validateHintPhone("")
    }

    private fun setupObservables() {
        observeNameValidation()
        observePhoneValidation()
        observeEmailValidation()
        observePassValidation()
        observeConfirmPassValidation()
        observeTermValidation()
    }

    private fun observeNameValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidNameFlow.collect { isName->
                    if (isName is RegisterViewModel.Validation.IsValid){
                        binding.etFname.validateText(isName.isValid, binding.ivMarkName, binding.tvErrorFName)
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
                        binding.etPhone.validateText(isPhone.isValid, binding.ivMarkPhone, binding.tvErrorPhone)
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

                        binding.etPass.validateText(isCorrect.isValid, binding.ivMarkPass, binding.tvErrorPass)
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
                        binding.etConfirmPass.validateText(isCorrect.isValid, binding.ivMarkConfirmPass, binding.tvErrorConfirmPass)
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
        handleSharedFlow(viewModel.userFlow,onSuccess = {},lifeCycle = Lifecycle.State.CREATED)
    }

    private fun observeNavigation() {
        handleFlow(viewModel.userNavigation){
            if (it.first){
                navigateToActivation()
            }
        }
    }

    private fun navigateToActivation() {
        if ((arguments?.getSerializable("fromTweet") as TweetAdModel?)!= null) AddAdFragment.navigateToAddTweet(requireContext(),
            (arguments?.getSerializable("fromTweet") as TweetAdModel?)!!
        ).also { Log.e("data_to_tweet",(arguments?.getSerializable("fromTweet") as TweetAdModel?)!!.imgsModel.map { it.part?.body }.toString() )}
        else requireActivity().startActivityWithAnimationFinishAllStack<HomeActivity>()

    }



}