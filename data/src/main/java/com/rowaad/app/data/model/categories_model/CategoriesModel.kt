package com.rowaad.app.data.model.categories_model

import com.google.gson.annotations.SerializedName
import com.rowaad.app.data.model.orders.CategoriesItem

data class CategoriesModel (
    @SerializedName("records")
    val categories: List<CategoriesItem>? = null
    )