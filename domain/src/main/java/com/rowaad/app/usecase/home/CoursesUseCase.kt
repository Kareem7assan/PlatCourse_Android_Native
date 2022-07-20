package com.rowaad.app.usecase.home

import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.home.HomeRepository
import com.rowaad.app.usecase.transformResponse
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CoursesUseCase @Inject constructor(private val baseRepository: BaseRepository,
                                         private val repository: HomeRepository) {
/*
    val dateFlow = flowOf(repository.requestDate())
    val timeFlow = flowOf(repository.requestTime())
    val zippedFlow = dateFlow.zip(timeFlow) { date, time -> Result(date, time) }
*/

    suspend fun allCourses(page:Int?=1): Flow<Pair<CoursesModel,CoursesModel>> {
         return newCourses(1).zip(featuredCourses(1)) {newCoursesModel, featuredCoursesModel ->
                Pair(newCoursesModel,featuredCoursesModel)
         }
    }
    private suspend fun newCourses(page:Int?=1): Flow<CoursesModel> {
        return repository.newCourses(page)
                .transformResponse { emit(it) }
    }

    private suspend fun featuredCourses(page:Int?=1): Flow<CoursesModel> {
        return repository.featuredCourses(page)
                .transformResponse { emit(it) }
    }

    fun isUserLogin():Boolean{
        return baseRepository.isLogin()
    }
}