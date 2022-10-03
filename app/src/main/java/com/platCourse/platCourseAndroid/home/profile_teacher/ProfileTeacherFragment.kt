package com.platCourse.platCourseAndroid.home.profile_teacher

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentProfileBinding
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.utils.Constants_Api
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.fromJson
import com.rowaad.utils.extention.loadImage
import org.jetbrains.anko.sdk27.coroutines.onClick

class ProfileTeacherFragment : BaseFragment(R.layout.fragment_profile) {

    private var details: CourseItem?=null
    private val binding by viewBinding<FragmentProfileBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        details=arguments?.getString("details")?.fromJson<CourseItem>()
        sendRequestProfile()
        setupActions()
        handleData(viewModel.getUser()!!)
        handleProfileObservable()

        handleToolbar()

    }

    private fun handleToolbar() {
        binding?.toolbar.tvTitle?.text=getString(R.string.teacher_profile)
        binding?.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupActions() {
        binding?.toolbar.ivBack?.onClick {
            handleAvatar()
        }
        binding.tvEmail.setOnClickListener {
            IntentUtils.openEmailIntent(binding.tvEmail.text.toString(),requireContext())
        }
        binding.tvPhone.setOnClickListener {
            IntentUtils.callNumberIntent(phone = binding.tvPhone.text.toString(),context = requireContext())
        }

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
        viewModel.sendRequestTeacher(details?.ownerId ?: 0)
    }



    private fun handleProfileObservable(){
        handleSharedFlow(viewModel.contactFlow,onSuccess = {it as UserModel
            handleData(it)
        })
    }


    private fun handleData(user: UserModel) {
        with(binding!!){
            tvName.text = user.name
            tvCountry.text = user.country
            tvCity.text = user.city
            tvEmail.text = user.email
            tvPhone.text = user.phone_number
            if (user.profile_image.isNullOrEmpty().not()) ivAvatar.loadImage(user.profile_image)

        }
    }




}