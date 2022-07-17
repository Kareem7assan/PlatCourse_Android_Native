package com.platCourse.platCourseAndroid.home.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.widget.textChanges
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentSearchBinding
import com.platCourse.platCourseAndroid.home.time_line.viewmodel.TimeLineViewModel
import com.rowaad.utils.extention.clearTxt
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onEditorAction
import java.util.concurrent.TimeUnit

class PreSearchFragment : BaseFragment(R.layout.fragment_search) {


    private var binding: FragmentSearchBinding?=null
    private val viewModel: TimeLineViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentSearchBinding.bind(view)
        handleSearch()
        handleToolbar()
        handleData()
    }

    private fun handleData() {
        binding?.rvSubCat?.hide()
        binding?.view?.hide()
        binding?.tabLay?.hide()
        binding?.nearByLay?.hide()
        binding?.regionsLay?.hide()
        binding?.rvTweets?.hide()
        binding?.showFollowersTweets?.hide()
        binding?.searchBriefLay?.hide()
    }

    private fun handleToolbar() {
        binding?.searchBar?.ivBack?.onClick {
            findNavController().popBackStack()
        }
    }

    private fun handleSearch() {

        //handle search
        binding?.searchBar?.ivClear?.setOnClickListener {
            binding?.searchBar?.etSearch?.clearTxt()
        }
        binding?.searchBar!!.etSearch
            .textChanges()
            .skipInitialValue()
            .debounce(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {key->
                if (key.isEmpty()) {
                    uiClearState()
                }
                else {
                    uiSearchState(key.toString())
                }
            }
        binding?.searchBar?.ivSearchEnd?.setOnClickListener {
            handleNavigation()
        }

        binding?.searchBar?.ivSearchStart?.setOnClickListener {
            handleNavigation()
        }
        binding?.searchBar?.etSearch?.onEditorAction { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                handleNavigation()
            }
        }

        binding?.searchBar?.ivClear?.setOnClickListener {
            binding?.searchBar?.etSearch?.clearTxt()
        }

    }



    private fun handleNavigation() {
        findNavController().navigate(R.id.action_global_searchFragment
            ,
            bundleOf(
                      "word"
                        to
                      binding?.searchBar?.etSearch?.text.toString()
            )
        )
    }

    private fun uiClearState() {
        binding?.searchBar?.ivSearchStart?.show()
        binding?.searchBar?.ivSearchEnd?.hide()
        binding?.searchBar?.etSearch?.hint=getString(R.string.search_hint)
        binding?.searchBar?.ivClear?.hide()

    }

    private fun uiSearchState(key: String) {
        binding?.searchBar?.ivSearchStart?.hide()
        binding?.searchBar?.ivSearchEnd?.show()
        binding?.searchBar?.etSearch?.setText(key)
        binding?.searchBar?.ivClear?.show()

        binding?.searchBar?.etSearch?.setSelection(binding?.searchBar?.etSearch?.length() ?: 0)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null


    }

}