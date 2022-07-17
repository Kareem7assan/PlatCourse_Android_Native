package com.platCourse.platCourseAndroid.home.profile

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.viewModels
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.model.UserModel
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentProfileBinding
import com.platCourse.platCourseAndroid.home.profile.adapter.StateAdapter
import com.platCourse.platCourseAndroid.home.profile.viewmodel.ProfileViewModel
import com.platCourse.platCourseAndroid.menu.edit_profile.EditProfileActivity
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.loadImage
import com.rowaad.utils.extention.show
import com.rowaad.utils.extention.toast
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColorResource

class ProfileActivity : BaseActivity(R.layout.fragment_profile), TabLayout.OnTabSelectedListener {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentProfileBinding
    private val viewModel:ProfileViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        sendRequestProfile()
    }

    override fun init() {
          binding=FragmentProfileBinding.bind(findViewById(R.id.nestedRoot))
          viewModel.userId=intent.getIntExtra("userId", 0)
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.profile_nav_host) as NavHostFragment
         navController = navHostFragment.navController
          setupTabs()
          setupProfileObservable()
          setupFollowObservable()
          setupListener()
          handleSelectListener()
    }

    private fun handleSelectListener() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.tweetsProfileFragment -> {
                    binding.appBar.show().also { binding.content.tabProfile.show() }.also { binding.content.viewSep.show() }
                    binding.content.tabProfile.setScrollPosition(0, 0f, true)
                    enableBehaviour()

                }
                R.id.tweetsRepliesFragment -> {
                    binding.appBar.show().also { binding.content.tabProfile.show() }.also { binding.content.viewSep.show() }
                    binding.content.tabProfile.setScrollPosition(2, 0f, true)
                    enableBehaviour()
                }
                R.id.favsProfileFragment -> {
                    binding.appBar.show().also { binding.content.tabProfile.show() }.also { binding.content.viewSep.show() }
                    binding.content.tabProfile.setScrollPosition(1, 0f, true)
                    enableBehaviour()
                }
                else -> {
                    disableBehaviour()
                    binding.appBar.hide().also { binding.content.tabProfile.hide() }.also { binding.content.viewSep.hide() }
                }
            }
        }
    }

    private fun enableBehaviour() {
        val params: CoordinatorLayout.LayoutParams = binding.content.root.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = AppBarLayout.ScrollingViewBehavior()
        binding.content.root.requestLayout()
    }

    private fun disableBehaviour() {
        val params: CoordinatorLayout.LayoutParams = binding.content.root.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = null
        binding.content.root.requestLayout()
    }


    private fun setupTabs() {
        binding.content.tabProfile.addTab(binding.content.tabProfile.newTab().setText(R.string.the_tweets))
        binding.content.tabProfile.addTab(binding.content.tabProfile.newTab().setText(R.string.the_favs))
        binding.content.tabProfile.addTab(binding.content.tabProfile.newTab().setText(R.string.replies_and_tweets))
        binding.content.tabProfile.addOnTabSelectedListener(this)
    }

    private fun setupFollowObservable() {
        handleSharedFlow(viewModel.followFlow, onSuccess = {
        }, onShowProgress = {}, onHideProgress = {})
    }

    private fun setupListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.actionBtn.setOnClickListener {
            if (viewModel.isGuestUser().not()) {
                when {
                    binding.actionBtn.text.toString() == getString(R.string.edit_profile) -> {
                        startActivity(Intent(this, EditProfileActivity::class.java))
                    }
                    binding.actionBtn.text.toString() == getString(R.string.cancel_follow) -> {
                        handleUnFollowButton()
                        viewModel.sendRequestFollow()
                    }
                    else -> {
                        handleFollowButton()
                        viewModel.sendRequestFollow()
                    }
                }
            }
            else{
                showVisitorDialog(binding.root) {}
            }
        }
    }

    private fun sendRequestProfile() {
        viewModel.sendRequestProfile()
    }

    private fun setupProfileObservable() {
        handleSharedFlow(viewModel.profileFlow, onSuccess = {
            it as UserModel
            handleUser(it)
        })
    }




    private fun handleUser(user: UserModel) {
        //my profile
        if (viewModel.isMyProfile(userId = user.id!!)){
            binding.actionBtn.text=getString(R.string.edit_profile)
            binding.actionBtn.backgroundResource=R.drawable.stroke_water_bg_18_rad
        }
        else{
            if (user.isFollowed==true) {
                handleFollowButton()
            }
            else{
                handleUnFollowButton()
            }
        }
        binding.ivBgProfile.loadImage(user.header)
        binding.ivAvatar.loadImage(user.image)
        binding.tvName.text=user.name
        binding.tvUserName.text="@"+user.username
        binding.tvBio.text=user.bio
        binding.tvFollowing.text=user.totalFollowing
        binding.tvFollowers.text=user.totalFollowers
        //handleImageBgColor(user.header!!)


    }


    private fun handleFollowButton(){
        binding.actionBtn.text = getString(R.string.cancel_follow)
        binding.actionBtn.backgroundResource = R.drawable.stroke_water_solid_18_rad
        binding.actionBtn.textColorResource = R.color.white
    }

    private fun handleUnFollowButton(){
        binding.actionBtn.text = getString(R.string.follow)
        binding.actionBtn.backgroundResource = R.drawable.stroke_water_bg_18_rad
        binding.actionBtn.textColorResource = R.color.water_blue
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        binding.content.tabProfile.getTabAt(tab?.position ?: 0)?.select()
        when (tab?.position) {
            0 -> {
                navController.navigate(R.id.action_global_tweetsProfileFragment)
            }
            1 -> {
                navController.navigate(R.id.action_global_favsProfileFragment)
            }
            else -> {
                navController.navigate(R.id.action_global_tweetsRepliesFragment)
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }
}