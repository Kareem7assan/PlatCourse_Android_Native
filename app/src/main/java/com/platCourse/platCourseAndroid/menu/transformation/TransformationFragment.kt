package com.platCourse.platCourseAndroid.menu.transformation

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.bank_accounts_model.RecordsItem
import com.rowaad.app.data.model.tweets_model.Tweets
import com.rowaad.app.data.utils.Constants_Api
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.register.RegisterViewModel
import com.platCourse.platCourseAndroid.databinding.FragmentTransformationBinding
import com.platCourse.platCourseAndroid.menu.bank_accounts.BankViewModel
import com.rowaad.utils.PixUtils
import com.rowaad.utils.extention.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class TransformationFragment : BaseFragment(R.layout.fragment_transformation) {

    private var imgPart: MultipartBody.Part?=null
    private val binding by viewBinding<FragmentTransformationBinding>()
    private val viewModel: BankViewModel by viewModels()
    private val REQUSET_CODE=110

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        sendRequestTweets()
        setupAdsObservable()
        setupObservablesValidation()
        setupRequestObservable()
        handleFixedBank()
        setupActions()

    }

    private fun setupRequestObservable() {
        handleSharedFlow(viewModel.transFlow,onSuccess = {
            showSuccessMsg(getString(R.string.trans_success))
        })
    }

    private fun handleFixedBank() {
        binding.etBankName.setText(recordsItem?.bankName)
    }

    private fun setupActions() {
        binding.etCal.onClickShowBirthDatePickerDialog{
            viewModel.transDate=it
        }
        binding.btnUpload.setOnClickListener {
            val options: Options = Options.init()
                    .setRequestCode(REQUSET_CODE) //Request code for activity results
                    .setCount(1) //Number of images to restict selection count
                    .setMode(Options.Mode.Picture) //Option to select only pictures or videos or both
                    .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion

            Pix.start(this, options)

        }
        binding.btnSend.setOnClickListener {
            viewModel.sendRequestTrans(
                    name = binding.etName.getContent(),
                    amount = binding.etAmount.getContent(),
                    bankAccount = recordsItem?.id.toString(),
                    desc = binding.etNotes.getContent(),
                    tweetId = viewModel.selectedTweetId,
                    transferDate = viewModel.transDate,
                    img = imgPart
            ).also {
                //initialized
                viewModel.isValidNameFlow(binding.etName.getContent())
                //watcher
                binding.etName.doOnTextChanged { text, start, before, count ->
                    viewModel.isValidNameFlow(text.toString())
                }}.also {
                //initialized
                viewModel.isValidAmountFlow(binding.etAmount.getContent())
                //watcher
                binding.etAmount.doOnTextChanged { text, start, before, count ->
                    viewModel.isValidAmountFlow(text.toString())
                }}
        }
    }

    private fun setupAdsObservable() {
        handleSharedFlow(viewModel.tweetFlow,onSuccess = {
            val tweets=it as List<Tweets>
            if (tweets.isNotEmpty()){
                setupSpinner(tweets)
            }
        },lifeCycle = Lifecycle.State.CREATED)

    }

    private fun setupSpinner(data: List<Tweets>) {
        setSpinner(data = data.map { it.tweetId.toString()+"-"+ it.title },etLay = binding.etAdNum,indexPos = 0){ selected->
            viewModel.selectedTweetId=selected.split("-")[0]
            Log.e("tweetId",viewModel.selectedTweetId.toString())
        }
    }

    private fun setSpinner(
            data: List<String>,
            etLay: AppCompatAutoCompleteTextView,
            indexPos: Int? = 0,
            callSomeThing: (key: String) -> Unit
    ){
        val keysAdapter= ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
        etLay.setMyAdapter(data, keysAdapter, true){
            callSomeThing(it)
        }

    }

    private fun sendRequestTweets() {
        viewModel.sendRequestTweets()
    }

    private fun setupToolbar() {
        binding.toolbar.ivBack.setOnClickListener {
            closeMe()
        }
        binding.toolbar.tvTitle.text=getString(R.string.transform)
    }

    private fun setupObservablesValidation() {
        observeAmountValidation()
        observeNameValidation()
    }

    private fun observeNameValidation() {
        handleFlow(viewModel.isValidNameFlow){isName->
            if (isName is RegisterViewModel.Validation.IsValid){
                binding.etName.validateText(isName.isValid, null, binding.tvErrorName)
            }
        }
    }

    private fun observeAmountValidation() {
        handleFlow(viewModel.isValidAmountFlow){isAmount->
            if (isAmount is RegisterViewModel.Validation.IsValid){
                binding.etAmount.validateText(isAmount.isValid, null, binding.tvErrorAmount)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUSET_CODE && resultCode == Activity.RESULT_OK ){
            val returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)?.first()!!
            imgPart= PixUtils.convertImgToFilePix("image", returnValue)
            Log.e("img_path",returnValue)
            binding.tvImg.text=returnValue
        }
    }

    companion object{
        private var recordsItem: RecordsItem? = null

        fun newInstance(recordsItem: RecordsItem): TransformationFragment {
            val args = Bundle()
            val fragment = TransformationFragment()
            this.recordsItem=recordsItem
            fragment.arguments = args
            return fragment
        }
    }
}