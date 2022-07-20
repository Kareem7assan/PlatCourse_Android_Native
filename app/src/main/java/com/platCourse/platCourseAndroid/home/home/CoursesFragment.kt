package com.platCourse.platCourseAndroid.home.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.login.LoginViewModel
import com.platCourse.platCourseAndroid.databinding.FragmentCoursesBinding
import com.platCourse.platCourseAndroid.databinding.FragmentLoginBinding
import com.platCourse.platCourseAndroid.home.home.adapter.CoursesAdapter
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.courses_model.CoursesModel

class CoursesFragment : BaseFragment(R.layout.fragment_courses) {


    private val binding by viewBinding<FragmentCoursesBinding>()
    private val viewModel: CoursesViewModel by viewModels()
    private val newAdapter by lazy { CoursesAdapter() }
    private val featuredAdapter by lazy { CoursesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation()
        handleNewRec()
        handleFeaturedRec()
        setupActions()
        sendRequest()
    }

    private fun sendRequest() {
        viewModel.sendRequestCourses()
    }

    private fun handleNewRec() {
        binding.rvNewCourses.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvNewCourses.adapter=newAdapter
    }

    private fun handleFeaturedRec() {
        binding.rvFeaturedCourses.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvFeaturedCourses.adapter=featuredAdapter
    }

    private fun setupActions() {

    }

    private fun observeNavigation() {
        handleSharedFlow(viewModel.coursesFlow,onSuccess = {
            it as Pair<CoursesModel,CoursesModel>
            handleNewCourses(it.first)
            handleFeaturedCourses(it.second)
        })
    }

    private fun handleNewCourses(newCourses: CoursesModel) {
        newAdapter.swapData(newCourses.results ?: listOf())
    }
    private fun handleFeaturedCourses(newCourses: CoursesModel) {
        featuredAdapter.swapData(newCourses.results ?: listOf())
    }

}