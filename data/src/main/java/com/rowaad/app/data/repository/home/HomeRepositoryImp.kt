package com.rowaad.app.data.repository.home

import com.rowaad.app.data.cache.PreferencesGateway
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
    override fun newCourses(page: Int?): Flow<Response<CoursesModel>> {
        return flow {
            emit(api.newCourses(page))
        }
    }

    override fun featuredCourses(page: Int?): Flow<Response<CoursesModel>> {
        return flow {
            emit(api.featuredCourses(page))
        }
    }


}