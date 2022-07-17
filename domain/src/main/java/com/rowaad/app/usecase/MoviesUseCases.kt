package com.rowaad.app.usecase

import android.util.Log
import com.kareem.dietDelivery.data.model.movies_model.Movie
import com.kareem.dietDelivery.data.model.movies_model.MoviesModel
import com.rowaad.app.data.repository.MoviesRepository
import com.rowaad.app.data.utils.Constants_Api
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class MoviesUseCases @Inject constructor(private val repository: MoviesRepository) {

  suspend fun showAllMovies(page:Int): Flow<List<Movie>> {
    return repository.getMovies(page)
            .transform { emit(it.body()?.results!!) }
            .onEach { repository.saveMovies(it) }


  }
}



