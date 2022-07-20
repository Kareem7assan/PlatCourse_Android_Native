package com.platCourse.platCourseAndroid.auth.intro

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentIntroBinding
import com.rowaad.app.base.BaseFragment

class IntroFragment : BaseFragment(R.layout.fragment_intro) {

    private var binding: FragmentIntroBinding? = null

    //private val viewModel: AddAdViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentIntroBinding.bind(view)
        setupActions()
    }

    private fun setupActions() {
        binding?.registerBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_registerFragment,

            )
        }
        binding?.loginBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_loginFragment,

                    )
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }

    companion object NavigateToIntro{

    }
}