package com.rowaad.app.data.repository

import com.kareem.dietDelivery.data.model.movies_model.Movie
import com.kareem.dietDelivery.data.model.movies_model.MoviesModel
import com.rowaad.app.data.cache.PreferencesGateway
import com.rowaad.app.data.remote.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class MoviesRepositoryImp @Inject constructor(
    private val api: UserApi,
    private val db: PreferencesGateway,
): MoviesRepository {

    override suspend fun getMovies(page:Int): Flow<Response<MoviesModel>> {
        return flow { emit(api.getLatestMovies(page = page)) }

    }

    override suspend fun saveMovies(movies: List<Movie>) {
        db.save("name",movies.first().title ?: "")
    }
}