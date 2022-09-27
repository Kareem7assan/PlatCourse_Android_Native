package com.platCourse.platCourseAndroid.auth.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import androidx.core.os.bundleOf
import androidx.core.os.postDelayed
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.utils.Constants_Api.INTENT.LOGOUT
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivitySplashBinding
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.rowaad.app.data.utils.Constants_Api
import com.rowaad.utils.extention.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity(R.layout.activity_splash) {

    private lateinit var navController: NavController
    private var binding: ActivitySplashBinding? = null




    override fun init() {
        binding=ActivitySplashBinding.bind(findViewById(R.id.rootNested))
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.auth_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        val fromLogout=intent.getBooleanExtra(LOGOUT,false)
        Log.e("fromLogout",fromLogout.toString())
        Handler(Looper.getMainLooper()).postDelayed({
            if (fromLogout) {
                navController.navigate(R.id.action_splashFragment_to_loginFragment,
                        bundleOf(
                        LOGOUT
                            to
                                "true"
                        )
                )
            }
            else{
                startActivity(Intent(this, HomeActivity::class.java))
            }
        },3000)
    }



    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }


}