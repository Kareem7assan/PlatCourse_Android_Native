package com.platCourse.platCourseAndroid.home

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.Settings
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityHomeBinding
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.rowaad.app.base.BaseActivity
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import com.rowaad.utils.extention.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class HomeActivity : BaseActivity(R.layout.activity_home) {

    private var mLastClickTime: Long = 0
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private val viewModel: MenuViewModel by viewModels()


    override fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        binding = ActivityHomeBinding.bind(findViewById(R.id.drawerContent))
        setActions()
        setupBottomNavigationChecked()
        setupToolbarAction()
        Timer().scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                    runOnUiThread {
                        if (viewModel.isVisitor.not()) viewModel.showNotifications(1)
                    }




            }

        },0,60000)
        binding.toolbar?.notificationCount.hide()
        handleCount()


    }

    private fun handleCount() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hasSeenNotifications.collectLatest { hasUnSeen ->
                    if (hasUnSeen && binding.toolbar?.ivNotif.isVisible) binding.toolbar?.notificationCount.show()
                    else binding.toolbar?.notificationCount.hide()
                }
            }
        }

    }

    private fun setupToolbarAction() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.searchCoursesFragment -> {
                    hideToolbar()
                }
                R.id.notificationsFragment->{
                    hideToolbar()
                }
                R.id.courseLessonsFragment->{
                    hideToolbar()
                }
                R.id.quizWebViewFragment->{
                    binding.mainBottomNavigation.hide()
                    hideToolbar()
                }
                R.id.profileTeacherFragment->{
                    hideToolbar()
                }
                else -> {
                    showToolbar()
                }
            }
        }
    }


    private fun setupBottomNavigationChecked() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.coursesFragment -> {
                    binding.mainBottomNavigation.menu.findItem(R.id.coursesMenuFragment)?.isChecked =true
                    setupTitle(getString(R.string.courses))
                    handleHomeToolbar()
                }
                R.id.myCoursesFragment -> {
                    binding.mainBottomNavigation.menu.findItem(R.id.myCoursesMenuFragment)?.isChecked = true
                    setupTitle(getString(R.string.my_courses))
                    handleHomeToolbar()
                }
                R.id.homeFragment -> {
                    binding.mainBottomNavigation.menu.findItem(R.id.homeMenuFragment)?.isChecked = true
                    setupTitle(getString(R.string.home))
                    handleHomeToolbar()
                }

                R.id.menuFragment -> {
                    binding.mainBottomNavigation.menu.findItem(R.id.moreMenuFragment)?.isChecked = true
                    setupTitle(getString(R.string.more))
                    handleHomeToolbar()
                }
                R.id.pendingCoursesFragment -> {
                    binding.mainBottomNavigation.menu.findItem(R.id.moreMenuFragment)?.isChecked = true
                    setupTitle(getString(R.string.orders))
                    handleHomeToolbar()
                }
                else -> {
                    //binding?.mainBottomNavigation.menu.findItem(R.id.homeMenuFragment)?.isChecked = true
                    //setupTitle(getString(R.string.home))

                    handleNormalToolbar()
                }
            }
        }
    }

    private fun hideToolbar() {
        binding.mainBottomNavigation.hide()
        binding.toolbar.root.hide()
    }
    private fun showToolbar() {
        binding.mainBottomNavigation.show()
        binding.toolbar.root.show()
    }

    private fun setupTitle(title: String) {
        binding.toolbar.tvTitle.text=title
    }
    fun setupTitleSubCat(title: String) {
        binding.toolbar.tvTitle.text=title
    }


    override fun setActions(){
        setupBottomNavigation()
        binding.toolbar.ivSearchEnd.setOnClickListener {
            navController.navigate(R.id.action_global_searchCoursesFragment)
        }
        binding.toolbar.ivNotif.setOnClickListener {
            if (getBaseRepository(this).isLogin())
                 navController.navigate(R.id.action_global_notificationsFragment)
             else
                 showVisitorDialog(binding.root){

                 }
        }

        binding.toolbar.ivBack.setOnClickListener {
            navController.navigateUp()
        }



    }

    private fun handleHomeToolbar(){
        binding.toolbar.groupHome.show()
        binding.toolbar.ivBack.hide()
    }

    private fun handleNormalToolbar(){
        binding.toolbar.groupHome.hide()
        binding.toolbar.ivBack.show()
    }





    private fun setupBottomNavigation() {
        binding?.mainBottomNavigation?.setOnItemSelectedListener { menuItem->
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                return@setOnItemSelectedListener false
            }
            else {
                mLastClickTime = SystemClock.elapsedRealtime()

                when (menuItem.itemId) {
                  R.id.coursesMenuFragment -> {
                        navController.navigate(R.id.action_global_coursesFragment)
                        return@setOnItemSelectedListener true
                }
                    R.id.myCoursesMenuFragment -> {
                        navController.navigate(R.id.action_global_myCoursesFragment)
                        return@setOnItemSelectedListener true
                }
                    R.id.homeMenuFragment -> {
                        navController.navigate(R.id.action_global_homeFragment)
                        return@setOnItemSelectedListener true
                }
                    R.id.moreMenuFragment -> {
                        navController.navigate(R.id.action_global_menuFragment)
                        return@setOnItemSelectedListener true
                }

                    else -> return@setOnItemSelectedListener false
                }
            }

        }
    }





}