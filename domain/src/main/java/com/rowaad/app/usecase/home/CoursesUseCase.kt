package com.rowaad.app.usecase.home

import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.teacher_model.TeacherModel
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.home.HomeRepository
import com.rowaad.app.usecase.transformResponse
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CoursesUseCase @Inject constructor(private val baseRepository: BaseRepository,
                                         private val repository: HomeRepository
                                         ) {

    suspend fun allCourses(page:Int?=1): Flow<Pair<CoursesModel,CoursesModel>> {
         return newCourses(1).zip(featuredCourses(1)) {newCoursesModel, featuredCoursesModel ->
                Pair(newCoursesModel,featuredCoursesModel)
         }
    }
     suspend fun newCourses(page:Int?=1): Flow<CoursesModel> {
        return repository.newCourses(page)
                .transformResponse { emit(it) }
    }

     suspend fun featuredCourses(page:Int?=1): Flow<CoursesModel> {
        return repository.featuredCourses(page)
                .transformResponse { emit(it) }
    }
    suspend fun myCourses(page:Int?=1): Flow<CoursesModel> {
        return repository.myCourses(page)
                .transformResponse { emit(it) }
    }
    suspend fun pendingCourses(page:Int?=1): Flow<CoursesModel> {
        return repository.pendingCourses(page)
                .transformResponse { emit(it) }
    }
    suspend fun searchCourse(title:String,page:Int?=1): Flow<CoursesModel> {
        return repository.searchCourse(title,page)
                .transformResponse { emit(it) }
    }
    suspend fun coursesBasedCategories(category:Int?=null,subCategory:Int?=null,page:Int?=1): Flow<CoursesModel> {
        return repository.coursesBasedCat(category,subCategory,page)
                .transformResponse { emit(it) }
    }
    suspend fun buyCourse(courseId:Int): Flow<Any> {
        return repository.buyCourse(courseId)
                .transformResponse { emit(it) }
    }
    suspend fun teacher(ownerId:Int): Flow<UserModel> {
        return repository.teacher(ownerId)
                .transformResponse { emit(it) }
    }
    suspend fun contactTeacher(courseId:Int,msg:String): Flow<Any> {
        return repository.contactTeacher(courseId,msg)
                .transformResponse { emit(it) }
    }




}