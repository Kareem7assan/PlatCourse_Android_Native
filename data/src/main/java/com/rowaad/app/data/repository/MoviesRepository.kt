package com.rowaad.app.data.repository

import com.kareem.dietDelivery.data.model.movies_model.Movie
import com.kareem.dietDelivery.data.model.movies_model.MoviesModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MoviesRepository {

    suspend fun getMovies(page:Int): Flow<Response<MoviesModel>>
    suspend fun saveMovies(movies: List<Movie>)

}