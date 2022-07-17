package com.rowaad.app.di


import com.rowaad.app.data.repository.MoviesRepository
import com.rowaad.app.data.repository.MoviesRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MoviesRepositoryModule {
    @Binds
    abstract fun providesMovieRepo(moviesRepository: MoviesRepositoryImp): MoviesRepository

}