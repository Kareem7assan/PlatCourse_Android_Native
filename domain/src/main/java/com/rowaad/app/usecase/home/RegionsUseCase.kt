package com.rowaad.app.usecase.home

import com.rowaad.app.data.model.orders.CategoriesItem
import com.rowaad.app.data.model.tweets_model.Region
import com.rowaad.app.data.repository.base.BaseRepository
import com.rowaad.app.data.repository.home.HomeRepository
import com.rowaad.app.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegionsUseCase  @Inject constructor(private val baseRepository: BaseRepository,
                                          private val repository: HomeRepository) {

    //get regions
    suspend fun regions(): Flow<List<Region>?> {
        return repository.regions().transformResponseData {
            emit(it.records)
        }
    }

}