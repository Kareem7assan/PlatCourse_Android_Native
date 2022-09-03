package com.rowaad.app.data.repository.home

import com.rowaad.app.data.cache.PreferencesGateway
import com.rowaad.app.data.model.categories_model.CategoriesModel
import com.rowaad.app.data.model.courses_model.CoursesModel
import com.rowaad.app.data.remote.UserApi
import com.rowaad.app.data.repository.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(
    private val api: UserApi,
    private val db: PreferencesGateway,
    private val baseRepository: BaseRepository,
): HomeRepository {

    override fun allCategories(): Flow<Response<CategoriesModel>> {
        return flow {
            emit(api.categories())
        }
    }

    override fun newCourses(page: Int?): Flow<Response<CoursesModel>> {
        return flow {
            emit(api.newCourses(page))
        }
    }

    override fun myCourses(page: Int?): Flow<Response<CoursesModel>> {
        return flow {
            emit(api.myCourses(page))
        }
    }

    override fun searchCourse(title: String?, page: Int?): Flow<Response<CoursesModel>> {
        return flow {
            emit(api.searchCourses(title, page))
        }
    }

    override fun featuredCourses(page: Int?): Flow<Response<CoursesModel>> {
        return flow {
            emit(api.featuredCourses(page))
        }
    }


    override fun coursesBasedCat(
        categoryId: Int?,
        subCategoryId: Int?,
        page: Int?
    ): Flow<Response<CoursesModel>> {
        return flow {
            emit(api.coursesBasedCat(categoryId = categoryId,subCategoryId = subCategoryId,page = page))
        }
    }

    override fun contactTeacher(courseId: Int, message: String): Flow<Response<Any>> {
        return flow {
            emit(api.contactTeacher(courseId, message))
        }
    }

    override fun buyCourse(courseId: Int): Flow<Response<Any>> {
        return flow {
            emit(api.buyCourse(courseId))
        }
    }


}