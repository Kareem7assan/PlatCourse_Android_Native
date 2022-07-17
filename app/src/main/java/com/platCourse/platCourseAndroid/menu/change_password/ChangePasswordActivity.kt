package com.platCourse.platCourseAndroid.menu.change_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.remote.NetWorkState
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.forget_pass.viewmodel.ForgetViewModel
import com.platCourse.platCourseAndroid.databinding.ActivityChangePasswordBinding
import com.platCourse.platCourseAndroid.databinding.ActivityEditProfileBinding
import com.rowaad.utils.extention.getContent
import com.rowaad.utils.extention.validateText
import com.rowaad.utils.extention.validateTextPass
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChangePasswordActivity : BaseActivity(R.layout.activity_change_password) {

    private var binding: ActivityChangePasswordBinding? = null


    private fun handleToolbar() {
        binding?.toolbar?.tvTitle?.text = getString(R.string.change_password)
        binding?.toolbar?.ivBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private val viewModel: ForgetViewModel by viewModels()

    override fun init() {
        binding = ActivityChangePasswordBinding.bind(findViewById(R.id.rootPass))
        handleToolbar()
        setupToolbar()
        observeValidationOldPass()
        observeValidationNewPass()
        observeConfirmValidationPass()
        observeNavigation()
        setupObserveHint()
        setupActions()
    }

    private fun setupObserveHint() {
        binding?.etOldPass?.validateTextPass("")
        binding?.etNewPass?.validateTextPass("")
        binding?.etConfirmPass?.validateTextPass("")
    }

    private fun setupToolbar() {
        binding?.toolbar?.tvTitle?.text=getString(R.string.change_password)
        binding?.toolbar?.ivBack?.setOnClickListener{
            onBackPressed()
        }
    }

    private fun observeNavigation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateFlow.collectLatest { networkState ->
                    when(networkState){
                        is NetWorkState.Loading->{
                            showProgress()
                        }
                        is NetWorkState.StopLoading->{
                            hideProgress()
                        }
                        is NetWorkState.Success<*> ->{
                            showSuccessMsg(getString(R.string.pass_updated_successfully)).also { onBackPressed() }
                        }
                        is NetWorkState.Error->{
                            handleErrorGeneral(networkState.th)
                        }

                    }

                }
            }
        }
    }

    private fun setupActions() {
        binding!!.completeBtn.setOnClickListener {
            viewModel.sendRequestUpdate(password = binding!!.etOldPass.getContent(),newPass = binding!!.etNewPass.getContent(),confirmPassword = binding!!.etConfirmPass.getContent())
                .also {
                    //initialized
                    viewModel.isValidPass(binding!!.etOldPass.getContent())
                    //watcher
                    binding!!.etOldPass.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidPass(text.toString())
                    }}.also {
                    //initialized
                    viewModel.isValidNewPass(binding!!.etNewPass.getContent())
                    //watcher
                    binding!!.etNewPass.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidNewPass(text.toString())
                    }}
                .also {
                    viewModel.isValidConfirmPass(binding!!.etNewPass.getContent(),binding!!.etConfirmPass.getContent())
                    binding!!.etConfirmPass.doOnTextChanged { text, start, before, count ->
                        viewModel.isValidConfirmPass(binding!!.etNewPass.getContent(),text.toString())
                    }
                }
        }


    }


    private fun observeValidationOldPass() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidPassFlow
                    .collect { isPass ->
                        when(isPass){
                            is ForgetViewModel.Validation.IsValid-> {
                                binding!!.etOldPass.validateText(isPass.isValid, null, binding!!.tvErrorPass)
                            }
                            else->{}
                        }

                    }
            }
        }
    }
    private fun observeValidationNewPass() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidNewPassFlow
                    .collect { isPass ->
                        when(isPass){
                            is ForgetViewModel.Validation.IsValid-> {
                                binding!!.etNewPass.validateText(isPass.isValid, null, binding!!.tvErrorNewPass)
                            }
                            else->{}
                        }

                    }
            }
        }
    }

    private fun observeConfirmValidationPass() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isValidConfirmPassFlow
                    .collect { isConfirmPass ->
                        when(isConfirmPass){
                            is ForgetViewModel.Validation.IsValid-> {
                                binding!!.etConfirmPass.validateText(isConfirmPass.isValid, null, binding!!.tvErrorConfirmPass)

                            }
                            else->{}
                        }

                    }
            }
        }
    }

}