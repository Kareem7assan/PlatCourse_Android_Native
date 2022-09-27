package com.platCourse.platCourseAndroid.menu

import android.util.Log
import com.rowaad.app.base.BaseViewModel
import com.rowaad.app.data.model.Menu
import com.rowaad.app.data.model.MenuModel
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.app.data.remote.NetWorkState
import com.rowaad.app.usecase.handleException
import com.rowaad.app.usecase.menu.MenuUseCase
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.auth.register.RegisterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
open class MenuViewModel @Inject constructor(private val menuUseCase: MenuUseCase
) : BaseViewModel(){



    private val _userFlow= MutableSharedFlow<NetWorkState>()
    val userFlow= _userFlow.asSharedFlow()

    private val _contactsFlow= MutableSharedFlow<NetWorkState>()
    val contactsFlow= _contactsFlow.asSharedFlow()

    private val _articlesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val articlesFlow= _articlesFlow.asSharedFlow()

    private val _articleFlow= MutableSharedFlow<NetWorkState>()
    val articleFlow= _articleFlow.asSharedFlow()

    private val _myProfileFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val myProfileFlow = _myProfileFlow.asSharedFlow()

    private val _editProfileFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val editProfileFlow= _editProfileFlow.asSharedFlow()


    private val _contactsPostFlow= MutableSharedFlow<NetWorkState>()
    val contactsPostFlow= _contactsPostFlow.asSharedFlow()

    private val _settingsFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val settingsFlow= _settingsFlow.asSharedFlow()

    private val _isValidNameFlow= MutableStateFlow<RegisterViewModel.Validation>(RegisterViewModel.Validation.Idle)
    val isValidNameFlow= _isValidNameFlow.asSharedFlow()

    private val _isValidEmailFlow= MutableStateFlow<RegisterViewModel.Validation>(RegisterViewModel.Validation.Idle)
    val isValidEmailFlow= _isValidEmailFlow.asSharedFlow()

    private val _isValidTitleFlow= MutableStateFlow<RegisterViewModel.Validation>(RegisterViewModel.Validation.Idle)
    val isValidTitleFlow= _isValidTitleFlow.asSharedFlow()

    private val _isValidBodyFlow= MutableStateFlow<RegisterViewModel.Validation>(RegisterViewModel.Validation.Idle)
    val isValidBodyFlow= _isValidBodyFlow.asSharedFlow()

    var pageNotificationNum=0

    fun getUser()=menuUseCase.getUser()
    val isVisitor=menuUseCase.isLogin.not()


    fun getListMenu():MutableList<MenuModel>{
        return if (menuUseCase.isLogin){
            mutableListOf(
                MenuModel(name = R.string.orders,resImg = R.drawable.ic_baseline_shopping_bag_24,menuItem = Menu.ORDERS),
                MenuModel(name = R.string.articles,resImg = R.drawable.ic_baseline_menu_book_24,menuItem = Menu.ARTICLES),
                MenuModel(name = R.string.terms_menu,resImg = R.drawable.ic_baseline_library_books_24,menuItem = Menu.TERMS),
                MenuModel(name = R.string.contact_us,resImg = R.drawable.ic_baseline_question_answer_24,menuItem = Menu.CONTACT_US),
                MenuModel(name = R.string.night_mode,resImg = R.drawable.ic_baseline_nightlight_24,menuItem = Menu.NIGHT),
                MenuModel(name = R.string.logout,resImg = R.drawable.ic_baseline_person_24,menuItem = Menu.LOGOUT),
                )
        }
        else{
            mutableListOf(
                    MenuModel(name = R.string.articles,resImg = R.drawable.ic_baseline_menu_book_24,menuItem = Menu.ARTICLES),
                    MenuModel(name = R.string.terms_menu,resImg = R.drawable.ic_baseline_library_books_24,menuItem = Menu.TERMS),
                    MenuModel(name = R.string.contact_us,resImg = R.drawable.ic_baseline_question_answer_24,menuItem = Menu.CONTACT_US),
                    MenuModel(name = R.string.night_mode,resImg = R.drawable.ic_baseline_nightlight_24,menuItem = Menu.NIGHT)

                    )
        }

    }

     fun setDarkMode(isEnable:Boolean){
        menuUseCase.enableDark(isEnable)
    }



    fun getArticles(){
        executeSharedApi(_articlesFlow){
            menuUseCase.articles()
                    .onStart { _articlesFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _articlesFlow.emit(NetWorkState.StopLoading) }
                    .catch { _articlesFlow.emit(NetWorkState.Error(it)) }
                    .collectLatest {  _articlesFlow.emit(NetWorkState.Success(it)) }
        }
    }

    fun getArticle(id:Int){
        executeSharedApi(_articleFlow){
            menuUseCase.article(id)
                    .onStart { _articleFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _articleFlow.emit(NetWorkState.StopLoading) }
                    .catch { _articleFlow.emit(NetWorkState.Error(it)) }
                    .collectLatest {  _articleFlow.emit(NetWorkState.Success(it)) }
        }
    }


    fun contactUsContacts(){
        executeSharedApi(_contactsFlow){
            menuUseCase.contactUsContacts()
                    .onStart { _contactsFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _contactsFlow.emit(NetWorkState.StopLoading) }
                    .catch { _contactsFlow.emit(NetWorkState.Error(it)) }
                    .collectLatest {  _contactsFlow.emit(NetWorkState.Success(it)) }
        }
    }


    fun myProfile(){
        executeSharedApi(_myProfileFlow){
            menuUseCase.myProfile()
                    .onStart { _myProfileFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _myProfileFlow.emit(NetWorkState.StopLoading).also {
                        _myProfileFlow.emit(NetWorkState.Idle)
                    } }
                    .catch { _myProfileFlow.emit(NetWorkState.Error(it)).also {
                        _myProfileFlow.emit(NetWorkState.Idle)
                    } }
                    .collectLatest {  _myProfileFlow.emit(NetWorkState.Success(it))
                    }
        }
    }

    fun editProfile(avatar: MultipartBody.Part?=null){
        Log.e("image","multi"+","+avatar?.headers.toString())
            executeSharedApi(_editProfileFlow) {
                menuUseCase.editProfile(avatar = avatar)
                        .onStart { _editProfileFlow.emit(NetWorkState.Loading) }
                        .onCompletion { _editProfileFlow.emit(NetWorkState.StopLoading).also {
                            _editProfileFlow.emit(NetWorkState.Idle)
                            myProfile()
                        } }
                        .catch { _editProfileFlow.emit(NetWorkState.Error(it)).also {
                            _editProfileFlow.emit(NetWorkState.Idle)
                        } }
                        .collectLatest { _editProfileFlow.emit(NetWorkState.Success(it))
                        }

        }
    }
    fun editProfileBody(avatar: RequestBody){
            executeSharedApi(_editProfileFlow) {
                menuUseCase.editProfileBody( avatar)
                        .onStart { _editProfileFlow.emit(NetWorkState.Loading) }
                        .onCompletion { _editProfileFlow.emit(NetWorkState.StopLoading) }
                        .catch { _editProfileFlow.emit(NetWorkState.Error(it)).also {
                            _editProfileFlow.emit(NetWorkState.Idle)
                        } }
                        .collectLatest { _editProfileFlow.emit(NetWorkState.Success(it)) }

        }
    }

    fun editProfile(body: HashMap<String,RequestBody>){
            executeSharedApi(_editProfileFlow) {
                menuUseCase.editProfileBody(body)
                        .onStart { _editProfileFlow.emit(NetWorkState.Loading) }
                        .onCompletion { _editProfileFlow.emit(NetWorkState.StopLoading) }
                        .catch { _editProfileFlow.emit(NetWorkState.Error(it)).also {
                            _editProfileFlow.emit(NetWorkState.Idle)
                        } }
                        .collectLatest { _editProfileFlow.emit(NetWorkState.Success(it)) }

        }
    }





    fun logout(){
        menuUseCase.clearPrefs()
        /*executeSharedApi(_userFlow){
            menuUseCase.logout()
                    .onStart { _userFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _userFlow.emit(NetWorkState.StopLoading) }
                    .catch { _userFlow.emit(NetWorkState.Error(it)) }
                    .collectLatest { menuUseCase.clearPrefs().also { _userFlow.emit(NetWorkState.Success(it)) } }
        }*/
    }

    private val _notificationFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val notificationFlow= _notificationFlow.asStateFlow()

    private val _notificationRemoveFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    val notificationRemoveFlow= _notificationRemoveFlow.asStateFlow()


    fun showNotifications(page:Int=1){
        executeApi(_notificationFlow){
            menuUseCase.notifications(page)
                    .onStart { _notificationFlow.emit(NetWorkState.Loading) }
                    .onCompletion { _notificationFlow.emit(NetWorkState.StopLoading) }
                    .catch { _notificationFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collect {resp-> _notificationFlow.emit(NetWorkState.Success(resp))}
        }
    }

    fun seeNotification(notificationId:Int){
        executeApi(_notificationRemoveFlow){
            menuUseCase.readNotification(notificationId)
                    .catch { _notificationRemoveFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collect()
        }
    }
    fun seeAllNotifications(){
        executeApi(_notificationRemoveFlow){
            menuUseCase.readAllNotifications()
                    .catch { _notificationRemoveFlow.emit(NetWorkState.Error(it.handleException())) }
                    .collect()
        }
    }

    fun isValidMailFlow(email: String){
        _isValidEmailFlow.value=(RegisterViewModel.Validation.IsValid(menuUseCase.isValidMail(email)))
    }
    fun isValidNameFlow(name: String){
        _isValidNameFlow.value=(RegisterViewModel.Validation.IsValid(menuUseCase.isValidName(name)))
    }
    fun isValidTitleFlow(title: String){
        _isValidTitleFlow.value=(RegisterViewModel.Validation.IsValid(menuUseCase.isValidTitle(title)))
    }
    fun isValidBodyFlow(body: String){
        _isValidBodyFlow.value=(RegisterViewModel.Validation.IsValid(menuUseCase.isValidBody(body)))
    }


}