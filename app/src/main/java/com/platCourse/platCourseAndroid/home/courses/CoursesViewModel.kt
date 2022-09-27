package com.platCourse.platCourseAndroid.home.courses

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.home.CourseDetailsUseCase
import com.rowaad.app.usecase.home.CoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
open class CoursesViewModel @Inject constructor(private val coursesUseCase: CoursesUseCase,
                                                private val courseDetailsUseCase: CourseDetailsUseCase) : BaseViewModel(){

    private var ownedCourses: MutableList<Int> = mutableListOf()
    private var token: String?=null

    init {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.e("token",it.toString())

            token=it
        }
    }


    fun isCourseOwner(courseId:Int):Boolean{
        return courseDetailsUseCase.isCourseOwner(courseId,ownedCourses)
    }

    private val _coursesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val coursesFlow= _coursesFlow.asSharedFlow()

    private val _lessonsFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val lessonsFlow= _lessonsFlow.asSharedFlow()

    private val _filesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val filesFlow= _filesFlow.asSharedFlow()

    private val _discFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val discFlow= _discFlow.asSharedFlow()

    private val _addDiscFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val addDiscFlow= _addDiscFlow.asSharedFlow()

    private val _addCommentFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val addCommentFlow= _addCommentFlow.asSharedFlow()

    private val _reviewsFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val reviewsFlow= _reviewsFlow.asSharedFlow()

    private val _rateFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val rateFlow= _rateFlow.asSharedFlow()

    private val _quizzesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val quizzesFlow= _quizzesFlow.asSharedFlow()


    private val _buyCourseFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val buyCourseFlow= _buyCourseFlow.asSharedFlow()


    private val _couponFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val couponFlow= _couponFlow.asSharedFlow()

    private val _couponPurchaseFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val couponPurchaseFlow= _couponPurchaseFlow.asSharedFlow()

    private val _contactFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val contactFlow= _contactFlow.asSharedFlow()


    private val _userFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
     val userFlow= _userFlow.asSharedFlow()


    fun sendRequestCourses(){
            executeApi(_coursesFlow){
                coursesUseCase.allCourses(1)
                    .onStart { _coursesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _coursesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _coursesFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _coursesFlow.emit(NetWorkState.Success(it)) }
            }
    }



    fun sendRequestUser(){
        if (isUserLogin()){
            executeApi(_userFlow) {
                courseDetailsUseCase.fetchUser()
                    .onStart { _userFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _userFlow.emit(NetWorkState.StopLoading) }
                    .catch { _userFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest {
                        ownedCourses = (it.courses ?: listOf<Int>()) as MutableList<Int>
                        _userFlow.emit(NetWorkState.Success(it))
                    }
            }

        }

    }
    fun sendRequestFeaturedCourses(pageNumber:Int){
            executeApi(_coursesFlow){
                coursesUseCase.featuredCourses(pageNumber)
                    .onStart { _coursesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _coursesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _coursesFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _coursesFlow.emit(NetWorkState.Success(it)) }
            }
    }

    fun sendRequestNewCourses(pageNumber:Int){
            executeApi(_coursesFlow){
                coursesUseCase.newCourses(pageNumber)
                    .onStart { _coursesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _coursesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _coursesFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _coursesFlow.emit(NetWorkState.Success(it)) }
            }
    }
    fun sendSearchCourses(key:String,pageNumber:Int){
            executeApi(_coursesFlow){
                coursesUseCase.searchCourse(key,pageNumber)
                    .onStart { _coursesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _coursesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _coursesFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _coursesFlow.emit(NetWorkState.Success(it)) }
            }
    }
    fun sendRequestLessons(courseId:Int){
            executeApi(_lessonsFlow){
                courseDetailsUseCase.lessons(courseId)
                    .onStart { _lessonsFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _lessonsFlow.emit(NetWorkState.StopLoading) }
                    .catch { _lessonsFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _lessonsFlow.emit(NetWorkState.Success(it)) }
            }
    }
    fun sendRequestReviews(courseId:Int){
            executeApi(_reviewsFlow){
                courseDetailsUseCase.reviews(courseId)
                    .onStart { _reviewsFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _reviewsFlow.emit(NetWorkState.StopLoading) }
                    .catch { _reviewsFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _reviewsFlow.emit(NetWorkState.Success(it)) }
            }
    }
    fun sendRequestAddRate(courseId:Int,rate:Float,comment:String?=null){
            executeSharedApi(_rateFlow){
                courseDetailsUseCase.addReview(courseId,rate, comment)
                    .onStart { _rateFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _rateFlow.emit(NetWorkState.StopLoading) }
                    .catch { _rateFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _rateFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _rateFlow.emit(NetWorkState.Success(it)) }
            }
    }

    fun sendRequestQuizzes(courseId:Int){
            executeSharedApi(_quizzesFlow){
                courseDetailsUseCase.quizzes(courseId)
                    .onStart { _quizzesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _quizzesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _quizzesFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _quizzesFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _quizzesFlow.emit(NetWorkState.Success(it)) }
            }
    }

    fun sendRequestBuyCourse(courseId:Int){
        executeSharedApi(_buyCourseFlow){
            coursesUseCase.buyCourse(courseId)
                    .onStart { _buyCourseFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _buyCourseFlow.emit(NetWorkState.StopLoading).also {
                        _buyCourseFlow.emit(NetWorkState.Idle)
                    } }
                    .catch { _buyCourseFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _buyCourseFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _buyCourseFlow.emit(NetWorkState.Success(it)) }
        }
    }
    fun sendRequestCoupon(courseId:Int,coupon:String){
        executeSharedApi(_couponFlow){
            courseDetailsUseCase.setCoupon(courseId,coupon)
                    .onStart { _couponFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _couponFlow.emit(NetWorkState.StopLoading) }
                    .catch { _couponFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _couponFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _couponFlow.emit(NetWorkState.Success(it)) }
        }
    }
    fun sendRequestCouponPurchase(courseId:Int,coupon:String){
        executeSharedApi(_couponPurchaseFlow){
            courseDetailsUseCase.setCouponPurchase(courseId,coupon)
                    .onStart { _couponPurchaseFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _couponPurchaseFlow.emit(NetWorkState.StopLoading) }
                    .catch { _couponPurchaseFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _couponPurchaseFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _couponPurchaseFlow.emit(NetWorkState.Success(it)) }
        }
    }
    fun sendRequestContactTeacher(courseId:Int,msg:String){
        executeSharedApi(_contactFlow){
            coursesUseCase.contactTeacher(courseId,msg)
                    .onStart { _contactFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _contactFlow.emit(NetWorkState.StopLoading) }
                    .catch { _contactFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _contactFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _contactFlow.emit(NetWorkState.Success(it)) }
        }
    }
    fun sendRequestTeacher(ownerId:Int){
        executeSharedApi(_contactFlow){
            coursesUseCase.teacher(ownerId)
                    .onStart { _contactFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _contactFlow.emit(NetWorkState.StopLoading) }
                    .catch { _contactFlow.emit(NetWorkState.Error(it.handleException())).also {
                        _contactFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest { _contactFlow.emit(NetWorkState.Success(it)) }
        }
    }

    fun sendRequestFiles(courseId:Int,pageNumber: Int){
            executeSharedApi(_filesFlow){
                courseDetailsUseCase.files(courseId,pageNumber)
                    .onStart { _filesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _filesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _filesFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _filesFlow.emit(NetWorkState.Success(it)) }
            }
    }

    fun sendRequestDiscussions(courseId:Int){
            executeSharedApi(_discFlow){
                courseDetailsUseCase.discussions(courseId.toString())
                    .onStart { _discFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _discFlow.emit(NetWorkState.StopLoading) }
                    .catch { _discFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _discFlow.emit(NetWorkState.Success(it)) }
            }
    }
    fun sendRequestAddDiscussions(courseId:Int,discTitle:String,discContent:String){
            executeSharedApi(_addDiscFlow){
                courseDetailsUseCase.addDiscussions(courseId.toString(),discTitle,discContent)
                    .onStart { _addDiscFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _addDiscFlow.emit(NetWorkState.StopLoading) }
                    .catch { _addDiscFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _addDiscFlow.emit(NetWorkState.Success(it)) }
            }
    }
    fun sendRequestAddComment(discId:Int,comment:String){
            executeSharedApi(_addCommentFlow){
                courseDetailsUseCase.addComment(discId,comment)
                    .onStart { _addCommentFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _addCommentFlow.emit(NetWorkState.StopLoading) }
                    .catch { _addCommentFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collectLatest { _addCommentFlow.emit(NetWorkState.Success(it)) }
            }
    }

    fun hasReview(review:String?):Boolean{
        return review.isNullOrBlank().not()
    }

    fun isUserLogin():Boolean{
        return courseDetailsUseCase.isUserLogin()
    }
    fun getUser():UserModel?{
        return courseDetailsUseCase.getUser()
    }

    fun getToken(): String {
        return courseDetailsUseCase.token
    }

}