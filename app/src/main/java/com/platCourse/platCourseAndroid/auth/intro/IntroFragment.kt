package com.platCourse.platCourseAndroid.auth.intro

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.data.model.TweetAdModel
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.splash.SplashActivity
import com.platCourse.platCourseAndroid.databinding.FragmentIntroBinding
import com.platCourse.platCourseAndroid.home.add_ad.AddAdFragment
import com.platCourse.platCourseAndroid.home.add_ad.viewmodel.AddAdViewModel

class IntroFragment : BaseFragment(R.layout.fragment_intro) {

    private var binding: FragmentIntroBinding? = null

    private val viewModel: AddAdViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentIntroBinding.bind(view)
        setupActions()
        preventBack()
    }

    private fun setupActions() {
        binding?.registerBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_registerFragment,
                    bundleOf(
                            "fromTweet"
                                to
                            fromTweet
                    )
            )
        }
        binding?.loginBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_loginFragment,
                    bundleOf(
                            "fromTweet"
                                    to
                            fromTweet
                    )
                    )
        }
    }

    private fun preventBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            if (fromTweet==null) requireActivity().finish()
            else {
                 AddAdFragment.navigateToAddTweet(requireContext(), fromTweet!!)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }

    companion object NavigateToIntro{
         var fromTweet: TweetAdModel? = null

        fun navigateToIntro(context: Context,tweet:TweetAdModel) {
            fromTweet=tweet
            val pendingIntent = NavDeepLinkBuilder(context)
                    .setComponentName(SplashActivity::class.java)
                    .setGraph(R.navigation.auth_nav_graph)
                    .setDestination(R.id.introFragment)
                    .createPendingIntent()
            pendingIntent.send()
        }
    }
}