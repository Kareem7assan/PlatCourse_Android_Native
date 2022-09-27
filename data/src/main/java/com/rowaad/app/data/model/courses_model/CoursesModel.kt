package com.rowaad.app.data.model.courses_model

import com.google.gson.annotations.SerializedName

data class CoursesModel(
	val next: String? = null,
	val previous: String? = null,
	val count: Int? = null,
	val results: List<CourseItem>? = null
)

data class CouponModel(val details:String?=null)

data class CourseItem(
	val overview: String? = null,
	val featured: Boolean? = null,
    @SerializedName("owner_name")
	val ownerName: String? = null,
    @SerializedName("sub_category")
	val subCategory: Int? = null,
    @SerializedName("owner_id")
    val ownerId: Int? = null,
    @SerializedName("discount_price")
	val discountPrice: Float? = null,
    @SerializedName("created_at")
	val createdAt: String? = null,
	val title: String? = null,
	val platform: List<String>? = null,
	val isOwner:Boolean?=false,
	val cover: String? = null,
	val rate: Float? = null,
	val price: Float? = null,
	val intro: String? = null,
	val id: Int? = null,
	val category: Int? = null
)

