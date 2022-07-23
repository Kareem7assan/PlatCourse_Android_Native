package com.rowaad.app.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.google.android.material.snackbar.Snackbar
import com.rowaad.app.base.databinding.ActivityBaseBinding
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.utils.Constants_Api
import com.rowaad.dialogs_utils.*
import com.rowaad.utils.extention.toast
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
abstract class BaseActivity(private val layoutResource:Int): AppCompatActivity() {
    private var viewBase: ActivityBaseBinding? = null
    private var progressDialog: ACProgressFlower? = null
    var savedInstanceState: Bundle? = null

    private val baseViewModel: BaseViewModel by viewModels()

    fun setFullScreen(){
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
    abstract fun init()
    open fun setActions(){}

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface BaseActivityEntryPoint {
        val baseRepository: BaseRepository
    }


    fun showVisitorDialog(view: View, onAction: (() -> Unit)?) {
        val snack= Snackbar.make(view, getString(R.string.you_should_login_first), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.register)) {
                    onAction?.invoke()
                    intent.component = (ComponentName(
                            "com.platCourse.platCourseAndroid",
                            "com.platCourse.platCourseAndroid.auth.splash.SplashActivity"
                    ))
                    //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra(Constants_Api.INTENT.LOGOUT,true)
                    startActivity(intent)
                }
                .setActionTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))

        val view = snack.view
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val btn = view.findViewById<Button>(com.google.android.material.R.id.snackbar_action)
        textView.typeface = ResourcesCompat.getFont(this,R.font.regular)
        btn.typeface = ResourcesCompat.getFont(this,R.font.regular)
        snack.show()
    }

    fun getBaseRepository(appContext: Context): BaseRepository = EntryPointAccessors
        .fromApplication(appContext, BaseActivityEntryPoint::class.java)
        .baseRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            getCustomizedContext(this, getBaseRepository(this).lang)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //resources.setLanguage(baseViewModel.getBaseRepository(this).lang)
        //Log.e("lang",baseViewModel.getBaseRepository(this).lang+baseViewModel.getBaseRepository(this).mobBrand+","
        //+baseViewModel.getBaseRepository(this).mobModel+","+baseViewModel.getBaseRepository(this).mobVersion)
