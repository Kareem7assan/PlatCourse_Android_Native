package com.rowaad.app.data.remote


sealed class NetWorkState {
    data class Success<out T>(val data: T) : NetWorkState()
    data class Error(val th: Throwable) : NetWorkState()
    object Loading : NetWorkState()
    object Idle : NetWorkState()
    object StopLoading: NetWorkState()

}

sealed class RoomMoviesState {
    data class Success(val data: List<Any>) : RoomMoviesState()
    object Empty : RoomMoviesState()
    //object Idle : RoomState()
    object Loading : RoomMoviesState()
    object StopLoading: RoomMoviesState()
}

sealed class UserRegisterState{
    object Logged :UserRegisterState()
    object UnCompletedRegister :UserRegisterState()
    object NotRegister :UserRegisterState()
    object AwaitingApproveRegister :UserRegisterState()

}

sealed class CurrentRegisterState{
    object VehicleRegister :CurrentRegisterState()
    object VerifiedRegister :CurrentRegisterState()
    object DocumentRegister :CurrentRegisterState()
    object BankRegister :CurrentRegisterState()
}










