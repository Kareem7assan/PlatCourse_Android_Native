package com.rowaad.app.data.repository.chat

import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.chanel_model.CustomerChat
import com.rowaad.app.data.model.chanel_model.MessageModel
import com.rowaad.app.data.model.chanel_model.MessagesModel
import com.rowaad.app.data.model.chanel_model.RoomsModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Query

interface ChatRepository {

         fun rooms(page:Int): Flow<Response<EndPointResponse<RoomsModel>>>

         fun messages(
                 otherId:Int, tweet:String
        ): Flow<Response<EndPointResponse<MessagesModel>>>

        fun sendMsg(otherId:Int, tweet:Int, message:String?=null,type:String?="text",
                   userType:String?=null,files:List<MultipartBody.Part>?=null
        ): Flow<Response<EndPointResponse<MessageModel>>>


}