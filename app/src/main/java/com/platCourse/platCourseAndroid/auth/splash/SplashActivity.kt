package com.platCourse.platCourseAndroid.auth.splash

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.platCourse.platCourseAndroid.BuildConfig
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivitySplashBinding
import com.platCourse.platCourseAndroid.error.ErrorScreenActivity
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.platCourse.platCourseAndroid.home.youtube.YoutubeActivity
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.utils.Constants_Api.INTENT.LOGOUT
import com.rowaad.dialogs_utils.MaintenanceDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity(R.layout.activity_splash) {

    private var fromLogout: Boolean = false
    private lateinit var navController: NavController
    private var binding: ActivitySplashBinding? = null
    private var isTrusted :Boolean = true

    private val viewModel: SplashViewModel by viewModels()



    override fun init() {
        binding = ActivitySplashBinding.bind(findViewById(R.id.rootNested))
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.auth_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        fromLogout = intent.getBooleanExtra(LOGOUT, false)
        viewModel.sendRequestVersion()
        handleVersion()



    }

    private fun handleVersion() {
        handleSharedFlow(viewModel.appVersion,onSuccess = {
            val appVersion=it as String
            if (CompareVersion.isFirmwareNewer(localVersion = BuildConfig.VERSION_NAME,if (appVersion.split(".").size == 3) appVersion else if (appVersion.split(".").size < 3) "$appVersion.0" else "$appVersion.0.0")){
                MaintenanceDialog.show(this){
                    val packageName="com.platcourse.platcourseapplication"
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                    } catch (e: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                    }
                }

            }
            else{

               isPhoneNumberTrusted()
               // handleNavigation(fromLogout)

            }
        })
    }

    private fun handleNavigation(fromLogout: Boolean) {
        if (navController.currentDestination?.id==R.id.splashFragment) {
            Handler(Looper.getMainLooper()).postDelayed({
                if (fromLogout) {
                    navController.navigate(
                            R.id.action_splashFragment_to_loginFragment,
                            bundleOf(
                                    LOGOUT
                                            to
                                            "true"
                            )
                    )
                } else {
                    startActivity(Intent(this, HomeActivity::class.java))
                }


            }, 3000)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }


    private fun isAnyPermissionNotGranted():Boolean{
        return    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED

    }

    private fun navigateErrorScreen(){
        startActivity(Intent(applicationContext, ErrorScreenActivity::class.java).also {
            it.putExtra("error","لا يمكن تشغيل التطبيق من خلال محاكى او برامج لا تتوافق مع شروط استخدام التطبيق، يرجى تشغيل التطبيق من خلال تطبيق الموبايل او الكمبيوتر الرسمى.")
        })
        finish()
    }
 private fun navigateErrorScreenPermession(){
        startActivity(Intent(applicationContext, ErrorScreenActivity::class.java).also {
            it.putExtra("error","عذرا لا يسمح بالدخول بدون الموافقة على أذونات الهاتف المطلوبة.")
        })
        finish()
    }

     fun isPhoneNumberTrusted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if (isAnyPermissionNotGranted()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    checkPhoneStatePermissionAPI30()
                }
                else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.READ_PHONE_STATE
                        ),
                        500
                    )
                }

            }
            else{

                val manger = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

                isTrusted = (
                        manger.simCountryIso == "eg" || manger.simCountryIso == "sa"
                        ||
                        manger.simCountryIso=="kw" || manger.simCountryIso=="ae"
                        ||
                        manger.simCountryIso=="jo" || manger.simCountryIso=="ps"
                        ||
                        manger.simCountryIso=="iq" || manger.simCountryIso=="bh"
                        ||
                        manger.simCountryIso=="ae" || manger.simCountryIso=="jo"
                        ||
                        manger.simCountryIso=="sd" || manger.simCountryIso=="om"
                        ||
                        manger.simCountryIso=="qa" || manger.simCountryIso=="lb"
                        ||
                        manger.simCountryIso=="ma" || manger.simCountryIso=="tn"
                        ||
                        manger.simCountryIso=="dz" || manger.simCountryIso=="il"

                        )
                if (isTrusted) handleNavigation(fromLogout = fromLogout)
                else {

                    addValue( PlatApp(false,uidValue!!))

                    navigateErrorScreen()
                }

                //handleNavigation(fromLogout = fromLogout)
                Log.e("manger",manger.simCountryIso)
            }
        }
         else{
            if (isAnyPermissionNotGranted()) {
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                                Manifest.permission.READ_PHONE_STATE
                        ),
                        500
                )
            }
            else{

                val manger = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

                isTrusted = (manger.simCountryIso == "eg" || manger.simCountryIso == "sa"
                        ||
                        manger.simCountryIso=="kw" || manger.simCountryIso=="ae"
                        ||
                        manger.simCountryIso=="jo" || manger.simCountryIso=="ps"
                        ||
                        manger.simCountryIso=="iq" || manger.simCountryIso=="bh"
                        ||
                        manger.simCountryIso=="ae" || manger.simCountryIso=="jo"
                        ||
                        manger.simCountryIso=="sd" || manger.simCountryIso=="om"
                        ||
                        manger.simCountryIso=="qa" || manger.simCountryIso=="lb"
                        ||
                        manger.simCountryIso=="ma" || manger.simCountryIso=="tn"
                        || manger.simCountryIso=="dz" || manger.simCountryIso=="il"

                        )
                if (isTrusted) handleNavigation(fromLogout = fromLogout)
                else {

                    addValue( PlatApp(false,uidValue!!))

                    navigateErrorScreen()
                }

                //handleNavigation(fromLogout = fromLogout)
            }
        }



    }

    fun checkPhoneStatePermissionAPI30() {
        var dialog : AlertDialog?=null
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {
            dialog = AlertDialog.Builder(this)
                .setTitle(R.string.phone_permission_title)
                .setMessage(R.string.phone_state_permission_message)
                .setPositiveButton(com.rowaad.utils.R.string.yes) { dialog, _ ->
                    // this request will take user to Application's Setting page
                    dialog.dismiss()
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_PHONE_STATE),
                        500
                    )


                }
                .setNegativeButton(com.rowaad.utils.R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()

        }
        else{
            dialog?.dismiss()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==500) {
            if (grantResults.isNotEmpty()) {
                isPhoneNumberTrusted()
            } else {
                navigateErrorScreenPermession()
            }
        }
    }
}