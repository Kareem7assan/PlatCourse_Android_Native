package com.rowaad.app.base

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.utils.Constants_Api
import com.rowaad.utils.extention.handleException
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class BaseViewModel :ViewModel() {

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface BaseViewModelEntryPoint {
        val baseRepository: BaseRepository
    }
    private val parentJob = Job()

    private val _unAuthorizedFlow= MutableSharedFlow<Boolean>()
    internal val unAuthorizedFlow= _unAuthorizedFlow.asSharedFlow()
    private val _maintenanceFlow= MutableSharedFlow<Boolean>()
     internal val maintenanceFlow= _maintenanceFlow.asSharedFlow()

    private val _connectionErrorFlow= MutableSharedFlow<Boolean>()
     internal val connectionErrorFlow= _connectionErrorFlow.asSharedFlow()

     fun getBaseRepository(appContext: Context): BaseRepository = EntryPointAccessors
                                                                             .fromApplication(appContext, BaseViewModelEntryPoint::class.java)
                                                                             .baseRepository

    private  fun handler(state: MutableStateFlow<NetWorkState>): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { coroutineContext, throwable ->
            when (throwable.message){
                Constants_Api.ERROR_API.UNAUTHRIZED -> {
                        _unAuthorizedFlow
                                .tryEmit(true)


                }
                Constants_Api.ERROR_API.MAINTENANCE -> {

                        _maintenanceFlow.tryEmit(true)
                }

               /* Constants_Api.ERROR_API.CONNECTION_ERROR -> {
                    Log.e("connectionErrorHandler",throwable.handleException().message.toString())

                    // _connectionErrorFlow.tryEmit(true)
                }*/


                else -> {
                    Log.e("error..","3"+","+Constants_Api.ERROR_API.UNAUTHRIZED)

                        state.value = NetWorkState.Error(throwable.handleException())

                }

            }
        }

    }
    private fun handlerShared(state: MutableSharedFlow<NetWorkState>): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { coroutineContext, throwable ->
            //Log.e("error..",throwable.message.toString())
            when (throwable.message){
                Constants_Api.ERROR_API.UNAUTHRIZED -> {
                    _unAuthorizedFlow.tryEmit(true)
                }
                Constants_Api.ERROR_API.MAINTENANCE -> {
                    _maintenanceFlow.tryEmit(true)
                }

               /* Constants_Api.ERROR_API.CONNECTION_ERROR -> {
                    Log.e("connectionErrorShared",throwable.handleException().message.toString())
                    _connectionErrorFlow.tryEmit(true)
                }*/
                else -> {

                        state.tryEmit(NetWorkState.Error(throwable.handleException()))

                }

            }
        }

    }



    fun executeApi(state: MutableStateFlow<NetWorkState>, job:Job=parentJob, action:suspend()->Unit){
        viewModelScope.launch( handler(state) ) {
            action()
        }
    }


     fun executeSharedApi(state: MutableSharedFlow<NetWorkState>, job:Job=parentJob, action:suspend()->Unit){
        viewModelScope.launch(handlerShared(state)) {
            action()
        }
    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}


