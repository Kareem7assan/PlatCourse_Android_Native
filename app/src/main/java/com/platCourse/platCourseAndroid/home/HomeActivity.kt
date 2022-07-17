package com.platCourse.platCourseAndroid.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.jakewharton.rxbinding4.widget.textChanges
import com.rowaad.app.base.BaseActivity
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityHomeBinding
import com.platCourse.platCourseAndroid.home.add_ad.AddAdActivity
import com.platCourse.platCourseAndroid.home.chat.viewmodel.ChatViewModel
import com.platCourse.platCourseAndroid.menu.MenuFragment
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.rowaad.utils.extention.clearTxt
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.loadImage
import com.rowaad.utils.extention.show
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeActivity : BaseActivity(R.layout.activity_home) {

    private var mLastClickTime: Long = 0
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private val viewModel: MenuViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        setupToolbar()
    }
    override fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        binding = ActivityHomeBinding.bind(findViewById(R.id.drawerContent))
        setActions()
        setNav()
        setupBottomNavigationChecked()
    }


    private fun setupBottomNavigationChecked() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.timeLineFragment -> binding.mainBottomNavigation.menu.findItem(
                    R.id.tweetsFragment)?.isChecked =
                    true
                R.id.favouriteFragment -> binding.mainBottomNavigation.menu.findItem(
                    R.id.favouriteFragment)?.isChecked =
                    true
                R.id.messagesFragment -> binding.mainBottomNavigation.menu.findItem(
                        R.id.messages)?.isChecked =
                        true
                R.id.notificationsFragment -> binding.mainBottomNavigation.menu.findItem(
                        R.id.notifFragment)?.isChecked =
                        true
                else -> binding?.mainBottomNavigation.menu.findItem(
                    R.id.tweetFragment)?.isChecked =
                    true
            }
        }
    }

    private fun setupToolbar() {
        if (viewModel.isVisitor)
            binding.ivBg.show().also { binding.ivAppLogo.show() }
        else
            binding.ivProfile.loadImage(viewModel.getUser()?.image)
                .also { binding.ivBg.hide() }
                .also { binding.ivAppLogo.hide() }
    }

    override fun setActions(){
        setupBottomNavigation()
        binding.ivSearch.setOnClickListener {
            if (navController.currentDestination?.id==R.id.messagesFragment){
                showUserSearchBar().also {
                    hideNormalSearch()
                }
            }
            else {
                showNormalSearch().also {
                    navController.navigate(R.id.action_global_preSearchFragment)
                }
            }
        }
        binding.ivClear.setOnClickListener {
            hideUserSearchBar().also {
                showNormalSearch()
                binding.etSearch.clearTxt()
            }
        }
        binding.btnStore.setOnClickListener {
            startActivity(Intent(this,AddAdActivity::class.java))
        }
        handleSearch()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
                showNormalSearch().also { hideUserSearchBar() }
        }
        //handleBottomBar and toolbar
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.regionsFragment -> {
                    binding.mainBottomNavigation.hide().also { binding.btnStore.hide() }.also { binding.toolbar.hide() }
                }
                R.id.citiesFragment -> {
                    binding.mainBottomNavigation.hide().also { binding.btnStore.hide() }.also { binding.toolbar.hide() }
                }
                R.id.regionsSearchFragment -> {
                    binding.mainBottomNavigation.hide().also { binding.btnStore.hide() }.also { binding.toolbar.hide() }
                }
                R.id.citiesSearchFragment -> {
                    binding.mainBottomNavigation.hide().also { binding.btnStore.hide() }.also { binding.toolbar.hide() }

                }
                R.id.preSearchFragment -> {
                    binding.mainBottomNavigation.hide().also { binding.btnStore.hide() }.also { binding.toolbar.hide() }
                }
                R.id.searchFragment -> {
                    binding.mainBottomNavigation.hide().also { binding.btnStore.hide() }.also { binding.toolbar.hide() }
                }
                R.id.chatFragment -> {
                    binding.mainBottomNavigation.hide().also { binding.btnStore.hide() }.also { binding.toolbar.hide() }
                }
                R.id.hashFragment -> {
                    binding.mainBottomNavigation.hide().also { binding.btnStore.hide() }.also { binding.toolbar.hide() }
                }
                R.id.detailsFragment -> {
                    binding.mainBottomNavigation.hide().also { binding.btnStore.hide() }.also { binding.toolbar.hide() }
                }
                R.id.reportFragment -> {
                    binding.mainBottomNavigation.hide().also { binding.btnStore.hide() }.also { binding.toolbar.hide() }
                }
                else -> {
                    binding.mainBottomNavigation.show().also { binding.btnStore.show() }.also { binding.toolbar.show() }
                }
            }
        }
    }

    private fun handleSearch() {
        binding.etSearch.textChanges()
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { chatViewModel.handleSearch(it.toString()) }

    }

    private fun showUserSearchBar() {
        binding.cardSearch.show()

    }
    private fun hideUserSearchBar() {
        binding.cardSearch.hide()

    }


    private fun showNormalSearch(){
        binding.groupMainSearch.show()
        binding.ivProfile.show()
    }
    private fun hideNormalSearch(){
        binding.groupMainSearch.hide()
        binding.ivProfile.hide()
    }

    private fun setupBottomNavigation() {
        binding?.ivProfile?.setOnClickListener {
            if (binding?.drawerContent!!.isOpen) {
                binding?.drawerContent?.closeDrawers()
                //navController.popBackStack(R.id.menuFragment,true)
            }
            else{
                binding?.drawerContent?.open()
                //navController.navigate(R.id.menuFragment)
            }
        }
        binding.ivBg?.setOnClickListener {
            if (binding?.drawerContent!!.isOpen) {
                binding?.drawerContent?.closeDrawers()
                //navController.popBackStack(R.id.menuFragment,true)
            }
            else{
                binding?.drawerContent?.open()
                //navController.navigate(R.id.menuFragment)
            }
        }

        binding?.mainBottomNavigation?.setOnItemSelectedListener { menuItem->
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                return@setOnItemSelectedListener false
            }
            else {
                mLastClickTime = SystemClock.elapsedRealtime()

                when (menuItem.itemId) {
                  R.id.tweetsFragment -> {
                        navController.navigate(R.id.action_global_timeLineFragment)
                        return@setOnItemSelectedListener true
                }
                    R.id.favouriteFragment -> {
                        if (viewModel.isVisitor){
                            showVisitorDialog(binding.homeNavHost) {}
                        }
                        else {
                            navController.navigate(R.id.action_global_favouriteFragment)
                        }
                        return@setOnItemSelectedListener true
                }
                    R.id.messages -> {
                        if (viewModel.isVisitor){
                            showVisitorDialog(binding.homeNavHost) {}
                        }
                        else {
                            navController.navigate(R.id.action_global_messagesFragment)
                        }
                        return@setOnItemSelectedListener true
                }
                    R.id.notifFragment -> {
                        if (viewModel.isVisitor){
                            showVisitorDialog(binding.homeNavHost) {}
                        }
                        else {
                            navController.navigate(R.id.action_global_notificationsFragment)
                        }
                        return@setOnItemSelectedListener true
                }

                    else -> return@setOnItemSelectedListener false
                }
            }

        }
    }


    private fun setNav() {
        if (binding.drawerContent.isOpen) {
            binding.drawerContent.closeDrawers()
        }
        val fragmentManager = supportFragmentManager
        val fragment = MenuFragment()
        fragmentManager.beginTransaction().replace(R.id.navView, fragment).commit()

    }


}