package com.rowaad.app.usecase.home

import com.rowaad.app.data.model.tweets_model.Region
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.home.HomeRepository
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportUseCase @Inject constructor(private val baseRepository: BaseRepository, private val repository: HomeRepository) {

    //post report
    suspend fun report(tweetId:String,reason:String,desc:String?=""): Flow<Any> {
        return repository.report(tweetId,reason,desc ?: "").transformResponseData {
            emit(it)
        }
    }

    fun isGuest():Boolean{
        return baseRepository.isLogin().not()
    }

}