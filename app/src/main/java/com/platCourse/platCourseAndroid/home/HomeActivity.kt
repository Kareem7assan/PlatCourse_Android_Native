package com.platCourse.platCourseAndroid.home

import android.content.Intent
import android.os.SystemClock
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityHomeBinding
import com.platCourse.platCourseAndroid.menu.MenuFragment
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.rowaad.app.base.BaseActivity
import com.rowaad.utils.extention.hide
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
        setupBottomNavigationChecked()
    }


    private fun setupBottomNavigationChecked() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.coursesMenuFragment -> {
                    binding.mainBottomNavigation.menu.findItem(R.id.coursesMenuFragment)?.isChecked =true
                    setupTitle(getString(R.string.courses))
                }
                R.id.myCoursesMenuFragment -> {
                    binding.mainBottomNavigation.menu.findItem(R.id.myCoursesMenuFragment)?.isChecked = true
                    setupTitle(getString(R.string.my_courses))

                }
                R.id.homeMenuFragment -> {
                    binding.mainBottomNavigation.menu.findItem(R.id.homeMenuFragment)?.isChecked = true
                    setupTitle(getString(R.string.home))
                }

                R.id.moreMenuFragment -> {
                    binding.mainBottomNavigation.menu.findItem(R.id.moreMenuFragment)?.isChecked = true
                    setupTitle(getString(R.string.menu))
                }

                else -> {
                    binding?.mainBottomNavigation.menu.findItem(R.id.homeMenuFragment)?.isChecked = true
                    setupTitle(getString(R.string.home))
                }
            }
        }
    }

    private fun setupTitle(title: String) {
        binding.toolbar.tvTitle.text=title
    }

    private fun setupToolbar() {

    }

    override fun setActions(){
        setupBottomNavigation()
        binding.toolbar.ivSearchEnd.setOnClickListener {

        }
        binding.toolbar.ivNotif.setOnClickListener {

        }



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