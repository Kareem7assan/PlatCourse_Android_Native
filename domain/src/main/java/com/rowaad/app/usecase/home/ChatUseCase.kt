package com.rowaad.app.usecase.home

import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.model.chanel_model.Message
import com.rowaad.app.data.model.chanel_model.MessagesModel
import com.rowaad.app.data.model.chanel_model.RoomChat
import com.rowaad.app.data.model.orders.CategoriesItem
import com.rowaad.app.data.model.tweets_model.PaginationInfo
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.chat.ChatRepository
import com.rowaad.app.data.repository.home.HomeRepository
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class ChatUseCase @Inject constructor(private val baseRepository: BaseRepository,
                                      private val repository: ChatRepository) {

    //rooms
    suspend fun rooms(page:Int?=1): Flow<Pair<List<RoomChat>?,PaginationInfo?>> {
        return repository.rooms(page!!).transformResponseData {
            emit(Pair(it.records,it.paginationInfo))
        }
    }

    //messages
    suspend fun messages(
            otherId:Int, tweet:String
    ): Flow<MessagesModel?>{
        return repository.messages(otherId, tweet).transformResponseData {
            emit(it)
        }
    }

    //send message
    suspend fun sendMsg(
            otherId:Int, tweetId:Int,message:String?=null,
            files:List<MultipartBody.Part>?=null
    ): Flow<Message?>{
        return repository.sendMsg(otherId = otherId,tweet = tweetId,
                message = message,type = if (files.isNullOrEmpty().not()) "image" else "text",userType =  "customers",files =  files).transformResponseData {
            emit(it.record)
        }
    }


    //user
    val user=baseRepository.loadUser()


}