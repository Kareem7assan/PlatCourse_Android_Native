package com.platCourse.platCourseAndroid.auth.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rowaad.app.base.BaseFragment
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentSplashBinding
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.rowaad.utils.extention.startActivityWithAnimationFinishAllStack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private var binding: FragmentSplashBinding?=null
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSplashBinding.bind(view)
        viewModel.handleNavigation()
        setupNavigation()
    }

    private fun setupNavigation() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationFlow.collect {
                    delay(3000)
                    if (it is SplashViewModel.SplashNavigation.NavigateToHome) handleNavigation()

                }
            }
        }
    }

    private fun handleNavigation(){
        if ((requireActivity() as SplashActivity).fromLogout)
            findNavController().navigate(R.id.action_splashFragment_to_introFragment)
        else
            requireActivity().startActivityWithAnimationFinishAllStack<HomeActivity>()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
}