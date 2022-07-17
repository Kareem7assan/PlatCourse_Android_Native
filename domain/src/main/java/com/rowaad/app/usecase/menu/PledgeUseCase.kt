package com.rowaad.app.usecase.menu

import android.util.Log
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.ImageDoc
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.bank_accounts_model.BankAccountsModel
import com.rowaad.app.data.model.bank_accounts_model.RecordsItem
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.tweets_model.Tweets
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.user.AuthRepository
import com.rowaad.app.usecase.Validations
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class PledgeUseCase @Inject constructor(private val baseRepository: BaseRepository, private val repository: AuthRepository) {

    //get banks
    suspend fun banks(): Flow<List<RecordsItem>?> {
        return repository.bankAccounts().transformResponseData {
            emit(it.records)
        }
    }

    //get my tweets
    suspend fun myTweets(): Flow<List<Tweets>?> {
        return repository.myTweets().transformResponseData {
            emit(it.records)
        }
    }
    //get my tweets
    suspend fun sendCommission(transName:String, description:String?=null,
                               paidMoney:String, bankAccount:String,
                               tweetId:String?=null, img: MultipartBody.Part?=null,
                               transferDate:String?=null

                               ): Flow<Any> {
        return repository.bankTransfer(
                transName, description, paidMoney, bankAccount, tweetId, img, transferDate
                ).transformResponseData {
            emit(it)
        }
    }


    fun isValidName(name: String): Boolean = Validations.isValidName(name)
    fun isValidAmount(amount: String): Boolean = Validations.isValidName(amount)
    fun isValidTransName(name: String): Boolean = Validations.isValidName(name)


    fun validateForm( name: String, amount: String, transName: String): Boolean {
        return when {
            isValidName(name).not() -> return false
            isValidAmount(amount).not() -> return false
            isValidTransName(transName).not() -> return false
            else -> true
        }
    }




}





