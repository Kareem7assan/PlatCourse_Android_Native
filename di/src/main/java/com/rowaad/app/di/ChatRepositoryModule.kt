package com.rowaad.app.di

import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.base.BaseRepositoryImpl
import com.rowaad.app.data.repository.chat.ChatRepository
import com.rowaad.app.data.repository.chat.ChatRepositoryImp
import com.rowaad.app.data.repository.user.AuthRepository
import com.rowaad.app.data.repository.user.AuthRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class  ChatRepositoryModule {
    @Binds
    abstract fun providesChatRepo(chatRepositoryImp: ChatRepositoryImp): ChatRepository


}