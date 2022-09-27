package com.platCourse.platCourseAndroid.search

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.widget.textChanges
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentMyCoursesBinding
import com.platCourse.platCourseAndroid.databinding.FragmentSearchBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.platCourse.platCourseAndroid.home.courses.adapter.CoursesAdapter
import com.platCourse.platCourseAndroid.home.my_courses.MyCoursesViewModel
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.utils.extention.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private var binding: FragmentSearchBinding? = null
    private var pageNumber: Int = 1
    private var next:String?=null

    private val viewModel: CoursesViewModel by viewModels()
    private val coursesAdapter by lazy { CoursesAdapter(true) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentSearchBinding.bind(view)
        handlePage()
        handleRec()
        handleSearch()
        observeMyCourses()
        setupAction()

    }

    private fun setupAction() {
        binding?.searchBar?.ivBack?.setOnClickListener {
            if (binding?.searchBar?.etSearch?.text?.isEmpty()==true)
                findNavController().navigateUp()
            else
                binding?.searchBar?.etSearch?.clearTxt()
        }

        binding?.searchBar?.ivClear?.setOnClickListener {
            binding?.searchBar?.etSearch?.clearTxt()
        }

        coursesAdapter.onClickItem=::onClickItem
        coursesAdapter.onClickItemProfile=::onClickItemProfile
    }

    private fun onClickItemProfile(courseItem: CourseItem, pos: Int) {
        findNavController().navigate(R.id.action_global_profileTeacherFragment,
                bundleOf(
                        "details"
                                to
                                courseItem.toJson()
                )
        )
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

    private fun handleSearch() {
        binding?.searchBar?.etSearch?.textChanges()
                ?.skipInitialValue()
                ?.debounce(1,TimeUnit.SECONDS)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.map {
                    if (it.isEmpty())
                        binding?.searchBar?.ivClear?.hide()
                    else
                        binding?.searchBar?.ivClear?.show()
                    it
                }

                ?.subscribe {
                    if (it.isNotEmpty()) clearData().also{sendRequestSearchCourses(binding?.searchBar?.etSearch?.text.toString())}
                }
    }

    private fun clearData() {
        coursesAdapter.clear()
        pageNumber=1
    }

    private fun handleRec() {
        binding?.rvCourses?.layoutManager= LinearLayoutManager(requireContext())
        binding?.rvCourses?.adapter=coursesAdapter
    }

    private fun showAnonymousDialog() {
        (requireActivity() as BaseActivity).showVisitorDialog(binding?.scrollView!!){}
    }

    private fun observeMyCourses() {
        handleSharedFlow(viewModel.coursesFlow,onShowProgress = {
            if (pageNumber>1) showBottomProgress() /*else showProgress()*/
        },onHideProgress = {
            if (pageNumber>1) hideBottomProgress() /*else hideProgress()*/

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
        binding?.rvCourses?.show()
        binding?.emptyLay?.root?.hide()

    }

    private fun showEmptyCourses() {
        binding?.rvCourses?.hide()
        binding?.emptyLay?.root?.show()
    }

    private fun showBottomProgress() {
        binding?.progressMore?.show()
    }

    private fun hideBottomProgress() {
        binding?.progressMore?.hide()
    }

    private fun handlePage() {
        binding?.scrollView?.handlePagination {
            pageNumber++
            if (hasNext()) sendRequestSearchCourses(binding?.searchBar?.etSearch?.text.toString())

        }
    }

    private fun sendRequestSearchCourses(key:String) {
        viewModel.sendSearchCourses(key,pageNumber)
    }

    private fun hasNext(): Boolean {
        return next!=null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
        pageNumber=1
        coursesAdapter.clear()
    }
}