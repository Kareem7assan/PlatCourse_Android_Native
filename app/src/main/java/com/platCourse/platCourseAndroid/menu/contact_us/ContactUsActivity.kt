package com.platCourse.platCourseAndroid.menu.contact_us

import androidx.activity.viewModels
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityContactUsBinding
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.model.settings.Social
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.toast

class ContactUsActivity : BaseActivity(R.layout.activity_contact_us) {

    private lateinit var binding: ActivityContactUsBinding
    private val viewModel: MenuViewModel by viewModels()

    override fun init() {
        binding = ActivityContactUsBinding.bind(findViewById(R.id.rootContact))
        handleToolbar()
        sendRequest()
        setupActions()
    }



    private fun setupActions() {

        binding.tvMail.setOnClickListener {
            IntentUtils.openEmailIntent(context = this,emai = binding.tvMail.text.toString())
        }
        binding.tvPhoneNum.setOnClickListener {
            IntentUtils.openWhatsappIntent(context = this,number = binding.tvPhoneNum.text.toString())
        }


        binding.tvFace.setOnClickListener {
            IntentUtils.openFacebookIntent(context = this,binding.tvFace.text.toString())
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