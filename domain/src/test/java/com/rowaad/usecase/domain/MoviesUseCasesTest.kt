package com.rowaad.usecase.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kareem.dietDelivery.data.model.movies_model.MoviesModel
import com.rowaad.app.data.repository.MoviesRepository
import com.rowaad.app.data.utils.Constants_Api
import com.rowaad.app.data.utils.Constants_Api.ERROR_API.UNAUTHRIZED
import com.rowaad.app.data.utils.handleError
import com.rowaad.app.usecase.MoviesUseCases
import com.rowaad.usecase.data.errorResponseBody
import com.rowaad.usecase.data.moviesMockResponse
import com.rowaad.usecase.data.moviesMockedList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
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
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class MoviesUseCasesTest {

    private lateinit var useCase: MoviesUseCases

    @Mock
    lateinit var fakeMoviesRepository: MoviesRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val expectedEx = ExpectedException.none();


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = MoviesUseCases(repository = fakeMoviesRepository)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `showAllMovies() when page has valid data then return Movies`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMovies(1)).thenReturn(flow { emit(Response.success(moviesMockResponse)) })
            //assert
            assertEquals(useCase.showAllMovies(1).first(), moviesMockedList)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `showAllMovies() when page has valid data then return error Movies 401`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMovies(1)).thenReturn((flow { emit(Response.error<MoviesModel>(401, errorResponseBody())) }))
            //assert
            expectedEx.expect(Throwable::class.java)
            expectedEx.expectMessage(UNAUTHRIZED)

            useCase.showAllMovies(1).first()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `showAllMovies() when page has valid data then return error Movies 500`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMovies(1)).thenReturn((flow { emit(Response.error<MoviesModel>(500, errorResponseBody())) }))
            //assert
            expectedEx.expect(Throwable::class.java)
            expectedEx.expectMessage(Constants_Api.ERROR_API.SERVER_ERROR)

            useCase.showAllMovies(1).first()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `showAllMovies() when page has valid data then return error Movies 400 BAD REQUEST`() {
        runBlockingTest {
            //arrange
            Mockito.`when`(fakeMoviesRepository.getMovies(1)).thenReturn((flow { emit(Response.error<MoviesModel>(400, errorResponseBody())) }))
            //assert
            expectedEx.expect(Throwable::class.java)

            expectedEx.expectMessage(errorResponseBody().handleError())
            print(errorResponseBody().handleError())
            //act
            useCase.showAllMovies(1).first()
        }
    }

}
