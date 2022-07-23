package com.rowaad.app.data.model.categories_model

import java.io.Serializable

data class SubCategory(
    var category: Int? = null,
    var id: Int? = null,
    var sub_category_name: String? = null
):Serializable