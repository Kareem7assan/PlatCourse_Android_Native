package com.rowaad.app.data.model.my_subscription

import com.google.gson.annotations.SerializedName
import com.rowaad.app.data.model.tweets_model.CreatedAt
import com.rowaad.app.data.model.tweets_model.Customer
import com.rowaad.app.data.model.tweets_model.Tweets

data class MySubscriptionModel(
	val currentSubscription: CurrentSubscription? = null,
	val tweets: List<Tweets>? = null
)

data class SubscribePackage(
	val priceAfterDiscountText: String? = null,
	val priceBeforeDiscount: Double? = null,
	val hasDiscount: Boolean? = null,
	val priceBeforeDiscountText: String? = null,
	val published: Boolean? = null,
	val numOfAds: Int? = null,
	val features: String? = null,
	val createdAt: Any? = null,
	val isFree: Boolean? = null,
	val name: String? = null,
	val id: Int? = null,
	val priceAfterDiscount: Double? = null,
	val updatedAt: Any? = null
)




data class CurrentSubscription(
	val createdAt: CreatedAt? = null,
	@SerializedName("package")
	val subscribedPackage: SubscribePackage? = null,
	val totalUsed: Int? = null,
	val totalRemained: Int? = null,
	val paymentMethod: String? = null,
	val id: Int? = null,
	val status: String? = null,
	val customer: Customer? = null
)





