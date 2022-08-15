package com.rowaad.app.usecase.categories

import com.rowaad.app.data.model.categories_model.CategoriesModel
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.home.HomeRepository
import com.rowaad.app.usecase.transformResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import javax.inject.Inject


class CategoriesUseCase @Inject constructor(private val baseRepository: BaseRepository,
                                         private val repository: HomeRepository) {

    suspend fun categories(): Flow<CategoriesModel> {
        return repository.allCategories()
                .transformResponse { emit(it) }
    }



}