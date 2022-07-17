package com.rowaad.app.data.repository.home

import com.rowaad.app.data.cache.PreferencesGateway
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.PaymentMethodModel
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
import com.rowaad.app.data.remote.UserApi
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.base.BaseRepositoryImpl
import com.rowaad.app.data.utils.Constants_Api
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.STATUS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(
    private val api: UserApi,
    private val db: PreferencesGateway,
    private val baseRepository: BaseRepository,
): HomeRepository {
    override fun allCategories(): Flow<Response<EndPointResponse<CategoriesModel>>> {
        return flow { emit(api.categories()) }
    }

    override fun subCategories(categoryId: Int): Flow<Response<EndPointResponse<CategoriesModel>>> {
        return flow { emit(api.subCategories(categoryId.toString())) }
    }

    override fun regions(): Flow<Response<EndPointResponse<RegionsModel>>> {
        return flow { emit(api.regions()) }
    }

    override fun packages(): Flow<Response<EndPointResponse<PackagesModel>>> {
        return flow { emit(api.myPackages()) }
    }

    override fun paymentMethods(): Flow<Response<EndPointResponse<PaymentMethodModel>>> {
        return flow { emit(api.paymentMethods()) }
    }

    override fun createTweetFree(title: String, category: Int, region: Int, city: Int, phone: String?, photos: List<MultipartBody.Part>?): Flow<Response<EndPointResponse<CreateTweetModel>>> {
        return if (photos.isNullOrEmpty()) flow { emit(api.createTweetFree(title, category, region, city, phone)) } else flow { emit(api.createTweetFree(title, category, region, city, phone,photos)) }
    }

    override fun createTweet(title: String, category: Int, region: Int, city: Int, phone: String?, photos: List<MultipartBody.Part>?, packageId: String, paymentMethod: String?, isPremium: String?): Flow<Response<EndPointResponse<CreateTweetModel>>> {
        return if (photos.isNullOrEmpty()) flow { emit(api.createTweet(title, category, region, city, phone,packageId, paymentMethod)) } else flow { emit(api.createTweet(title, category, region, city, phone,photos,packageId, paymentMethod)) }
    }

    override fun tweet(tweetId: String): Flow<Response<EndPointResponse<CreateTweetModel>>> {
        return flow { emit(api.tweetDetails(id = tweetId)) }
    }

    override fun addComments(comment: String, typeId: String): Flow<Response<EndPointResponse<Any>>> {
        return flow { emit(api.addComments(comment = comment,typeId = typeId)) }
    }

    override fun like(id: String): Flow<Response<EndPointResponse<Any>>> {
        return flow { emit(api.like(id = id)) }
    }

    override fun retweet(id: String): Flow<Response<EndPointResponse<Any>>> {
        return flow { emit(api.retweet(id = id)) }
    }

    override fun report(id: String, reason: String, description: String): Flow<Response<EndPointResponse<Any>>> {
        return flow { emit(api.report(id = id,reason = reason,description = description)) }
    }

    override fun updateTweet(tweetId: String,tweetStatus: String): Flow<Response<EndPointResponse<CreateTweetModel>>> {
        return flow { emit(api.tweetUpdate(tweetId,tweetStatus)) }
    }

    override fun myTweets(
        categoryId: Int?,
        regions: List<Int>?,
        city: List<Int>?,
        lat: Double?,
        lng: Double?,
        following:Int?,
        searchKey:String?,
        page:String?
    ): Flow<Response<EndPointResponse<TweetsModel>>> {
        return flow { emit(api.myTweets(category = categoryId,lat = lat, lng = lng, regions =  regions, city =  city,following = following,title = searchKey,page = page!!)) }
    }

    override fun myLikes(page: String): Flow<Response<EndPointResponse<TweetsModel>>> {
        return flow { emit(api.userLikes(baseRepository.loadUser()?.id ?: 0,page)) }
    }

    override fun userLikes(userId: Int,page: String): Flow<Response<EndPointResponse<TweetsModel>>> {
        return flow { emit(api.userLikes(userId,page)) }
    }


}