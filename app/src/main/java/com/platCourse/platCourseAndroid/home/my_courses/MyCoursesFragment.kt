package com.platCourse.platCourseAndroid.home.my_courses

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentCoursesBinding
import com.platCourse.platCourseAndroid.databinding.FragmentMyCoursesBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.platCourse.platCourseAndroid.home.courses.adapter.CoursesAdapter
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.utils.extention.*

class MyCoursesFragment : BaseFragment(R.layout.fragment_my_courses) {

    private var pageNumber: Int = 1
    private var next:String?=null
    private val binding by viewBinding<FragmentMyCoursesBinding>()
    private val viewModel: MyCoursesViewModel by viewModels()
    private val coursesAdapter by lazy { CoursesAdapter(true) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlePage()
        handleRec()
        if (viewModel.isLogin) sendRequestMyCourses()
        else showAnonymousDialog()
        observeMyCourses()
        setupActions()

    }

    private fun setupActions() {
        coursesAdapter.onClickItem=::onClickItem
    }

    private fun onClickItem(courseItem: CourseItem, pos: Int) {
        findNavController().navigate(R.id.action_global_courseDetailsFragment,
            bundleOf(
                "details"
                        to
                        courseItem.toJson()
            )
        )
    }

    private fun handleRec() {
        binding.rvCourses.layoutManager=LinearLayoutManager(requireContext())
        binding.rvCourses.adapter=coursesAdapter
    }

    private fun showAnonymousDialog() {
        (requireActivity() as BaseActivity).showVisitorDialog(binding.scrollView){}
    }

    private fun observeMyCourses() {
        handleSharedFlow(viewModel.myCoursesFlow,onShowProgress = {
            if (pageNumber>1) showBottomProgress() else showProgress()
        },onHideProgress = {
            if (pageNumber>1) hideBottomProgress() else hideProgress()

        } ,onSuccess = {it as CoursesModel
            next=it.next
            if (pageNumber==1 && it.results.isNullOrEmpty()){
                showEmptyCourses()
            }
            else {
                showCourses()
                coursesAdapter.addData(it.results!!)
            }
        })
    }

    private fun showCourses() {
        binding.rvCourses.show()
        binding.emptyLay.root.hide()

    }

    private fun showEmptyCourses() {
        binding.rvCourses.hide()
        binding.emptyLay.root.show()
    }

    private fun showBottomProgress() {
        binding.progressMore.show()
    }

    private fun hideBottomProgress() {
        binding.progressMore.hide()
    }

    private fun handlePage() {
        binding.scrollView.handlePagination {
            pageNumber++
            if (hasNext()) sendRequestMyCourses()

        }
    }

    private fun sendRequestMyCourses() {
        viewModel.getMyCourses(pageNumber)
    }

    private fun hasNext(): Boolean {
        return next!=null
    }
}