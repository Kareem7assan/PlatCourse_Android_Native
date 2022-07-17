package com.platCourse.platCourseAndroid.menu.bank_accounts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.bank_accounts_model.RecordsItem
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentBankAccountsBinding
import com.platCourse.platCourseAndroid.menu.bank_accounts.adapter.BankAccountAdapter
import com.platCourse.platCourseAndroid.menu.transformation.TransformationFragment
import com.rowaad.utils.extention.closeMe
import com.rowaad.utils.extention.replaceFragmentToActivity

class BankAccountsFragment : BaseFragment(R.layout.fragment_bank_accounts) {

     private val bankAdapter by lazy { BankAccountAdapter() }
    private val binding by viewBinding<FragmentBankAccountsBinding>()
    private val viewModel:BankViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRec()
        sendRequest()
        setupToolbar()
        handleObservableBanks()
    }

    private fun handleObservableBanks(){
        handleSharedFlow(viewModel.banksFlow,onSuccess = {
            val banks=it as List<RecordsItem>
            bankAdapter.swapData(banks)
        })
    }

    private fun setupToolbar() {
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.tvTitle.text=getString(R.string.commission)
    }

    private fun sendRequest() {
        viewModel.sendRequestBanks()

    }

    private fun setupRec() {
        binding.rvBanks.layoutManager=LinearLayoutManager(requireContext())
        binding.rvBanks.adapter=bankAdapter
        bankAdapter.onClickItem=::onClickItem
    }

    private fun onClickItem(recordsItem: RecordsItem, pos: Int) {
        requireActivity().replaceFragmentToActivity(TransformationFragment.newInstance(recordsItem))
    }

    companion object{
        fun newInstance(): BankAccountsFragment {
            val args = Bundle()
            val fragment = BankAccountsFragment()
            fragment.arguments = args
            return fragment
        }
    }

}