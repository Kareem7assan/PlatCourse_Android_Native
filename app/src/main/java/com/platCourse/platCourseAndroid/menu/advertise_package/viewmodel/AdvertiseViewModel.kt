package com.platCourse.platCourseAndroid.menu.advertise_package.viewmodel

import android.location.Location
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.model.tweets_model.City
import com.rowaad.app.data.model.tweets_model.Region
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.home.TimeLineUseCase
import com.rowaad.app.usecase.menu.MenuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.math.ln


@HiltViewModel
open class AdvertiseViewModel @Inject constructor(private val menuUseCase: MenuUseCase) : BaseViewModel(){

    private val _subscriptionFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val subscriptionFlow = _subscriptionFlow.asSharedFlow()

    fun getSubscriptions(){
        executeApi(_subscriptionFlow){
            menuUseCase.subscriptions()
                    .onStart { _subscriptionFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _subscriptionFlow.emit(NetWorkState.StopLoading) }
                    .catch { _subscriptionFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest {resp->  _subscriptionFlow.emit(NetWorkState.Success(resp)) } }
    }



}