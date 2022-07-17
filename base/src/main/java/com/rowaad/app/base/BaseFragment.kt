package com.rowaad.app.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.dialogs_utils.NoInternetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class BaseFragment(private val layout:Int): Fragment() {

    private  var fragmentView: ViewGroup?=null
    private var baseActivity: BaseActivity? = null
    private var progressDialog: ACProgressFlower? = null
     private val baseViewModel: BaseViewModel by activityViewModels()
    var savedInstanceState: Bundle? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
      /*  this.savedInstanceState = savedInstanceState
        try {
            fragmentView = LayoutInflater.from(activity)
                    .inflate(layout, container, false) as ViewGroup
            //view?.findViewById<ViewGroup>(R.id.layout_base_view)?.addView(fragmentView)
        } catch (e: Exception) {

        }
        return fragmentView!!.rootView*/

        return inflater.inflate(layout, container, false)
    }




    fun setTitleToolbar(tvTitle:TextView,title:String){
        tvTitle.text=title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentView=null
    }

    protected fun showSuccessMsg(title:String){
        baseActivity?.showSuccessMsg(title)
    }

    protected fun showErrorMessage(title:String){
        baseActivity?.showErrorMsg(title)

    }


    protected fun showProgress() {
        showProgressDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
                val activity = context as BaseActivity?
                this.baseActivity = activity
        }
    }

    protected fun showProgressFullScreen(){
        baseActivity?.showProgressFullScreen()
    }

    protected fun hideProgressFullScreen(fragmentRecords: Int) {
        baseActivity?.hideProgressFullScreen(fragmentRecords)
    }

    override fun onDetach() {
        super.onDetach()
        baseActivity=null
    }


    protected fun handleErrorGeneral(th: Throwable, func: (() -> Unit)?=null) {
       baseActivity?.handleErrorGeneral(th, func)
    }


    private fun showProgressDialog() {
            progressDialog = ACProgressFlower.Builder(activity)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.GRAY)
                    .fadeColor(Color.WHITE)
                    .build()
        if (progressDialog?.isShowing==false) {
            progressDialog!!.show()
        }
    }

    protected fun hideProgress() {
        hideProgressDialog()
    }

    private fun hideProgressDialog() {
            progressDialog?.hide()
            progressDialog?.dismiss()
            progressDialog?.cancel()

    }

    fun <T> Fragment.handleFlow(userFlow: Flow<T>,lifeCycle:Lifecycle.State=Lifecycle.State.STARTED,onAction:(flow:T)->Unit){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(lifeCycle) {
                userFlow.collect {flow->
                    onAction.invoke(flow)
                }
            }
        }
    }

    fun Fragment.handleSharedFlow(userFlow: SharedFlow<NetWorkState>,lifeCycle:Lifecycle.State=Lifecycle.State.STARTED,onShowProgress:(()->Unit)?=null, onHideProgress:(()->Unit)?=null, onSuccess:(data:Any)->Unit , onError:((th:Throwable)->Unit)?=null){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(lifeCycle) {
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

    fun Fragment.handleStateFlow(userFlow: StateFlow<NetWorkState>,lifeCycle:Lifecycle.State=Lifecycle.State.STARTED, onShowProgress:(()->Unit)?=null, onHideProgress:(()->Unit)?=null, onSuccess:(data:Any)->Unit , onError:((th:Throwable)->Unit)?=null){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(lifeCycle) {
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
