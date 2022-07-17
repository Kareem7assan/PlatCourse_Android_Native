package com.platCourse.platCourseAndroid.auth.login

import android.os.Bundle
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
import com.platCourse.platCourseAndroid.home.add_ad.AddAdFragment
import com.platCourse.platCourseAndroid.home.add_ad.viewmodel.AddAdViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.TweetAdModel
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.utils.extention.getContent
import com.rowaad.utils.extention.startActivityWithAnimationFinishAllStack
import com.rowaad.utils.extention.validateText
import com.rowaad.utils.extention.validateTextPass
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment(R.layout.fragment_login) {
    private val addViewModel: AddAdViewModel by activityViewModels()
    private val binding by viewBinding<FragmentLoginBinding>()
    private val viewModel: LoginViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation()
        setupInputsValidations()
        setupActions()
        setupHint()
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
                    if (isCorrect is LoginViewModel.Validation.IsValid){
                        binding.etPass.validateText(isCorrect.isValid, binding.ivMarkEmail, binding.tvErrorPass)
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
    }

    private fun preventBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireActivity().finishAffinity()
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

        if ((arguments?.getSerializable("fromTweet") as TweetAdModel?)!= null) AddAdFragment.navigateToAddTweet(requireContext(),
            (arguments?.getSerializable("fromTweet") as TweetAdModel?)!!
        ).also { Log.e("data_to_tweet",(arguments?.getSerializable("fromTweet") as TweetAdModel?)!!.imgsModel.map { it.part?.body }.toString() )}
        else requireActivity().startActivityWithAnimationFinishAllStack<HomeActivity>()

    }




    private fun setupHint() {
        binding.etPass.validateTextPass(getString(R.string.enter_pass))
    }
}