package com.platCourse.platCourseAndroid.home.categories

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentCategoriesBinding
import com.platCourse.platCourseAndroid.databinding.FragmentCoursesBinding
import com.platCourse.platCourseAndroid.home.categories.adapter.CatsAdapter
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.platCourse.platCourseAndroid.home.courses.adapter.CoursesAdapter
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.categories_model.CategoriesItem
import com.rowaad.utils.extention.toJson

class CategoriesFragment : BaseFragment(R.layout.fragment_categories) {

    private val binding by viewBinding<FragmentCategoriesBinding>()
    private val viewModel: CategoriesViewModel by viewModels()
    private val catsAdapter by lazy { CatsAdapter() }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleRec()
        observeCategories()
        sendRequest()
        setupActions()
    }

    private fun setupActions() {
        catsAdapter.onClickItem=::onClickItem
    }

    private fun onClickItem(categoriesItem: CategoriesItem, pos: Int) {
        findNavController().navigate(R.id.action_homeFragment_to_subCatFragment,
                    bundleOf(
                                    "sub_cat"
                                    to
                                    categoriesItem.toJson()

                    )
                )
    }

    private fun observeCategories() {
        handleSharedFlow(viewModel.categoriesFlow,onSuccess = {it as List<CategoriesItem>
            if (it.isNotEmpty()) catsAdapter.swapData(it)
        })
    }

    private fun sendRequest() {
        viewModel.getCategories()
    }

    private fun handleRec() {
        binding.rvCategories.layoutManager=LinearLayoutManager(requireContext())
        binding.rvCategories.adapter=catsAdapter

    }
}