package com.rowaad.app.base

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.hardware.display.DisplayManager
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rowaad.app.base.databinding.ActivityBaseBinding
import com.rowaad.app.base.utils.UtilitySecurity
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.utils.Constants_Api
import com.rowaad.dialogs_utils.*
import com.rowaad.utils.extention.fromJson
import com.rowaad.utils.extention.toJson
import com.rowaad.utils.extention.toast
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

import java.net.Socket
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes


@AndroidEntryPoint
abstract class BaseActivity(private val layoutResource: Int): AppCompatActivity() {
     val uidValue: String? by lazy {
         Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
     }
    private  val ref: DatabaseReference by lazy {
         Firebase.database("https://platcourse-ce46f-default-rtdb.firebaseio.com/").reference
    }
    private var registeredBefore: Boolean = false
    private var screeningBefore: Boolean = false
    private var viewBase: ActivityBaseBinding? = null
    private var progressDialog: ACProgressFlower? = null
    var savedInstanceState: Bundle? = null

    private val baseViewModel: BaseViewModel by viewModels()

    private val listener =
        object : DisplayManager.DisplayListener {
            override fun onDisplayChanged(displayId: Int) {
                Log.e("displayChange", displayId.toString())
            }

            override fun onDisplayAdded(displayId: Int) {
                // here stop video player
                if (UtilitySecurity.haveManyDisplays(applicationContext)){
                    Log.e("exc", "1")
                   // addValue(PlatApp(true, uidValue ?: ""))
                    manyScreenPolicy()
                }
                else{
                   // addValue(PlatApp(true, uidValue ?: ""))

                    voilantPolicy()
                }
            }

            override fun onDisplayRemoved(displayId: Int) {
                removeValue(uidValue!!)
            }
        }


    open fun setFullScreen(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

    }
    abstract fun init()
    open fun setActions(){}

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface BaseActivityEntryPoint {
        val baseRepository: BaseRepository
    }

    fun applicationIsInstall(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }


    }
    open fun isAppRunning(context: Context, packageName: String?): Boolean {
        val activityManager: ActivityManager =
            context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        var res=false
        try {
            Socket("google.com", 4444).close()

        }
         catch (ex: Exception){

            res=true
        }
        Log.e("appName", res.toString())
        val procInfos: List<ActivityManager.RunningAppProcessInfo> =
            activityManager.runningAppProcesses
            for (processInfo in procInfos) {
                Log.e("appName", processInfo.processName + "\n")
                if (processInfo.processName.contains(packageName.toString(), true)) {
                    return true
                }
        }
        return false
    }


    fun addValue(value: Any){
        val rootRef = FirebaseDatabase.getInstance().reference
        val memberRef = rootRef.child("plat_app")
        memberRef.child(uidValue!!).setValue(value)
    }

    fun removeValue(uid: String){
        ref?.child("plat_app").child(uid).removeValue { error, ref ->
            intent.component = (ComponentName(
                "com.platcourse.platcourseapplication",
                "com.platCourse.platCourseAndroid.auth.splash.SplashActivity"
            ))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }
        /*equalTo(uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.setValue(null);

                toast("removed")
                intent.component = (ComponentName(
                    "com.platCourse.platCourseAndroid",
                    "com.platCourse.platCourseAndroid.auth.splash.SplashActivity"
                ))
                //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(Constants_Api.INTENT.LOGOUT, true)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })*/
    }

    fun showVisitorDialog(view: View, onAction: (() -> Unit)?) {
        val snack= Snackbar.make(
            view,
            getString(R.string.you_should_login_first),
            Snackbar.LENGTH_LONG
        )
                .setAction(getString(R.string.register)) {
                    onAction?.invoke()
                    intent.component = (ComponentName(
                        "com.platcourse.platcourseapplication",
                        "com.platCourse.platCourseAndroid.auth.splash.SplashActivity"
                    ))
                    //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra(Constants_Api.INTENT.LOGOUT, true)
                    startActivity(intent)
                }
                .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setActionTextColor(ContextCompat.getColor(this, R.color.off_white_four))

        val view = snack.view
        val textView = view.findViewById<TextView>(R.id.snackbar_text)
        val btn = view.findViewById<Button>(R.id.snackbar_action)
        textView.typeface = ResourcesCompat.getFont(this, R.font.regular)
        btn.typeface = ResourcesCompat.getFont(this, R.font.regular)
        snack.show()
    }
