package com.platCourse.platCourseAndroid.menu.bank_accounts

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.model.ImageDoc
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.menu.PledgeUseCase
import com.rowaad.app.usecase.register.RegisterUseCase
import com.platCourse.platCourseAndroid.auth.register.RegisterViewModel
import com.rowaad.utils.extention.isNullOrEmptyTrue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
open class BankViewModel @Inject constructor(private val pledgeUseCase: PledgeUseCase) : BaseViewModel(){


     var selectedTweetId: String?=null
     var transDate: String?=null

    private val _isValidNameFlow= MutableStateFlow<RegisterViewModel.Validation>(RegisterViewModel.Validation.Idle)
    val isValidNameFlow = _isValidNameFlow.asSharedFlow()

    private val _isValidAmountFlow= MutableStateFlow<RegisterViewModel.Validation>(RegisterViewModel.Validation.Idle)
    val isValidAmountFlow = _isValidAmountFlow.asSharedFlow()

   /* private val _isValidTransName= MutableStateFlow<RegisterViewModel.Validation>(RegisterViewModel.Validation.Idle)
    val isValidTransName = _isValidTransName.asSharedFlow()
*/

    private val _transFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val transFlow = _transFlow.asSharedFlow()

    private val _tweetFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val tweetFlow = _tweetFlow.asSharedFlow()

    private val _banksFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val banksFlow = _banksFlow.asSharedFlow()


    fun isValidNameFlow(name: String){
        _isValidNameFlow.value=(RegisterViewModel.Validation.IsValid(pledgeUseCase.isValidName(name)))
    }
    fun isValidAmountFlow(amount: String){
        _isValidAmountFlow.value=(RegisterViewModel.Validation.IsValid(pledgeUseCase.isValidAmount(amount)))
    }

    fun sendRequestTrans(name: String, amount: String,
                          bankAccount:String,desc:String?=null,
                         tweetId:String?=null, img: MultipartBody.Part?=null,
                         transferDate:String?=null){
        if (pledgeUseCase.validateForm(name, amount, bankAccount)){
            executeApi(_transFlow){
                pledgeUseCase.sendCommission(transName = name, description = desc,paidMoney = amount,
                        bankAccount = bankAccount,tweetId = tweetId, img = img, transferDate = transferDate)
                        .onStart { _transFlow.emit(NetWorkState.Loading) }
                        .onCompletion { _transFlow.emit(NetWorkState.StopLoading) }
                        .catch { _transFlow.emit(NetWorkState.Error(it.handleException())) }
                        .collectLatest {resp->  _transFlow.emit(NetWorkState.Success(resp)) } }
        }

    }
    fun sendRequestTweets(){
        executeApi(_tweetFlow){
                pledgeUseCase.myTweets()
                        .onStart { _tweetFlow.emit(NetWorkState.Loading) }
                        .onCompletion { _tweetFlow.emit(NetWorkState.StopLoading) }
                        .catch { _tweetFlow.emit(NetWorkState.Error(it.handleException())) }
                        .collectLatest {resp->  _tweetFlow.emit(NetWorkState.Success(resp)) } }

    }
    fun sendRequestBanks(){
        executeApi(_banksFlow){
                pledgeUseCase.banks()
                        .onStart { _banksFlow.emit(NetWorkState.Loading) }
                        .onCompletion { _banksFlow.emit(NetWorkState.StopLoading) }
                        .catch { _banksFlow.emit(NetWorkState.Error(it.handleException())) }
                        .collectLatest {resp->  _banksFlow.emit(NetWorkState.Success(resp)) } }

    }


}