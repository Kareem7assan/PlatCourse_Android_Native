package com.rowaad.app.usecase.profile

import com.rowaad.app.data.model.UserModel
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