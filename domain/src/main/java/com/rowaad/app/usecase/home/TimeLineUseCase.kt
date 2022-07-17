package com.rowaad.app.usecase.home

import android.util.Log
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.ImageDoc
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.bank_accounts_model.BankAccountsModel
import com.rowaad.app.data.model.bank_accounts_model.RecordsItem
import com.rowaad.app.data.model.create_order.CreateTweetModel
import com.rowaad.app.data.model.orders.CategoriesItem
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.tweets_model.PaginationInfo
import com.rowaad.app.data.model.tweets_model.Region
import com.rowaad.app.data.model.tweets_model.Tweets
import com.rowaad.app.data.model.tweets_model.TweetsModel
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.home.HomeRepository
import com.rowaad.app.data.repository.user.AuthRepository
import com.rowaad.app.usecase.Validations
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class TimeLineUseCase @Inject constructor(private val baseRepository: BaseRepository,
                                          private val repository: HomeRepository) {

    //get categories
    suspend fun categories(): Flow<List<CategoriesItem>?> {
        return repository.allCategories().transformResponseData {
            emit(it.categories)
        }
    }


    //get subcategories
    suspend fun subCategories(catId:Int): Flow<List<CategoriesItem>?> {
        return repository.subCategories(catId).transformResponseData {
            emit(it.categories)
        }
    }


    //get tweets
    suspend fun home(catId: Int?=null,city:List<Int>? = null,lat:Double?=null,regions:List<Int>? = null,lng:Double?=null,following:Boolean?=null,searchKey:String?=null,page:Int?=1): Flow<Pair<List<Tweets>?,PaginationInfo?>> {
        return repository.myTweets(catId,regions,city,lat, lng,if (following==true) 1 else null,searchKey,page = page.toString()).transformResponseData {
            emit(Pair(it.records,it.paginationInfo))
        }
    }

    //get tweetDetails
    suspend fun tweetDetails(tweetId:String): Flow<Tweets> {
        return repository.tweet(tweetId = tweetId).transformResponseData {
            emit(it.record!!)
        }
    }

    //add comment
    suspend fun addComment(tweetId:String,comment:String): Flow<Any> {
        return repository.addComments(comment = comment,typeId = tweetId).transformResponseData {
            emit(it)
        }
    }

    //add like
    suspend fun like(tweetId:String): Flow<Any> {
        return repository.like(tweetId).transformResponseData {
            emit(it)
        }
    }
    //get like
    suspend fun favourites(page:Int?=1): Flow<Pair<List<Tweets>?,PaginationInfo?>> {
        return repository.myLikes(page = page.toString()).transformResponseData {
            emit(Pair(it.records,it.paginationInfo))
        }
    }

    //add retweet
    suspend fun retweet(tweetId:String): Flow<Any> {
        return repository.retweet(tweetId).transformResponseData {
            emit(it)
        }
    }



    //block & stop
    suspend fun soldStopTweet(status:String,tweetId:String): Flow<Boolean> {
        return repository.updateTweet(tweetStatus = status,tweetId = tweetId).transformResponseData {
            emit(it.success ?: true)
        }
    }

    fun userData():UserModel?{
        return baseRepository.loadUser()
    }


    fun isGuestUser():Boolean = baseRepository.isLogin().not()



    //get regions
    suspend fun regions(): Flow<List<Region>?> {
        return repository.regions().transformResponseData {
            emit(it.records)
        }
    }

}





