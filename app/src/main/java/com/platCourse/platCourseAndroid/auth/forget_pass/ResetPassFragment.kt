package com.platCourse.platCourseAndroid.auth.forget_pass

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.remote.NetWorkState
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.forget_pass.viewmodel.ForgetViewModel
import com.platCourse.platCourseAndroid.databinding.FragmentResetPassBinding
import com.rowaad.utils.extention.getContent
import com.rowaad.utils.extention.validateText
import com.rowaad.utils.extention.validateTextPass
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ResetPassFragment : BaseFragment(R.layout.fragment_reset_pass) {


    private val binding by viewBinding<FragmentResetPassBinding>()
    private val viewModel: ForgetViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation()
        observeValidationPass()
        observeConfirmValidationPass()
        setupActions()
       // setupHint()

    }

    private fun setupHint() {
        binding.etPass.validateTextPass(getString(R.string.enter_password_hint))
        binding.etConfirmPass.validateTextPass(getString(R.string.confirm_password_hint))
    }

    private fun observeNavigation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resetFlow.collectLatest { networkState ->
                    when(networkState){
                        is NetWorkState.Loading->{
                            showProgress()
                        }
                        is NetWorkState.StopLoading->{
                            hideProgress()
                        }
                        is NetWorkState.Success<*> ->{
                            handleNavigation( )
                        }
                        is NetWorkState.Error->{
                            handleErrorGeneral(networkState.th)
                        }

                    }

                }
            }
        }
    }


    private fun handleNavigation() {
        showSuccessMsg(getString(R.string.reset_password_succssfully))
        findNavController().navigate(R.id.action_resetPassFragment_to_loginFragment)
    }


    private fun observeValidationPass() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidPassFlow
                    .collect { isPass ->
                        when(isPass){
                            is ForgetViewModel.Validation.IsValid-> {
                                binding.etPass.validateText(isPass.isValid, null, binding.tvErrorPass)
                            }
                            else->{}
                        }

                    }
            }
        }
    }
    private fun observeConfirmValidationPass() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidConfirmPassFlow
                    .collect { isConfirmPass ->
                        when(isConfirmPass){
                            is ForgetViewModel.Validation.IsValid-> {
                                binding.etConfirmPass.validateText(isConfirmPass.isValid, null, binding.tvErrorConfirmPass)

                            }
                            else->{}
                        }

                    }
            }
        }
    }



    private fun setupActions() {
        binding.completeBtn.setOnClickListener {
            viewModel.sendRequestResetCode(binding.etPass.getContent(),binding.etConfirmPass.getContent(),arguments?.getString("code")!!)
                .also {
                    //initialized
                    viewModel.isValidPass(binding.etPass.getContent())
                    //watcher
                    binding.etPass.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidPass(text.toString())
                    }}
                .also {
                    viewModel.isValidConfirmPass(binding.etPass.getContent(),binding.etConfirmPass.getContent())
                    binding.etConfirmPass.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidConfirmPass(binding.etPass.getContent(),text.toString())
                    }
                }
        }




    }

}