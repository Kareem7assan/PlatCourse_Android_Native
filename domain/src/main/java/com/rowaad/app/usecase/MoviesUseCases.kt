/*
package com.rowaad.app.usecase

import com.kareem.dietDelivery.data.model.movies_model.Movie
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MoviesUseCases @Inject constructor(private val repository: MoviesRepository) {

  suspend fun showAllMovies(page:Int): Flow<List<Movie>> {
    return repository.getMovies(page)
            .transform { emit(it.body()?.results!!) }
            .onEach { repository.saveMovies(it) }


  }
}



*/
