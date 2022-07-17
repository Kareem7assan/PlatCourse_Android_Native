package com.platCourse.platCourseAndroid.home.search.viewmodel

import android.location.Location
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.model.orders.CategoriesItem
import com.rowaad.app.data.model.tweets_model.City
import com.rowaad.app.data.model.tweets_model.Region
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.home.TimeLineUseCase
import com.platCourse.platCourseAndroid.home.time_line.viewmodel.TimeLineViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
open class SearchViewModel @Inject constructor(private val timeLineUseCase: TimeLineUseCase) : BaseViewModel() {
    var searchKey:String?=null
    private val _tweetSearchFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val tweetSearchFlow = _tweetSearchFlow.asSharedFlow()
    var location: Location?=null
    val selectedRegions= mutableListOf<Region>()
    val selectedCities= mutableListOf<City>()
    var hasNearBy=false
    var isShowFollowers=false
    var parentCategory: CategoriesItem?=null

    private val _tweetFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val tweetFlow = _tweetFlow.asSharedFlow()

    private val _categoriesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val categoriesFlow = _categoriesFlow.asSharedFlow()

    private val _subCategoriesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val subCategoriesFlow = _subCategoriesFlow.asSharedFlow()
    private val _regionsFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val regionsFlow = _regionsFlow.asSharedFlow()

    fun sendRequestRegions(){
        executeApi(_regionsFlow){
            timeLineUseCase.regions()
                .onStart { _regionsFlow.emit(NetWorkState.Loading) }
                .onCompletion { _regionsFlow.emit(NetWorkState.StopLoading) }
                .catch { _regionsFlow.emit(NetWorkState.Error(it.handleException())) }
                .collectLatest {resp->  _regionsFlow.emit(NetWorkState.Success(resp)) } }
    }

    fun sendRequestTweetsSearch(catId: Int?=null,pageNumber:Int){
        executeApi(_tweetSearchFlow) {
            timeLineUseCase.home(catId = catId, city = if (selectedCities.isNullOrEmpty().not()) selectedCities.map { it.id!! } else null, lat =if (hasNearBy) location?.latitude else null, lng = if (hasNearBy) location?.longitude else null,
                regions = if (selectedRegions.isNullOrEmpty().not()) selectedRegions.map { it.id!! } else null, following = isShowFollowers,searchKey = searchKey, page = pageNumber)
                .onStart { _tweetSearchFlow.emit(NetWorkState.Loading) }
                .onCompletion { _tweetSearchFlow.emit(NetWorkState.StopLoading) }
                .catch { _tweetSearchFlow.emit(NetWorkState.Error(it.handleException())) }
                .collectLatest { resp -> _tweetSearchFlow.emit(NetWorkState.Success(resp)) }

        }

    }

    fun sendRequestCategories(){
        executeApi(_categoriesFlow){
            timeLineUseCase.categories()
                .onStart { _categoriesFlow.emit(NetWorkState.Loading) }
                .onCompletion { _categoriesFlow.emit(NetWorkState.StopLoading) }
                .catch { _categoriesFlow.emit(NetWorkState.Error(it.handleException())) }
                .collectLatest {resp->  _categoriesFlow.emit(NetWorkState.Success(resp)) } }
    }

    fun sendRequestSubCategories(catId:Int){
        executeApi(_subCategoriesFlow){
            timeLineUseCase.subCategories(catId)
                .onStart { _subCategoriesFlow.emit(NetWorkState.Loading) }
                .onCompletion { _subCategoriesFlow.emit(NetWorkState.StopLoading) }
                .catch { _subCategoriesFlow.emit(NetWorkState.Error(it.handleException())) }
                .collectLatest {resp->  _subCategoriesFlow.emit(NetWorkState.Success(resp)) } }
    }

    fun handleSearch(word:String,regions: List<Region>): List<Region> {
        return regions.filter {
            it.title?.contains(word) == true
        }
    }
    fun handleSearchCity(word:String,cities: List<City>): List<City> {
        return cities.filter {
            it.title?.contains(word) == true
        }
    }


}