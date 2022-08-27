package com.rowaad.app.usecase.home

import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.quiz_model.QuizModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.reviews.Review
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.course_details.CourseDetailsRepository
import com.rowaad.app.data.repository.home.HomeRepository
import com.rowaad.app.data.repository.menu.MenuRepository
import com.rowaad.app.usecase.transformResponse
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CourseDetailsUseCase @Inject constructor(private val baseRepository: BaseRepository,
                                               private val repository: MenuRepository,
                                               private val detailsRepository: CourseDetailsRepository
                                         ) {

    suspend fun fetchUser(): Flow<UserModel> {
        return repository.myProfile()
                .transformResponse { emit(it) }
    }

    suspend fun lessons(courseId:Int): Flow<List<LessonsModel>> {
        return detailsRepository.lessons(courseId)
                .transformResponse { emit(it) }
    }
    suspend fun reviews(courseId:Int): Flow<List<Review>> {
        return detailsRepository.reviews(courseId)
                .transformResponse { emit(it) }
    }

    suspend fun addReview(courseId:Int,rate:Float,comment:String?=null): Flow<Any> {
        return detailsRepository.addReview(courseId,rate,comment)
                .transformResponse { emit(it) }
    }

    suspend fun quizzes(courseId:Int): Flow<List<QuizModel>> {
        return detailsRepository.quizzes(courseId)
                .transformResponse { emit(it) }
    }


    fun isCourseOwner(courseId:Int,courses:List<Int>):Boolean{
        return courses.find { it==courseId } != null
    }

    fun isUserLogin():Boolean{
        return baseRepository.isLogin()
    }

    fun getUser():UserModel?{
        return baseRepository.loadUser()
    }

    val token:String = baseRepository.loadToken()


}