package com.rowaad.app.data.model.categories_model

import com.google.gson.annotations.SerializedName

data class CategoriesModel (
    @SerializedName("records")
    val categories: List<CategoriesItem>? = null
    )

data class CategoriesItem(val name:String)