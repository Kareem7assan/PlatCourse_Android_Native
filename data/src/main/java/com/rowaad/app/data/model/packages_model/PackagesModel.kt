package com.rowaad.app.data.model.packages_model

import com.rowaad.app.data.model.bank_accounts_model.CreatedAt
import com.rowaad.app.data.model.tweets_model.PaginationInfo

data class PackagesModel(
		val records: List<PackageItem>? = null,
		val customerSubscription:SubscriptionPackage?=null,
		val paginationInfo: PaginationInfo? = null
)

data class UpdatedAt(
	val humanTime: String? = null,
	val format: String? = null,
	val text: String? = null,
	val timestamp: Int? = null
)

data class SubscriptionPackage(
		val id: Int? = null,
		val totalUsed: Int? = null,
		val totalRemained: Int? = null,
		val status: String? = null
)

data class PackageItem(
		val numOfAds: Int? = null,
		val features: String? = null,
		val isFree:Boolean?=false,
		val createdAt: CreatedAt? = null,
		val offerEndAt: OfferEndAt? = null,
		val priceBeforeDiscount: Float? = null,
		val hasDiscount: Boolean? = null,
		val name: String? = null,
		val id: Int? = null,
		val published: Boolean? = null,
		val offerStartAt: OfferStartAt? = null,
		val priceAfterDiscount: Float? = null,
		val updatedAt: UpdatedAt? = null
)

data class OfferStartAt(
	val humanTime: String? = null,
	val format: String? = null,
	val text: String? = null,
	val timestamp: Int? = null
)

data class OfferEndAt(
	val humanTime: String? = null,
	val format: String? = null,
	val text: String? = null,
	val timestamp: Int? = null
)


