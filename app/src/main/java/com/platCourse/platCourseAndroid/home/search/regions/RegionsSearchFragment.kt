package com.platCourse.platCourseAndroid.home.search.regions

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.tweets_model.Region
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentRegionsBinding
import com.platCourse.platCourseAndroid.home.regions.adapter.RegionsAdapter
import com.platCourse.platCourseAndroid.home.search.viewmodel.SearchViewModel
import com.platCourse.platCourseAndroid.home.time_line.viewmodel.TimeLineViewModel
import com.rowaad.utils.extention.disable
import com.rowaad.utils.extention.enable
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.textColorResource

class RegionsSearchFragment : BaseFragment(R.layout.fragment_regions) {

    private  var regions: List<Region> = listOf()
    //private val viewModel: TimeLineViewModel by activityViewModels()
    private val viewModel: SearchViewModel by activityViewModels()

    private val binding by viewBinding<FragmentRegionsBinding>()
    private val adapter by lazy { RegionsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRec()
        setTitles()
        sendRequestRegions()
        setupActions()
        observeRegions()
        handleBtnSave()
    }

    private fun setTitles() {
        binding.etRegion.hint=getString(R.string.search_region_name)
        binding.tvRegion.text = getString(R.string.select_region)
    }

    private fun handleBtnSave() {
        if (viewModel.selectedRegions.isNullOrEmpty()){
            disableButton()
        }
        else{
            enableButton()
        }
    }

    private fun observeRegions() {
        handleSharedFlow(viewModel.regionsFlow,onSuccess = {it as List<Region>
            regions=it
            handleRegions(it)
        })
    }

    private fun setupActions() {
        adapter.onClickItem=::onClickItem
        binding.etRegion.doOnTextChanged { text, start, before, count ->
            if (count>=1) adapter.swapData(viewModel.handleSearch(text.toString(),regions))
            else adapter.swapData(regions)
        }
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.saveBtn.setOnClickListener {
            if (viewModel.selectedRegions.size==1){
                findNavController().navigate(R.id.action_regionsSearchFragment_to_citiesSearchFragment)
            }
            else{
                findNavController().navigateUp()
            }
        }
    }

    private fun onClickItem(region: Region, pos: Int,isChecked:Boolean) {
        if (isChecked)
            viewModel.selectedRegions.add(region).also {enableButton() }
        else
            viewModel.selectedRegions.remove(viewModel.selectedRegions.find { it.id==region.id }).also { disableButton() }
    }

    private fun setupRec() {
        binding.rvRegions.layoutManager=LinearLayoutManager(requireContext())
        binding.rvRegions.adapter=adapter
    }

    private fun sendRequestRegions() {
        viewModel.sendRequestRegions()

    }

    private fun enableButton(){
        binding.saveBtn.enable().also { binding.saveBtn.backgroundDrawable=ContextCompat.getDrawable(requireContext(),R.drawable.grad_background_btn) }.also { binding.saveBtn.textColorResource=R.color.white }
    }

    private fun disableButton(){
        if (viewModel.selectedRegions.isEmpty()) binding.saveBtn.disable().also { binding.saveBtn.backgroundDrawable=ContextCompat.getDrawable(requireContext(),R.drawable.disable_background_btn) }.also { binding.saveBtn.textColorResource=R.color.very_grey_color_text }
    }

    private fun handleRegions(regions: List<Region>) {
        adapter.swapData(regions.map {itemRegion->
            if (viewModel.selectedRegions.any { it.id==itemRegion.id })
                viewModel.selectedRegions.find { it.id==itemRegion.id }!!.copy(isChecked = true)
            else
                itemRegion
        }

        )
    }

}