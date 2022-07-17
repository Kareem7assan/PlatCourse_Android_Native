package com.platCourse.platCourseAndroid.menu.contact_us

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.settings.SettingsModel
import com.rowaad.app.data.model.settings.Social
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.login.LoginViewModel
import com.platCourse.platCourseAndroid.auth.register.RegisterViewModel
import com.platCourse.platCourseAndroid.databinding.ActivityContactUsBinding
import com.platCourse.platCourseAndroid.databinding.FragmentAdvertisePackageBinding
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.getContent
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import com.rowaad.utils.extention.validateText
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactUsActivity : BaseActivity(R.layout.activity_contact_us) {

    private var social: Social?=null
    private lateinit var binding: ActivityContactUsBinding
    private val viewModel: MenuViewModel by viewModels()

    override fun init() {
        binding = ActivityContactUsBinding.bind(findViewById(R.id.rootView))
        handleToolbar()
        sendRequest()
        setupActions()
        setupUserData()
        setupObservables()
        observeNameValidation()
        observeEmailValidation()
        observeTitleValidation()
        observeBodyValidation()
    }

    private fun setupUserData() {
        binding.etFname.setText(viewModel.getUser()?.name)
        binding.etEmail.setText(viewModel.getUser()?.email)
    }

    private fun setupActions() {
        binding.ivFace.setOnClickListener {
            IntentUtils.openFacebookIntent(uri = social?.whatsapp ?: "",context = this)
        }
        binding.ivTwitter.setOnClickListener {
            IntentUtils.openTwitterIntent(this,social?.twitter ?: "")
        }
        binding.ivInsta.setOnClickListener {
            IntentUtils.openInstagramIntent(this,social?.instagram ?: "")
        }
        binding.ivWhats.setOnClickListener {
            IntentUtils.openWhatsappIntent(social?.whatsapp ?: "",this)

        }
        binding.ivSnap.setOnClickListener {
            IntentUtils.openSnapAppIntent(snapchatId = social?.snapchat ?: "",context = this)

        }
        binding.ivYoutube.setOnClickListener {
            IntentUtils.openUrl(url = social?.youtube ?: "",context = this)
        }
        binding.sendBtn.setOnClickListener {
            viewModel.contactUsPost(name = binding.etFname.getContent(),email = binding.etEmail.getContent(),
                        subject = binding.etTitle.getContent(),msg = binding.etBody.getContent()
                    ).also {
                //initialized
                viewModel.isValidNameFlow(binding.etFname.getContent())
                //watcher
                binding.etFname.doOnTextChanged { text, start, before, count ->
                    viewModel.isValidNameFlow(text.toString())
                }}.also {
                //initialized
                viewModel.isValidMailFlow(binding.etEmail.getContent())
                //watcher
                binding.etEmail.doOnTextChanged { text, start, before, count ->
                    viewModel.isValidMailFlow(text.toString())
                }}.also {
                //initialized
                viewModel.isValidTitleFlow(binding.etTitle.getContent())
                //watcher
                binding.etTitle.doOnTextChanged { text, start, before, count ->
                    viewModel.isValidTitleFlow(text.toString())
                }}.also {
                //initialized
                viewModel.isValidBodyFlow(binding.etBody.getContent())
                //watcher
                binding.etBody.doOnTextChanged { text, start, before, count ->
                    viewModel.isValidBodyFlow(text.toString())
                }}
        }
    }

    private fun observeNameValidation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isValidNameFlow.collect { isName->
                    if (isName is RegisterViewModel.Validation.IsValid){
                        binding.etFname.validateText(isName.isValid, binding.ivMarkName, binding.tvErrorFName)
                    }
                }
            }
        }
    }

    private fun observeEmailValidation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isValidEmailFlow.collect { isMail->
                    if (isMail is RegisterViewModel.Validation.IsValid){
                        binding.etEmail.validateText(isMail.isValid, binding.ivMarkEmail, binding.tvErrorMail)
                    }
                }
            }
        }
    }

    private fun observeTitleValidation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isValidTitleFlow.collect { isTitle->
                    if (isTitle is RegisterViewModel.Validation.IsValid){
                        binding.etTitle.validateText(isTitle.isValid, null, binding.tvErrorTitle)
                    }
                }
            }
        }
    }

    private fun observeBodyValidation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isValidBodyFlow.collect { isBody->
                    if (isBody is RegisterViewModel.Validation.IsValid){
                        binding.etBody.validateText(isBody.isValid, null, binding.tvErrorBody)
                    }
                }
            }
        }
    }

    private fun setupObservables() {
        handleSharedFlow(viewModel.contactsFlow,onSuccess = {it as SettingsModel
          handleSocial(it)
        })

        handleSharedFlow(viewModel.contactsPostFlow,onSuccess = {
            showSuccessMsg(getString(R.string.msg_sent_success)).also { onBackPressed() }
        })


    }


    private fun handleSocial(settings: SettingsModel) {
        social=settings.social
        if (settings.social?.facebook.isNullOrBlank()){
            binding.ivFace.hide()
        }
        else{
            binding.ivFace.show()
        }
        if (settings.social?.twitter.isNullOrBlank()){
            binding.ivTwitter.hide()
        }
        else{
            binding.ivTwitter.show()
        }
        if (settings.social?.instagram.isNullOrBlank()){
            binding.ivInsta.hide()
        }
        else{
            binding.ivInsta.show()
        }
        if (settings.social?.whatsapp.isNullOrBlank()){
            binding.ivWhats.hide()
        }
        else{
            binding.ivWhats.show()
        }
        if (settings.social?.snapchat.isNullOrBlank()){
            binding.ivSnap.hide()
        }
        else{
            binding.ivSnap.show()
        }
        if (settings.social?.youtube.isNullOrBlank()){
            binding.ivYoutube.hide()
        }
        else{
            binding.ivYoutube.show()
        }
    }

    private fun sendRequest() {
        viewModel.contactUsContacts()
    }

    private fun handleToolbar() {
        binding.toolbar.tvTitle.text=getString(R.string.contact_us)
        binding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }


}