/*
        if(findOptional<RelativeLayout>(R.id.main_content)!=null){
            viewBase= ActivityBaseBinding.bind(findOptional(R.id.main_content)!!)
            val activityView = LayoutInflater.from(this)
                    .inflate(layoutResource, viewBase?.root, false) as ViewGroup


            viewBase?.flContent?.addView(activityView)
            viewBase?.prgressRoot?.addView(progressView)
        }
        else {
            setContentView(layoutResource)
        }*/
        viewBase = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(viewBase?.root)
        val activityView = LayoutInflater.from(this)
                .inflate(layoutResource, viewBase?.flContent, false) as ViewGroup
        viewBase?.flContent?.addView(activityView)

        //setContentView(layoutResource)
        this.savedInstanceState = savedInstanceState
        init()
        setActions()
        observeUnAuthorized()
        observeMaintenance()
       // observeConnection()
        //setFullScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBase=null
    }
    private fun observeMaintenance() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                baseViewModel.maintenanceFlow.collect {
                    if (it) handleMaintenance()
                }
            }
        }
    }

    private fun observeConnection() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                baseViewModel.connectionErrorFlow.collectLatest {
                    if (it) NoInternetDialog.show(this@BaseActivity,{})


                }
            }
        }
    }

    private fun inflateLayout(progressRes: Int) {
        //viewBase?.flProgress?.show()
        //viewBase?.flContent?.removeAllViews()
         val progressViewRes = progressRes
        val progress = LayoutInflater.from(this)
                .inflate(progressViewRes, viewBase?.flContent, false) as ViewGroup
        viewBase?.flContent?.addView(progress)
    }

    private fun inflateMain(layoutResource:Int) {
        //viewBase?.flProgress?.show()
        viewBase?.flContent?.removeAllViewsInLayout()

        val progress = LayoutInflater.from(this)
                .inflate(layoutResource, viewBase?.flContent, false) as ViewGroup
        viewBase?.flContent?.addView(progress)
    }


     fun showSuccessMsg(title:String){
        SuccessToast.showToast(this,title)
    }
     fun showErrorMsg(title:String){
        ErrorDialog.show(this,title)
    }

    private fun observeUnAuthorized() {
        GlobalScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                baseViewModel.unAuthorizedFlow
                        .collect {
                    if (it) handleUnAuthorized()
                }
            }
        }


    }

    override fun attachBaseContext(newBase: Context) {
        val newLocale = Locale(getBaseRepository(newBase).lang)
        val context = com.rowaad.app.base.utils.local.ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }
    private fun getCustomizedContext(context: Context, locale: String = "ar"): Context {
        val appLocale = Locale(locale)
        Locale.setDefault(appLocale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            updateResources(context, appLocale)
        else
            updateResourcesLegacy(context, appLocale)

    }
    private fun updateResources(context: Context, newLocale: Locale): Context {
        val currentResources = context.resources

        val newConfigurations = Configuration(currentResources.configuration)
        newConfigurations.setLocale(newLocale)

        return context.createConfigurationContext(newConfigurations)
    }

    private fun updateResourcesLegacy(context: Context, newLocale: Locale): Context {

        val currentResources = context.resources

        val newConfigurations = currentResources.configuration
        newConfigurations.setLocale(newLocale)

        currentResources.updateConfiguration(newConfigurations, currentResources.displayMetrics)

        return context
    }




   fun handleErrorGeneral(th: Throwable, func: (() -> Unit)?=null) {
       //Log.e("error",th.message.toString())
       th.printStackTrace()
        when (th.message) {
            Constants_Api.ERROR_API.BAD_REQUEST -> {
                ErrorDialog.show(this, getString(R.string.some_error))
            }
            Constants_Api.ERROR_API.NOT_FOUND -> {
                ErrorDialog.show(this, getString(R.string.some_error))
            }
            Constants_Api.ERROR_API.UNAUTHRIZED -> {
                baseViewModel.getBaseRepository(this).logout().also { handleUnAuthorized() }

            }
            Constants_Api.ERROR_API.MAINTENANCE -> {
                handleMaintenance()
            }
            Constants_Api.ERROR_API.CONNECTION_ERROR -> {
                NoInternetDialog.show(this@BaseActivity)
            }

            else -> {
                //ErrorDialog.show(this, getString(R.string.some_error))
                ErrorDialog.show(this,th.message!!)

            }
        }
    }

     fun handleMaintenance(func: (() -> Unit)?=null) {
        if (func != null) MaintenanceDialog.show(this, func) else MaintenanceDialog.show(
            this
        )
    }

    private fun handleUnAuthorized() {
        Log.e("authorized","authorized")
        UnAuthorizeDialog.show(
            activity = this,
            title = getString(R.string.you_have_unauthorized)
        ) {
            intent.component = (ComponentName(
                "com.platCourse.platCourseAndroid",
                "com.platCourse.platCourseAndroid.auth.SplashActivity"
            ))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }




    protected fun showProgress() {
        showProgressDialog()
    }

     fun showProgressFullScreen(){
         inflateLayout(R.layout.progress_dialog)

    }

     fun hideProgressFullScreen(res:Int){
/*
         viewBase!!.flContent?.hide()
         viewBase?.mainContent?.show()
*/

         inflateMain(res)

     }


    private fun showProgressDialog() {
        progressDialog = ACProgressFlower.Builder(this)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.GRAY)
            .fadeColor(Color.WHITE)
            .build()
        progressDialog!!.show()
    }

    protected fun hideProgress() {
        hideProgressDialog()
    }

    private fun hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.hide()
            progressDialog!!.dismiss()
        }
    }


    fun handleSharedFlow(
        userFlow: SharedFlow<NetWorkState>,
        onShowProgress: (() -> Unit)? = null,
        onHideProgress: (() -> Unit)? = null,
        onSuccess: (data: Any) -> Unit,
        onError: ((th: Throwable) -> Unit)? = null
    ){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userFlow.collect {networkState->
                    when(networkState){
                        is NetWorkState.Success<*> ->{
                            onSuccess(networkState.data!!)
                        }
                        is NetWorkState.Loading->{
                            if (onShowProgress==null) showProgress() else onShowProgress()
                        }
                        is NetWorkState.StopLoading->{
                            if (onHideProgress==null) hideProgress() else onHideProgress()

                        }
                        is NetWorkState.Error->{
                            if (onError==null) handleErrorGeneral(networkState.th) else onError(networkState.th)
                        }

                        else ->{
                        }
                    }
                }
            }
        }
    }

    fun handleStateFlow(
        userFlow: StateFlow<NetWorkState>,
        onShowProgress: (() -> Unit)? = null,
        onHideProgress: (() -> Unit)? = null,
        onSuccess: (data: Any) -> Unit,
        onError: ((th: Throwable) -> Unit)? = null
    ){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userFlow.collectLatest {networkState->

                    when(networkState){
                        is NetWorkState.Loading->{
                            if (onShowProgress==null) showProgress() else onShowProgress()
                        }
                        is NetWorkState.StopLoading->{
                            if (onHideProgress==null) hideProgress() else onHideProgress()

                        }
                        is NetWorkState.Success<*> ->{
                            onSuccess(networkState.data!!)
                        }
                        is NetWorkState.Error->{
                            if (onError==null) handleErrorGeneral(networkState.th) else onError(networkState.th)

                        }
                        is NetWorkState.Idle->{}
                    }
                }
            }
        }
    }
}