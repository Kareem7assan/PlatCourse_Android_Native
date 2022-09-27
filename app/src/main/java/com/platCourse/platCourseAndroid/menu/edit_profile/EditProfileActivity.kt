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
import com.rowaad.utils.extention.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.sdk27.coroutines.onClick

class EditProfileActivity : BaseActivity(R.layout.activity_edit_profile) {

    private var avatar: MultipartBody.Part? = null
    private var cover: MultipartBody.Part? = null
    private var binding: ActivityEditProfileBinding? = null
    private val viewModel: MenuViewModel by viewModels()

    override fun init() {
        sendRequestProfile()
        binding=ActivityEditProfileBinding.bind(findViewById(R.id.rootView))
        setupActions()
        handleData(viewModel.getUser()!!)
        handleProfileObservable()
        handleEditObservable()
        handleToolbar()

    }

    private fun handleToolbar() {
        binding?.toolbar?.tvTitle?.text=getString(R.string.profile)
        binding?.toolbar?.ivBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupActions() {
        binding?.ivEditAvatar?.onClick {
            handleAvatar()
        }
        binding?.transformBtn?.onClick {
            sendRequestAvatar()
        }
    }

    private fun handleEditObservable() {
        handleSharedFlow(viewModel.editProfileFlow,onSuccess = { it as UserModel
            showSuccessMsg(getString(R.string.update_profile))
        })
    }


    private fun handleAvatar() {
        val options: Options = Options.init()
            .setRequestCode(Constants_Api.CODES.AVATAR_CODE) //Request code for activity results
            .setCount(1) //Number of images to restict selection count
            .setMode(Options.Mode.Picture) //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion

        Pix.start(this, options)
    }


    private fun sendRequestProfile() {
        viewModel.myProfile()
    }

    private fun sendRequestAvatar(){
        viewModel.editProfile(avatar)
    }

    private fun sendRequestAvatar(body: HashMap<String,RequestBody>){
       // viewModel.editProfile(body)
    }
    private fun sendRequestAvatar(body: RequestBody){
        viewModel.editProfileBody(body)
    }

    private fun handleProfileObservable(){
        handleSharedFlow(viewModel.myProfileFlow,onSuccess = {it as UserModel
            handleData(it)
        })
    }


    private fun handleData(user: UserModel) {
        with(binding!!){
            tvUserName.text = user.username
            tvName.text = user.name
            tvCountry.text = user.country
            tvCity.text = user.city
            tvEmail.text = user.email
            tvPhone.text = user.phone_number
            if (user.profile_image.isNullOrEmpty().not()) ivAvatar.loadImage(user.profile_image)

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
            val imgName = "profile_image"
            val avatarPart = PixUtils.convertImgToFilePix(imgName, avatarPath)
            avatar=avatarPart
            binding!!.ivAvatar.setImageBitmap(BitmapUtils.modifyImgOrientation(avatarPath!!))

            val build=PixUtils.convertImgToFile(imgName, avatarPath)
            binding?.transformBtn?.show()
            //sendRequestAvatar(PixUtils.convertImgToFile(imgName, avatarPath))
        }

        else{
            Log.e("hyp", "some thing,$requestCode")
        }
    }
}