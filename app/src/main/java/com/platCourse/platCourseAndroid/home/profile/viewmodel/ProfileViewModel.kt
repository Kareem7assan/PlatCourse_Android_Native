package com.platCourse.platCourseAndroid.home.profile.viewmodel

import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.home.AddTweetUseCase
import com.rowaad.app.usecase.profile.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
open class ProfileViewModel @Inject constructor(private val profileUseCase: ProfileUseCase) : BaseViewModel(){

    var userId:Int=profileUseCase.myProfile()?.id ?: 0

    private val _profileFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val profileFlow = _profileFlow.asSharedFlow()

    private val _favFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val favFlow = _favFlow.asSharedFlow()

    private val _tweetsFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val tweetsFlow = _tweetsFlow.asSharedFlow()

    private val _tweetRepliesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val tweetRepliesFlow = _tweetRepliesFlow.asSharedFlow()

    private val _followFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val followFlow = _followFlow.asSharedFlow()

    fun isMyProfile(userId:Int):Boolean{
        return profileUseCase.myProfile()?.id==userId

    }
    fun isGuestUser():Boolean{
        return profileUseCase.isGuestUser()

    }
    fun sendRequestProfile(){
        executeApi(_profileFlow){
            profileUseCase.userProfile(userId)
                .onStart { _profileFlow.emit(NetWorkState.Loading) }
                .onCompletion { _profileFlow.emit(NetWorkState.StopLoading) }
                .catch { _profileFlow.emit(NetWorkState.Error(it.handleException())) }
                .collectLatest {resp->  _profileFlow.emit(NetWorkState.Success(resp)) } }
    }

    fun sendRequestFavs(page:Int){
        executeApi(_favFlow){
            profileUseCase.favourites(userId,page)
                .onStart { _favFlow.emit(NetWorkState.Loading) }
                .onCompletion { _favFlow.emit(NetWorkState.StopLoading) }
                .catch { _favFlow.emit(NetWorkState.Error(it.handleException())) }
                .collectLatest {resp->  _favFlow.emit(NetWorkState.Success(resp)) } }
    }

    fun sendRequestTweets(page:Int){
        executeApi(_tweetsFlow){
            profileUseCase.userTweets(userId,page)
                .onStart { _tweetsFlow.emit(NetWorkState.Loading) }
                .onCompletion { _tweetsFlow.emit(NetWorkState.StopLoading) }
                .catch { _tweetsFlow.emit(NetWorkState.Error(it.handleException())) }
                .collect {resp->  _tweetsFlow.emit(NetWorkState.Success(resp)) } }
    }

    fun sendRequestReplies(page:Int){
        executeApi(_tweetRepliesFlow){
            profileUseCase.tweetsReplies(userId,page)
                .onStart { _tweetRepliesFlow.emit(NetWorkState.Loading) }
                .onCompletion { _tweetRepliesFlow.emit(NetWorkState.StopLoading) }
                .catch { _tweetRepliesFlow.emit(NetWorkState.Error(it.handleException())) }
                .collectLatest {resp->  _tweetRepliesFlow.emit(NetWorkState.Success(resp)) } }
    }
 fun sendRequestFollow(){
        executeApi(_followFlow){
            profileUseCase.follow(userId)
                .onStart { _followFlow.emit(NetWorkState.Loading) }
                .onCompletion { _followFlow.emit(NetWorkState.StopLoading) }
                .catch { _followFlow.emit(NetWorkState.Error(it.handleException())) }
                .collectLatest {resp->  _followFlow.emit(NetWorkState.Success(resp)) } }
    }

}
