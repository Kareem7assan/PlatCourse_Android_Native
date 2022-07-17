package com.rowaad.app.usecase.profile

import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.tweets_model.PaginationInfo
import com.rowaad.app.data.model.tweets_model.Tweets
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.home.HomeRepository
import com.rowaad.app.data.repository.menu.MenuRepository
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val baseRepository: BaseRepository,
                                         private val repository: HomeRepository,
                                         private val menuRepository: MenuRepository,
){
    //profiles
    suspend fun userProfile(userId:Int): Flow<UserModel> {
        return menuRepository.profile(userId = userId)
            .transformResponseData {
                emit(it.userModel)
            }
    }

    //favourites
    suspend fun favourites(userId:Int,page:Int): Flow<Pair<List<Tweets>?,PaginationInfo?>> {
        return repository.userLikes(page = page.toString(),userId = userId)
            .transformResponseData {
                emit(Pair(it.records,it.paginationInfo))
            }
    }

    //tweets & replies
    suspend fun tweetsReplies(userId:Int,page: Int): Flow<Pair<List<Tweets>?,PaginationInfo?>> {
        return menuRepository.tweetsReplies(customerId = userId.toString(),page = page)
            .transformResponseData {
                emit(Pair(it.records,it.paginationInfo))
            }
    }

    //user tweets
    suspend fun userTweets(userId:Int,page: Int): Flow<Pair<List<Tweets>?,PaginationInfo?>> {
        return menuRepository.userTweets(customerId = userId.toString(),page = page)
            .transformResponseData {
                emit(Pair(it.records,it.paginationInfo))
            }
    }


    //follow */* unFollow
    suspend fun follow(userId:Int): Flow<Any> {
        return menuRepository.follow(customerId = userId.toString())
            .transformResponseData {
                emit(it)
            }
    }

    fun isGuestUser():Boolean{
        return baseRepository.isLogin().not()
    }
    fun myProfile():UserModel?{
        return baseRepository.loadUser()
    }
}