package com.rowaad.app.data.utils


object Constants_Api {

    var VARIANT_URL = "https://pub.rh.net.sa/taghareed/backend/main/public/api/"

    const val BASE_TOKEN = "Bearer HJGFGDHFGJCHXGFDGHFHFXDFSHJFJKGKGHH"

object CODES{
    const val CAM_CODE=100
    const val AVATAR_CODE=110
    const val COVER_CODE=120
    const val MAP_CODE=101
}
//    const val APP_KEY=""

    object KEYS{
        const val MAP_KEY="AIzaSyBhNrmYXMj0CdXh0hxJmUy-681HzzyVLzo"
    }
object PrefKeys{
    const val USER_DATA = "USER_DATA"
    const val LANG = "lang"
    const val DEVICE_ID = "dev_id"
    const val IS_USER_LOGGED = "IS_USER_LOGGED"
    const val IS_USER_REGISTER = "IS_REGISTER"
    const val TOKEN = "TOKEN"
    const val IS_GUEST_USER_LOGGED = "IS_GUEST_USER_LOGGED"
    const val PREFERENCES_NAME = "PREFERENCES_NAME"
    const val DEVICE_TOKEN = "DEVICE_TOKEN"
    const val LOCATION_DATA = "LOCATION_DATA"
    const val IS_LOCATION_SAVED = "IS_LOCATION_SAVED"
    const val IS_LOCATION_UPDATED = "IS_LOCATION_UPDATED"
    const val FIREBASE: String = "fcm"
    const val STATUS: String = "status"
    const val INTERVAL: String = "interval"
    const val HAS_REGISTER: String = "hasRegister"
    const val SHOW_NOTIFICATION: String = "notification_show"
    const val USER: String = "user"
    const val SESSION: String = "session"
}


    object Payments {
        val CASH="cashOnDelivery"
        val VISA="VISA"
        val MADA="MADA"
        val MASTER="MASTER"
        val APPLEPAY="APPLEPAY"
        val WALLET="wallet"
    }
    object TWEET {
        val ACTIVE="active"
        val DEACTIVE="inActive"
        val PENDING="pending"
        val SOLD="sold"
    }

object ERROR_API{
    //deleted or unauthorized 401
    const val UNAUTHRIZED = "unAuthroized"
    //500
    const val SERVER_ERROR = "serverError"
    //503
    const val MAINTENANCE = "maintenanceMode"
    //400
    const val BODY_ERROR = "body_error"
    //400
    const val BAD_REQUEST = "bad_request"
    //404
    const val NOT_FOUND = "not_found"

    const val USER_DELETED = "userDelete"
    //no internet connection
    const val CONNECTION_ERROR = "connection_error"

}
object INTENT{
        const val CURRENT="current"
        const val ORDER_ID="orderId"
        const val ROOM="room"
        const val MSG="msg"
        const val USER="user"
        const val LOGOUT="logout"
        const val PHONE="phone"
        const val ORDER_KEY="order"
        const val MSG_KEY="message_key"
    const val INTENT_IMG="img"

    const val NAV="out_nav"
}
object ORDER{
        const val ACCEPTED="accepted"
        const val ON_WAY_TO_CUSTOMER="deliveryOnTheWay"
        const val PENDING="pending"
        const val CANCELLED="orderCancelByAdmin"
}
object FCM{
        const val NEW_ORDER="order"
        const val PULL_ORDER="pullOrder"
        const val CANCEL_ORDER="cancelOrder"
        const val ACCEPT_ORDER_ADMIN="acceptedOrder"
        const val ON_WAY_ADMIN="deliveryOnTheWayOrder"
        const val COMPLETE_ADMIN="completedOrder"
        const val NOT_COMPLETE_ADMIN="notCompletedOrder"

}

    const val INTENT_EXTRA = "extra"

    const val DEFAULT_APP_LANGUAGE = "ar"

}