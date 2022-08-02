package com.platCourse.platCourseAndroid.menu

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
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
import com.platCourse.platCourseAndroid.menu.articles.ArticlesActivity
import com.platCourse.platCourseAndroid.menu.change_password.ChangePasswordActivity
import com.platCourse.platCourseAndroid.menu.contact_us.ContactUsActivity
import com.platCourse.platCourseAndroid.menu.pages.PagesActivity
import com.platCourse.platCourseAndroid.menu.terms.TermsActivity
import com.rowaad.utils.extention.*
import org.jetbrains.anko.configuration
import org.jetbrains.anko.support.v4.toast

class MenuFragment : BaseFragment(R.layout.fragment_menu) {
    private val viewModel: MenuViewModel by viewModels()
    private val binding by viewBinding<FragmentMenuBinding>()
    private val menuAdapter by lazy { MenuAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        setupRec()
        handleSharedFlow(viewModel.userFlow,onSuccess = {
            handleLogout()
        })
        setupNightMode()

    }

    private fun setupNightMode() {
        menuAdapter.enableDarkMode(isDark())
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

            tvName.text=if (user?.username.isNullOrEmpty()) user?.username else "@"+user?.username
            if (viewModel.isVisitor)
            {
                tvName.text = getString(R.string.login)
                ivLogo.setImageResource(R.drawable.response)

            }
            else{
                tvName.text = user?.name
                ivLogo.loadImage(user?.image)
            }
        }
    }

    private fun handleLogout() { requireActivity().startActivityWithAnimation<SplashActivity>(formLogout = false) }

    private fun setupActions() {
       menuAdapter.onClickItem=::onClickItem
       menuAdapter.onClickItemNight=::onClickItemNight
}

    private fun onClickItemNight(isChecked:Boolean) {
            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES).also { viewModel.setDarkMode(true) }
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO).also { viewModel.setDarkMode(false) }

    }

    private fun isDark():Boolean{
        var isDark =false
        when (requireActivity().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {isDark=false} // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {isDark=true} // Night mode is active, we're using dark theme
        }
        return isDark
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
            Menu.ARTICLES->{
                startActivity(Intent(requireContext(),ArticlesActivity::class.java))
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
                startActivity(Intent(requireContext(),TermsActivity::class.java))
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


