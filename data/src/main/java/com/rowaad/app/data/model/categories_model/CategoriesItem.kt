package com.rowaad.app.data.model.categories_model

import java.io.Serializable

data class CategoriesItem(
    var category_name: String? = null,
    var created_at: String? = null,
    var id: Int? = null,
    var sub_category: List<SubCategory>? = null
):Serializable