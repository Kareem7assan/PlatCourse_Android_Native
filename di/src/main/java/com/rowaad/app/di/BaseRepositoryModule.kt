package com.rowaad.app.di

import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.base.BaseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class  BaseRepositoryModule {
    @Binds
    abstract fun providesBaseRepo(baseRepositoryModule: BaseRepositoryImpl): BaseRepository


}