data class PlatApp(val isRecording: Boolean, val uid: String)
    fun getBaseRepository(appContext: Context): BaseRepository = EntryPointAccessors
        .fromApplication(appContext, BaseActivityEntryPoint::class.java)
        .baseRepository

    class Cli {
        @RequiresApi(Build.VERSION_CODES.O)
        @OptIn(ExperimentalTime::class)
        fun runCommand(cmd: String, workingDir: File = File("."), timeout: Duration = 3.minutes): String? {
            // this is modified from https://stackoverflow.com/a/52441962/940217
            return try {
                val parts = "\\s".toRegex().split(cmd)
                val proc = ProcessBuilder(*parts.toTypedArray())
                    .directory(workingDir)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start()

                proc.waitFor(timeout.inWholeSeconds, TimeUnit.SECONDS)
                proc.inputStream.bufferedReader().readText()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            getCustomizedContext(this, getBaseRepository(this).lang)

        if (getBaseRepository(this).isEnableDark)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
        //setFullScreen()
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
        observeConnection()
        checkEmulator()
        checkADB()
       // checkUsbConnected()
        //checkUserRegisteredBeforeInDB(uidValue)


        if (applicationIsInstall(this, "com.teamviewer.quicksupport.market")
            ||
            applicationIsInstall(
                this,
                "com.teamviewer.teamviewer.market.mobile"
            )
            ||
            applicationIsInstall(
                this,
                "com.splashtop.remote.business"
            )
            ||
            applicationIsInstall(
                this,
                "com.logmein.rescuemobile"
            )

        ){
            teamViewerPolicy()
        }


    }


     fun checkADB(fromHome:Boolean=false) {
         //toast(Settings.Global.getInt(contentResolver, Settings.Global.ADB_ENABLED).toString())
        if (Settings.Global.getInt(contentResolver, Settings.Global.ADB_ENABLED)!=0 || isWifiAdbEnabled()){
            intent.component = (ComponentName(
                "com.platcourse.platcourseapplication",
                "com.platCourse.platCourseAndroid.error.ErrorScreenActivity"
            ))
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(
                "error",
                "  قم بإغلاق وضع المطور (developer mode) ثم قم بفتح التطبيق مرة أخرى"
            )
            intent.putExtra(
                "adb",
                true
            )
            startActivity(intent)
            if (fromHome) finish()
        }

    }

    fun isWifiAdbEnabled(): Boolean {
        // "this" is your activity or context
        return Settings.Global.getInt(this.contentResolver, "adb_wifi_enabled", 0) != 0
    }

    private fun checkUsbConnected() {
        if (isConnected(this)){
            intent.component = (ComponentName(
                "com.platcourse.platcourseapplication",
                "com.platCourse.platCourseAndroid.error.ErrorScreenActivity"
            ))
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(
                "error",
                "  عذراً لا يسمح بتوصيل الusb أثناء استخدام التطبيق برجاء نزعها و اعادة فتح التطبيق"
            )

            startActivity(intent)
            finish()
        }
    }

    fun checkEmulator() {
        if (UtilitySecurity.CheckIsRealPhoneMain(this).not()) {
            intent.component = (ComponentName(
                "com.platcourse.platcourseapplication",
                "com.platCourse.platCourseAndroid.error.ErrorScreenActivity"
            ))
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(
                "error",
                "قد يتواجد على جهازك بعض البرامج او التطبيقات التى لا تتوافق مع شروط و احكام و خصوصية التطبيق، يرجى حذفها و اعادة المحاولة."
            )

            addValue(PlatApp(false, uidValue ?: ""))
            startActivity(intent)
            finish()




        }
    }

     private fun checkUserRegisteredBeforeInDB(uidValue: String?) {

        ref?.child("plat_app")?.child(uidValue!!)?.get()?.addOnSuccessListener {
            val json = it.value.toJson()
            val user = json.fromJson<PlatApp?>()
                registeredBefore = user != null
                if (registeredBefore) screeningBefore = user?.isRecording ?: false
            }


            if (registeredBefore && screeningBefore) manyScreenPolicy()
            else if (registeredBefore) voilantPolicy()
            else checkEmulator()

        }


     fun voilantPolicy() {
         intent.component = (ComponentName(
             "com.platcourse.platcourseapplication",
             "com.platCourse.platCourseAndroid.error.ErrorScreenActivity"
         ))
         intent.putExtra(
             "error", "عذرا لا يسمح بالتسجيل لقد قمت بانتهاك سياسة خصوصية التطبيق."
         )
         startActivity(intent)
         finish()
    }
     fun teamViewerPolicy() {
         intent.component = (ComponentName(
             "com.platcourse.platcourseapplication",
             "com.platCourse.platCourseAndroid.error.ErrorScreenActivity"
         ))
         intent.putExtra(
             "error",
             "عذرا لا يسمح بإستخدام teamViewer أو برنامج يتيح مشاركة جهازك مع أجهزة اخري برجاء حذفه ثم اعادة التشغيل."
         )
         startActivity(intent)
         finish()
    }
     fun manyScreenPolicy() {
         intent.component = (ComponentName(
             "com.platcourse.platcourseapplication",
             "com.platCourse.platCourseAndroid.error.ErrorScreenActivity"
         ))
         intent.putExtra(
             "error", "لا يسمح بتشغيل التطبيق على شاشة خارجية."
         )
         startActivity(intent)
         finish()
    }

    override fun onResume() {
        super.onResume()
        val displayManager = applicationContext.getSystemService(DISPLAY_SERVICE) as DisplayManager
        displayManager.registerDisplayListener(listener, null)
        checkADB()
        //checkUsbConnected()

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
                    if (it) NoInternetDialog.show(this@BaseActivity, {})


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


    private fun inflateMain(layoutResource: Int) {
        //viewBase?.flProgress?.show()
        viewBase?.flContent?.removeAllViewsInLayout()

        val progress = LayoutInflater.from(this)
                .inflate(layoutResource, viewBase?.flContent, false) as ViewGroup
        viewBase?.flContent?.addView(progress)
    }


     fun showSuccessMsg(title: String){
        SuccessToast.showToast(this, title)
    }
     fun showErrorMsg(title: String){
        ErrorDialog.show(this, title)
    }

    private fun observeUnAuthorized() {
        GlobalScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                baseViewModel.unAuthorizedFlow
                        .collect {
                    if (it) handleUnAuthorized().also {
                        getBaseRepository(this@BaseActivity).clearCache()
                    }
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

    fun isConnected(context: Context): Boolean {
        intent =
            context.registerReceiver(null, IntentFilter("android.hardware.usb.action.USB_STATE"))
        return intent.extras!!.getBoolean("connected")
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




   fun handleErrorGeneral(th: Throwable, func: (() -> Unit)? = null) {
       //Log.e("error",th.message.toString())
       th.printStackTrace()
        when (th.message) {
            Constants_Api.ERROR_API.BAD_REQUEST -> {
                ErrorDialog.show(this, getString(R.string.some_error))
            }
            Constants_Api.ERROR_API.NOT_FOUND -> {
                ErrorDialog.show(this, getString(R.string.some_error))
            }
            /*Constants_Api.ERROR_API.UNAUTHRIZED -> {
                baseViewModel.getBaseRepository(this).logout().also { handleUnAuthorized() }
            }*/
            Constants_Api.ERROR_API.MAINTENANCE -> {
                handleMaintenance()
            }
            Constants_Api.ERROR_API.CONNECTION_ERROR -> {
                NoInternetDialog.show(this@BaseActivity)
            }
            Constants_Api.ERROR_API.UNAUTHRIZED -> {
                handleUnAuthorized().also {
                    getBaseRepository(this).clearCache()
                }
            }
            else -> {
                //ErrorDialog.show(this, getString(R.string.some_error))
                ErrorDialog.show(this, th.message!!)

            }
        }
    }

     fun handleMaintenance(func: (() -> Unit)? = null) {
        if (func != null) MaintenanceDialog.show(this, func) else MaintenanceDialog.show(
            this
        )
    }

    private fun handleUnAuthorized() {
        Log.e("authorized", "authorized")
        UnAuthorizeDialog.show(
            activity = this,
            title = getString(R.string.you_have_unauthorized)
        ) {
            intent.component = (ComponentName(
                "com.platcourse.platcourseapplication",
                "com.platCourse.platCourseAndroid.auth.splash.SplashActivity"
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

     fun hideProgressFullScreen(res: Int){
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
                userFlow.collect { networkState->
                    when(networkState){
                        is NetWorkState.Success<*> -> {
                            onSuccess(networkState.data!!)
                        }
                        is NetWorkState.Loading -> {
                            if (onShowProgress == null) showProgress() else onShowProgress()
                        }
                        is NetWorkState.StopLoading -> {
                            if (onHideProgress == null) hideProgress() else onHideProgress()

                        }
                        is NetWorkState.Error -> {
                            if (onError == null) handleErrorGeneral(networkState.th) else onError(
                                networkState.th
                            )
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
                userFlow.collectLatest { networkState->

                    when(networkState){
                        is NetWorkState.Loading -> {
                            if (onShowProgress == null) showProgress() else onShowProgress()
                        }
                        is NetWorkState.StopLoading -> {
                            if (onHideProgress == null) hideProgress() else onHideProgress()

                        }
                        is NetWorkState.Success<*> -> {
                            onSuccess(networkState.data!!)
                        }
                        is NetWorkState.Error -> {
                            if (onError == null) handleErrorGeneral(networkState.th) else onError(
                                networkState.th
                            )

                        }
                        is NetWorkState.Idle -> {
                        }
                    }
                }
            }
        }
    }
}