package com.platCourse.platCourseAndroid.home.search.regions

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.tweets_model.City
import com.rowaad.app.data.model.tweets_model.Region
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentRegionsBinding
import com.platCourse.platCourseAndroid.home.regions.adapter.CitiesAdapter
import com.platCourse.platCourseAndroid.home.regions.adapter.RegionsAdapter
import com.platCourse.platCourseAndroid.home.search.viewmodel.SearchViewModel
import com.platCourse.platCourseAndroid.home.time_line.viewmodel.TimeLineViewModel
import com.rowaad.utils.extention.disable
import com.rowaad.utils.extention.enable
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.textColorResource

class CitiesSearchFragment : BaseFragment(R.layout.fragment_regions) {
    private  var cities: List<City> = listOf()
    private val viewModel: SearchViewModel by activityViewModels()
    private val binding by viewBinding<FragmentRegionsBinding>()
    private val adapter by lazy { CitiesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRec()
        getCities()
        setupActions()
        handleBtnSave()
        setTitles()
    }


    private fun setTitles() {
        binding.etRegion.hint=getString(R.string.select_city)
        binding.tvRegion.text = getString(R.string.search_city)
    }

    private fun handleBtnSave() {
        if (viewModel.selectedCities.isNullOrEmpty()){
            disableButton()
        }
        else{
            enableButton()
        }
    }

   

    private fun setupActions() {
        adapter.onClickItem = ::onClickItem
        binding.etRegion.doOnTextChanged { text, start, before, count ->
            if (count >= 1) adapter.swapData(viewModel.handleSearchCity(text.toString(), cities))
            else adapter.swapData(cities)
        }
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveBtn.setOnClickListener {
            findNavController().navigate(R.id.action_citiesSearchFragment_to_searchFragment)
        }
    }

    private fun onClickItem(city: City, pos: Int, isChecked:Boolean) {
        if (isChecked)
            viewModel.selectedCities.add(city).also {enableButton() }
        else
            viewModel.selectedCities.remove(viewModel.selectedCities.find { it.id==city.id }).also { disableButton() }
    }

    private fun setupRec() {
        binding.rvRegions.layoutManager= LinearLayoutManager(requireContext())
        binding.rvRegions.adapter=adapter
    }

    private fun getCities() {
        cities=viewModel.selectedRegions.first().cities ?: mutableListOf()
        handleCities(cities)

    }

    private fun enableButton(){
        binding.saveBtn.enable().also { binding.saveBtn.backgroundDrawable= ContextCompat.getDrawable(requireContext(),R.drawable.grad_background_btn) }.also { binding.saveBtn.textColorResource=R.color.white }
    }

    private fun disableButton(){
        if (viewModel.selectedCities.isEmpty()) binding.saveBtn.disable().also { binding.saveBtn.backgroundDrawable= ContextCompat.getDrawable(requireContext(),R.drawable.disable_background_btn) }.also { binding.saveBtn.textColorResource=R.color.very_grey_color_text }
    }

    private fun handleCities(cities: List<City>) {
        adapter.swapData(cities.map {itemCity->
            if (viewModel.selectedCities.any { it.id==itemCity.id })
                viewModel.selectedCities.find { it.id==itemCity.id }!!.copy(isChecked = true)
            else
                itemCity
        }

        )
    }
}