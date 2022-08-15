package com.platCourse.platCourseAndroid.home.courses

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentCoursesBinding
import com.platCourse.platCourseAndroid.home.courses.adapter.CoursesAdapter
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import com.rowaad.utils.extention.toJson

class CoursesFragment : BaseFragment(R.layout.fragment_courses) {


    private val binding by viewBinding<FragmentCoursesBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()
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
        binding.tvMoreFeatured.setOnClickListener {
            findNavController().navigate(R.id.action_global_featuredCoursesFragment)
        }
        binding.tvMoreNew.setOnClickListener {
            findNavController().navigate(R.id.action_global_newCoursesFragment)
        }
        newAdapter.onClickItem=::onClickCourse
        featuredAdapter.onClickItem=::onClickCourse
    }

    private fun onClickCourse(courseItem: CourseItem, pos: Int) {
        findNavController().navigate(R.id.action_global_courseDetailsFragment,
                bundleOf(
                    "details"
                        to
                    courseItem.toJson()
                )
            )
    }

    private fun observeNavigation() {
        handleSharedFlow(viewModel.coursesFlow,onSuccess = {
            it as Pair<CoursesModel,CoursesModel>
            handleNewCourses(it.first)
            handleFeaturedCourses(it.second)
        },onShowProgress = {
            showProgress().also { binding.groupTitles.hide() }
        },onHideProgress = {
            hideProgress().also { binding.groupTitles.show() }
        })
    }

    private fun handleNewCourses(newCourses: CoursesModel) {
        newAdapter.swapData(newCourses.results ?: listOf())
    }
    private fun handleFeaturedCourses(newCourses: CoursesModel) {
        featuredAdapter.swapData(newCourses.results ?: listOf())
    }

}