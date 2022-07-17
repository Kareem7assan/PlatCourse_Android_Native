package com.rowaad.app.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.remote.UserRegisterState
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.user.AuthRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SplashUseCaseAgainTest{
        private lateinit var useCase: SplashUseCase

        @Mock
        lateinit var fakeBaseRepository: BaseRepository
        @Mock
        lateinit var fakeAuthRepository: AuthRepository

        @get:Rule
        val instantExecutorRule = InstantTaskExecutorRule()

        @get:Rule
        val expectedEx = ExpectedException.none();


        @Before
        fun setUp() {
            MockitoAnnotations.openMocks(this)
            useCase = SplashUseCase(baseRepository = fakeBaseRepository,repository = fakeAuthRepository)
        }

        @Test
        fun `validateUserLogin when user login and approved`(){
            Mockito.`when`(fakeBaseRepository.isLogin()).thenReturn(true)
            fakeBaseRepository.saveUser(UserModel(approved = true,accessToken = "asdasd"))
            assertEquals(useCase.isLogin, UserRegisterState.Logged)
        }
    }
