package com.platCourse.platCourseAndroid.home

import android.os.SystemClock
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityHomeBinding
import com.rowaad.app.base.BaseActivity
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity(R.layout.activity_home) {

    private var mLastClickTime: Long = 0
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController


    override fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        binding = ActivityHomeBinding.bind(findViewById(R.id.drawerContent))
        setActions()
        setupBottomNavigationChecked()
        setupToolbarAction()
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