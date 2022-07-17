package com.rowaad.app.di

import com.rowaad.app.data.repository.MoviesRepository
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.user.AuthRepository
import com.rowaad.app.usecase.MoviesUseCases
import com.rowaad.app.usecase.SplashUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {



    @Provides
    fun providesMoviesUseCase(moviesRepository: MoviesRepository): MoviesUseCases {
        return MoviesUseCases(moviesRepository)
    }


    @Provides
    fun providesSplashUseCase(baseRepository: BaseRepository,authRepository: AuthRepository): SplashUseCase {
        return SplashUseCase(baseRepository,authRepository)
    }


}

