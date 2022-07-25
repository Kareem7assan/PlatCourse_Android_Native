package com.rowaad.app.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MenuModel (@DrawableRes val resImg:Int, @StringRes val name:Int, val menuItem:Menu)

/*enum class Menu(
        PROFILE, CHANGE_PASSWORD, PACKAGES, CONTACT_US
)*/

enum class Menu {
    PROFILE, ORDERS,ARTICLES, CHANGE_PASSWORD, PACKAGES, CONTACT_US, COMMISSION, ABOUT_US , TERMS ,PRIVACY_SALES, LOGOUT,NIGHT,LOGIN
}