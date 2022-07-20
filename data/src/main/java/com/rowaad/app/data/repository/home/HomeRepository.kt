package com.rowaad.app.data.repository.home

import com.rowaad.app.data.model.courses_model.CoursesModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface HomeRepository{
    /*fun allCategories(): Flow<Response<EndPointResponse<CategoriesModel>>>*/
    fun newCourses(page:Int?): Flow<Response<CoursesModel>>
    fun featuredCourses(page:Int?): Flow<Response<CoursesModel>>

}