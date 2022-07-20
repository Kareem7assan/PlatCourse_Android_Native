package com.platCourse.platCourseAndroid.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.Menu
import com.rowaad.dialogs_utils.LogoutDialog
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.splash.SplashActivity
import com.platCourse.platCourseAndroid.databinding.FragmentMenuBinding
import com.platCourse.platCourseAndroid.menu.adapter.MenuAdapter
import com.platCourse.platCourseAndroid.menu.change_password.ChangePasswordActivity
import com.platCourse.platCourseAndroid.menu.contact_us.ContactUsActivity
import com.platCourse.platCourseAndroid.menu.pages.PagesActivity
import com.rowaad.utils.extention.*

class MenuFragment : BaseFragment(R.layout.fragment_menu) {
    private val viewModel: MenuViewModel by viewModels()
    private val binding by viewBinding<FragmentMenuBinding>()
    private val menuAdapter by lazy {
        MenuAdapter()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        setupRec()
        handleSharedFlow(viewModel.userFlow,onSuccess = {
            handleLogout()
        })

    }

    private fun setupRec() {
        binding.rvMenu.layoutManager=LinearLayoutManager(requireContext())
        binding.rvMenu.adapter=menuAdapter
        menuAdapter.swapData(viewModel.getListMenu())
    }

    override fun onResume() {
        super.onResume()
        handleMenu()
    }


    private fun handleMenu() {
        val user=viewModel.getUser()
        with(binding) {
            ivLogo.loadImage(user?.image)
            tvUserName.text=if (user?.username.isNullOrEmpty()) user?.username else "@"+user?.username
            if (viewModel.isVisitor)
            {
                tvName.text = getString(R.string.welcom)
                tvUserName.hide()
                logoMenu.show().also { ivLogo.invisible() }

            }
            else{
                tvName.text = user?.name
                tvUserName.show()
                logoMenu.hide().also { ivLogo.show() }
            }
        }
    }

    private fun handleLogout() { requireActivity().startActivityWithAnimation<SplashActivity>(formLogout = false) }

    private fun setupActions() {
       menuAdapter.onClickItem=::onClickItem
}

    private fun onClickItem(menu: Menu) {
        when(menu){
            Menu.PROFILE->{
               /* startActivity(Intent(requireContext(),ProfileActivity::class.java).also {
                    it.putExtra("userId",viewModel.getUser()?.id)
                })*/
            }
            Menu.CHANGE_PASSWORD->{
                startActivity(Intent(requireContext(),ChangePasswordActivity::class.java))
            }
            Menu.COMMISSION->{
                //startActivity(Intent(requireContext(),CommissionActivity::class.java))
            }
            Menu.PACKAGES->{
             //   startActivity(Intent(requireContext(),AdvertisePackageActivity::class.java))
            }
            Menu.CONTACT_US->{
                startActivity(Intent(requireContext(),ContactUsActivity::class.java))
            }
            Menu.ABOUT_US->{
                startActivity(Intent(requireContext(),PagesActivity::class.java).also {
                    it.putExtra("page","aboutUs")
                })
            }
            Menu.TERMS->{
                startActivity(Intent(requireContext(),PagesActivity::class.java).also {
                    it.putExtra("page","terms")
                })
            }
            Menu.PRIVACY_SALES->{
                startActivity(Intent(requireContext(),PagesActivity::class.java).also {
                    it.putExtra("page","sales")
                })
            }
            Menu.LOGIN->{
                handleLogin()
            }
            Menu.LOGOUT->{
                LogoutDialog.show(requireActivity()){
                    viewModel.logout()
                }
            }
        }
    }

    private fun handleLogin() { requireActivity().startActivityWithAnimation<SplashActivity>(formLogout = true) }


}


