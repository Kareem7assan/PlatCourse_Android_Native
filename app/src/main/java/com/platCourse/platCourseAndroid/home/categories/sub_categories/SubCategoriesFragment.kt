package com.platCourse.platCourseAndroid.home.categories.sub_categories

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentCategoriesBinding
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.platCourse.platCourseAndroid.home.categories.CategoriesViewModel
import com.platCourse.platCourseAndroid.home.categories.adapter.CatsAdapter
import com.platCourse.platCourseAndroid.home.categories.adapter.SubCatsAdapter
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.categories_model.CategoriesItem
import com.rowaad.app.data.model.categories_model.SubCategory
import com.rowaad.utils.extention.fromJson

class SubCategoriesFragment : BaseFragment(R.layout.fragment_categories) {
    private var cat: CategoriesItem?=null
    private val binding by viewBinding<FragmentCategoriesBinding>()
    private val catsAdapter by lazy { SubCatsAdapter() }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleRec()
        cat=arguments?.getString("sub_cat")?.fromJson<CategoriesItem>()
        setupTitle(cat?.category_name)
        setData(cat?.sub_category)
        setupActions()
    }

    private fun setupActions() {
        catsAdapter.onClickItem=::onClickCat
    }

    private fun onClickCat(subCategory: SubCategory, i: Int) {
        findNavController().navigate(R.id.action_global_CoursesBaseCategoriesFragment,
            bundleOf(
                "cat"
                to
                  cat?.id  ,
                "cat_name"
                to
                  cat?.category_name  ,
                "sub_cat"
                to
                  subCategory?.id  ,
                "sub_cat_name"
                to
                  subCategory?.sub_category_name

            )
            )

    }

    private fun setupTitle(categoryName: String?) {
        (requireActivity() as HomeActivity).setupTitleSubCat(categoryName!!)
    }

    private fun setData(data: List<SubCategory>?) {
        catsAdapter.swapData(data = data!!)
    }





    private fun handleRec() {
        binding.rvCategories.layoutManager= LinearLayoutManager(requireContext())
        binding.rvCategories.adapter=catsAdapter

    }
}