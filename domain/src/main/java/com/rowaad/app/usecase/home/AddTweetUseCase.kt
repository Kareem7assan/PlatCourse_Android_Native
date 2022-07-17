package com.rowaad.app.usecase.home

import android.util.Log
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.ImageDoc
import com.rowaad.app.data.model.PaymentMethodModel
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.bank_accounts_model.BankAccountsModel
import com.rowaad.app.data.model.bank_accounts_model.RecordsItem
import com.rowaad.app.data.model.orders.CategoriesItem
import com.rowaad.app.data.model.orders.PaymentMethodInfo
import com.rowaad.app.data.model.packages_model.PackagesModel
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

class AddTweetUseCase @Inject constructor(private val baseRepository: BaseRepository,
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


    fun isRegister():Boolean=baseRepository.isLogin()
    //get home

    suspend fun home(catId: Int?=null,city:List<Int>? = null,lat:Double?=null,regions:List<Int>? = null,lng:Double?=null,following:Boolean?=null,searchKey:String?=null,page:Int?=1): Flow<Pair<List<Tweets>?,PaginationInfo?>> {
        return repository.myTweets(catId,regions,city,lat, lng,if (following==true) 1 else null,searchKey).transformResponseData {
            emit(Pair(it.records,it.paginationInfo))
        }
    }

    //get regions
    suspend fun regions(): Flow<List<Region>?> {
        return repository.regions().transformResponseData {
            emit(it.records)
        }
    }

    //get packages
    suspend fun packages(): Flow<PackagesModel> {
        return repository.packages().transformResponseData {
            emit(it)
        }
    }

    //get methods
    suspend fun paymentMethods(): Flow<List<PaymentMethodInfo>> {
        return repository.paymentMethods().transformResponseData {
            emit(it.records)
        }
    }


    //get methods
    suspend fun createTweetFree(title:String,category:Int, region:Int, city:Int, phone:String?=null, photos: List<MultipartBody.Part>?=null): Flow<Tweets?> {
        Log.e("data_useCase",photos?.map { it.body }.toString())
        return repository.createTweetFree(title, category, region, city, phone, photos).transformResponseData {
            emit(it.record)
        }
    }

    //get methods
    suspend fun createTweetFreePackage(title:String,category:Int, region:Int, city:Int, phone:String?=null, photos: List<MultipartBody.Part>?=null,packageId:String): Flow<Tweets?> {
        return repository.createTweet(title = title, category = category, region = region, city = city, phone = phone, photos = photos,packageId,paymentMethod = null,isPremium = "1").transformResponseData {
            emit(it.record)
        }
    }
    //get methods
    suspend fun createTweetPaid(title:String,category:Int, region:Int, city:Int, phone:String?=null, photos: List<MultipartBody.Part>?=null,packageId:String,paymentMethod:String?=null): Flow<Tweets?> {
        return repository.createTweet(title, category, region, city, phone, photos,packageId,paymentMethod = paymentMethod,isPremium = "1").transformResponseData {
            emit(it.record)
        }
    }

}





