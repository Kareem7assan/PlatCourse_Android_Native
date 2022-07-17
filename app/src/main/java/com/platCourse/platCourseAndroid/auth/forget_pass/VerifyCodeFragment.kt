package com.platCourse.platCourseAndroid.auth.forget_pass

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.forget_pass.viewmodel.ForgetViewModel
import com.platCourse.platCourseAndroid.databinding.FragmentVerifyCodeBinding
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import kotlinx.coroutines.flow.collect
import org.jetbrains.anko.support.v4.toast

class VerifyCodeFragment : BaseFragment(R.layout.fragment_verify_code) {

    private lateinit var timer: CountDownTimer
    private val binding by viewBinding<FragmentVerifyCodeBinding>()
    private val viewModel: ForgetViewModel by activityViewModels()




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCode()
        observeValidation()
        observeResendCode()
        setupTimer()
        setupActions()


    }

    private fun observeValidation() {
        handleFlow(viewModel.isValidCode,lifeCycle = Lifecycle.State.CREATED){
            if (it is ForgetViewModel.Validation.IsValid){
                if (it.isValid.not()) showErrorMessage(getString(R.string.invalid_code))
            }
        }
    }

    private fun setupTimer() {
        startResendTimer()
    }


    private fun startResendTimer() {
        timer = object : CountDownTimer(120000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResetPassTimer.show()
                binding.tvResetPass.hide()
                viewModel.timeCount=millisUntilFinished / 1000
                binding.tvResetPassTimer.text =
                    getString(R.string.resend_timer, (millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                binding.tvResetPassTimer.hide()
                binding.tvResetPass.show()

            }
        }

        timer.start()
    }




    private fun navigateToReset() {
        findNavController().navigate(R.id.action_verifyCodeFragment_to_resetPassFragment, bundleOf(
            "code"
        to
            binding.otpView.text.toString()
        ))
    }



    private fun observeCode() {
        handleSharedFlow(viewModel.codeFlow,onSuccess = {
            navigateToReset()
        })

    }

    private fun observeResendCode() {
        handleSharedFlow(viewModel.resendFlow,onSuccess = {
            showSuccessMsg(String.format(getString(R.string.success_code),it as String))
            setupTimer()
            viewModel.code=it
            //toast(it as String)
        })

    }


    private fun setupActions() {
        binding.completeBtn.setOnClickListener {
            viewModel.sendRequestCode(binding.otpView.text.toString())
        }
        binding?.tvResetPass.setOnClickListener {
            viewModel.sendRequestResendCode()
        }
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
    }

}