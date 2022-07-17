package com.rowaad.app.data.repository.home

import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.PaymentMethodModel
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.WalletModel
import com.rowaad.app.data.model.categories_model.CategoriesModel
import com.rowaad.app.data.model.create_order.CreateTweetModel
import com.rowaad.app.data.model.history_model.HistoryModel
import com.rowaad.app.data.model.map_location_model.MapLocationModel
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.orders.OrdersModel
import com.rowaad.app.data.model.packages_model.PackagesModel
import com.rowaad.app.data.model.reasons_model.ReasonsModel
import com.rowaad.app.data.model.regions_model.RegionsModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.settings.SettingsModel
import com.rowaad.app.data.model.single_order.SingleOrderModel
import com.rowaad.app.data.model.tweets_model.TweetsModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface HomeRepository{
    fun allCategories(): Flow<Response<EndPointResponse<CategoriesModel>>>
    fun subCategories(categoryId:Int): Flow<Response<EndPointResponse<CategoriesModel>>>
    fun regions(): Flow<Response<EndPointResponse<RegionsModel>>>
    fun packages(): Flow<Response<EndPointResponse<PackagesModel>>>
    fun paymentMethods(): Flow<Response<EndPointResponse<PaymentMethodModel>>>
    fun  createTweetFree(
            title:String, category:Int, region:Int, city:Int,
            phone:String?=null, photos: List<MultipartBody.Part>?=null
    ): Flow<Response<EndPointResponse<CreateTweetModel>>>

    fun  createTweet(
            title:String, category:Int, region:Int, city:Int,
            phone:String?=null, photos: List<MultipartBody.Part>?=null,
            packageId:String, paymentMethod:String?=null, isPremium:String?="1"
    ): Flow<Response<EndPointResponse<CreateTweetModel>>>

    fun  tweet(tweetId:String): Flow<Response<EndPointResponse<CreateTweetModel>>>

    fun  addComments(comment:String,typeId:String): Flow<Response<EndPointResponse<Any>>>

    fun like(
             id:String
    ): Flow<Response<EndPointResponse<Any>>>

    fun retweet(
            id:String
    ): Flow<Response<EndPointResponse<Any>>>

     fun report(id:String, reason:String, description:String): Flow<Response<EndPointResponse<Any>>>



    fun  updateTweet(tweetId:String,tweetStatus:String): Flow<Response<EndPointResponse<CreateTweetModel>>>

    fun myTweets(categoryId:Int?=null,regions:List<Int>? = null
                 ,city:List<Int>? = null, lat:Double?=null,lng:Double?=null,following:Int?=null,searchKey:String?=null
                 ,page:String?="1"
                 ): Flow<Response<EndPointResponse<TweetsModel>>>

    fun myLikes(page:String): Flow<Response<EndPointResponse<TweetsModel>>>
    fun userLikes(userId:Int,page:String): Flow<Response<EndPointResponse<TweetsModel>>>

}