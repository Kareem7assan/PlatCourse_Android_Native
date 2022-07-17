package com.rowaad.app.data.repository.chat

import com.rowaad.app.data.cache.PreferencesGateway
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.PaymentMethodModel
import com.rowaad.app.data.model.WalletModel
import com.rowaad.app.data.model.categories_model.CategoriesModel
import com.rowaad.app.data.model.chanel_model.CustomerChat
import com.rowaad.app.data.model.chanel_model.MessageModel
import com.rowaad.app.data.model.chanel_model.MessagesModel
import com.rowaad.app.data.model.chanel_model.RoomsModel
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

class ChatRepositoryImp @Inject constructor(
    private val api: UserApi,
    private val db: PreferencesGateway,
    private val baseRepository: BaseRepository,
): ChatRepository {
    override fun rooms(page:Int): Flow<Response<EndPointResponse<RoomsModel>>> {
        return flow { emit(api.channels(page)) }
    }

    override fun messages(otherId: Int, tweet: String): Flow<Response<EndPointResponse<MessagesModel>>> {
        return flow { emit(api.messages(otherId, tweet)) }

    }

    override  fun sendMsg(otherId:Int, tweet:Int, message:String?,type:String?,
                          userType:String?,files:List<MultipartBody.Part>?): Flow<Response<EndPointResponse<MessageModel>>> {
        return if (files.isNullOrEmpty()) flow { emit(api.sendMsg(otherId, tweet, message, type, userType)) }
        else flow { emit(api.sendMsg(otherId = otherId,tweet =  tweet, message = message, type = type, userType = userType, files =  files)) }
    }
}