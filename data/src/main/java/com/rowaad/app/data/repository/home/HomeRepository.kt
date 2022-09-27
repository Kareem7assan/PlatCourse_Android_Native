package com.rowaad.app.data.repository.home

import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.categories_model.CategoriesModel
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.app.data.model.teacher_model.TeacherModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.*


interface HomeRepository{
    fun allCategories(): Flow<Response<CategoriesModel>>
    fun newCourses(page:Int?): Flow<Response<CoursesModel>>
    fun myCourses(page:Int?): Flow<Response<CoursesModel>>
    fun pendingCourses(page:Int?): Flow<Response<CoursesModel>>
    fun searchCourse(title:String?,page:Int?): Flow<Response<CoursesModel>>
    fun featuredCourses(page:Int?): Flow<Response<CoursesModel>>
    fun coursesBasedCat(categoryId:Int?=null, subCategoryId:Int?=null, page:Int?=1): Flow<Response<CoursesModel>>
    fun contactTeacher(courseId:Int, message:String): Flow<Response<Any>>
    fun buyCourse(courseId:Int): Flow<Response<Any>>
    fun teacher(ownerId:Int): Flow<Response<UserModel>>

}