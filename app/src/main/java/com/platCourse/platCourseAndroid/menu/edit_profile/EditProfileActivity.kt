package com.platCourse.platCourseAndroid.menu.edit_profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.model.ImageModel
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.utils.Constants_Api
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.register.RegisterViewModel
import com.platCourse.platCourseAndroid.databinding.ActivityEditProfileBinding
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.rowaad.utils.BitmapUtils
import com.rowaad.utils.PixUtils
import com.rowaad.utils.extention.getContent
import com.rowaad.utils.extention.loadImage
import com.rowaad.utils.extention.toast
import com.rowaad.utils.extention.validateText
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EditProfileActivity : BaseActivity(R.layout.activity_edit_profile) {

    private var avatar: MultipartBody.Part? = null
    private var cover: MultipartBody.Part? = null
    private var binding: ActivityEditProfileBinding? = null
    private val viewModel: MenuViewModel by viewModels()
    private val validationViewModel: RegisterViewModel by viewModels()

    override fun init() {
        sendRequestProfile()
        binding=ActivityEditProfileBinding.bind(findViewById(R.id.rootView))
        setupActions()
        handleData(viewModel.getUser()!!)
        handleProfileObservable()
        handleValidations()
        handleEditObservable()

    }

    private fun handleEditObservable() {
        handleSharedFlow(viewModel.editProfileFlow,onSuccess = { it as UserModel
            showSuccessMsg(getString(R.string.update_profile)).also { onBackPressed() }
        })
    }

    private fun handleValidations() {
        observeNameValidation()
        observePhoneValidation()
        observeEmailValidation()
    }
    private fun observeNameValidation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                validationViewModel.isValidNameFlow.collect { isName->
                    if (isName is RegisterViewModel.Validation.IsValid){
                        binding?.etFname?.validateText(isName.isValid, binding?.ivMarkName, binding?.tvErrorFName)
                    }
                }
            }
        }
    }

    private fun observePhoneValidation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                validationViewModel.isValidPhoneFlow.collect { isPhone->
                    if (isPhone is RegisterViewModel.Validation.IsValid){
                        binding?.etPhone?.validateText(isPhone.isValid, binding?.ivMarkPhone, binding?.tvErrorPhone)
                    }
                }
            }
        }
    }

    private fun observeEmailValidation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                validationViewModel.isValidEmailFlow.collect { isMail->
                    if (isMail is RegisterViewModel.Validation.IsValid){
                        binding?.etEmail?.validateText(isMail.isValid, binding?.ivMarkEmail, binding?.tvErrorMail)
                    }
                }
            }
        }
    }


    private fun setupActions() {
        binding?.ivEditAvatar?.setOnClickListener {
            handleAvatar()
        }
        binding?.ivEditBg?.setOnClickListener {
            handleCover()
        }
        binding?.editBtn?.setOnClickListener {
            sendRequestEdit()
        }
        binding?.ivBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun sendRequestEdit() {
        viewModel.editProfile(name = binding?.etFname!!.getContent(),
            userName = binding?.etUsername?.getContent(),phone = binding?.etPhone!!.getContent(),
            email = binding?.etEmail!!.getContent(),bio = binding?.etBio?.getContent(),
            avatar = avatar , cover = cover
        ).also {
            //initialized
            validationViewModel.isValidNameFlow(binding?.etFname!!.getContent())
            //watcher
            binding?.etFname!!.doOnTextChanged { text, start, before, count ->
                validationViewModel.isValidNameFlow(text.toString())
            }
        }.also {
            //initialized
            validationViewModel.isValidPhoneFlow(binding?.etPhone!!.getContent())
            //watcher
            binding?.etPhone!!.doOnTextChanged { text, start, before, count ->
                validationViewModel.isValidPhoneFlow(text.toString())
            }

        }.also {
            //initialized
            validationViewModel.isValidMailFlow(binding?.etEmail!!.getContent())
            //watcher
            binding?.etEmail!!.doOnTextChanged { text, start, before, count ->
                validationViewModel.isValidMailFlow(text.toString())
            }}
    }

    private fun handleAvatar() {
        val options: Options = Options.init()
            .setRequestCode(Constants_Api.CODES.AVATAR_CODE) //Request code for activity results
            .setCount(1) //Number of images to restict selection count
            .setMode(Options.Mode.Picture) //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion

        Pix.start(this, options)

    }
    private fun handleCover() {
        val options: Options = Options.init()
            .setRequestCode(Constants_Api.CODES.COVER_CODE) //Request code for activity results
            .setCount(1) //Number of images to restict selection count
            .setMode(Options.Mode.Picture) //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion

        Pix.start(this, options)

    }

    private fun sendRequestProfile() {
        viewModel.myProfile()
    }

    private fun handleProfileObservable(){
        handleStateFlow(viewModel.myProfileFlow,onSuccess = {it as UserModel
            handleData(it)
        })
    }

    private fun handleData(user: UserModel) {
        with(binding!!){
            etUsername.setText(user.username)
            etFname.setText(user.name)
            etBio.setText(user.bio)
            etEmail.setText(user.email)
            etPhone.setText(user.phone_number)
            //if (user.image.isNullOrEmpty().not()) ivAvatar.loadImage(user.image)
            //if (user.header.isNullOrEmpty().not())ivCover.loadImage(user.header)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants_Api.CODES.AVATAR_CODE) {
            val avatarPath = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)?.get(0)
            val imgName = "image"
            val avatarPart = PixUtils.convertImgToFilePix(imgName, avatarPath)
            avatar=avatarPart
            binding!!.ivAvatar.setImageBitmap(BitmapUtils.modifyImgOrientation(avatarPath!!))
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == Constants_Api.CODES.COVER_CODE) {
            val coverPath =
                data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)?.get(0)
            val imgName = "header"
            val coverPart = PixUtils.convertImgToFilePix(imgName, coverPath)
            cover=coverPart
            binding!!.ivCover.setImageBitmap(BitmapUtils.modifyImgOrientation(coverPath!!))

        }
        else{
            Log.e("hyp", "some thing,$requestCode")
        }
    }
}