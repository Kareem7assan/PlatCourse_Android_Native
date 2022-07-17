package com.rowaad.app.data.model.orders

import com.rowaad.app.data.model.UserModel

data class OrdersModel(
		val current: List<OrderItem>? = listOf(),
		val deliveryMen: UserModel? = null,
		val newRequest: List<OrderItem>? = listOf(),
		val message: String? = null,
)

data class OrderItem(
	val deliveryCostText: String? = null,
	val paymentMethodInfo: PaymentMethodInfo? = null,
	val nextStatus: List<String?>? = null,
	val distanceToTheRestaurant: String? = null,
	val restaurant: Restaurant? = null,
	val statusLog: List<StatusLogItem?>? = null,
	val deliveryCommissionText: String? = null,
	val createdAt: CreatedAt? = null,
	val finalPriceText: String? = null,
	val deliveryMenId: Int? = null,
	val timer: Int? = null,
	val order:OrderDetails?=null,
	val distanceToTheCustomer: String? = null,
	val minuteToTheCustomer: String? = null,
	val totalMinute: String? = null,
	val addressOrderCustomer: AddressOrderCustomer? = null,
	val statusText: String? = null,
	val customerId: Int? = null,
	val location: Location? = null,
	val id: Int? = null,
	val totalDistance: String? = null,
	val minuteToTheRestaurant: String? = null,
	val status: String? = null,
	val customer: Customer? = null
)

data class DataState(
	val bankInfo: Boolean? = null,
	val images: Boolean? = null,
	val vehicleInfo: Boolean? = null
)

data class NameItem(
	val localeCode: String? = null,
	val text: String? = null
)

data class WorkTimesItem(
	val available: String? = null,
	val day: String? = null,
	val close: String? = null,
	val open: String? = null
)

data class Coords(
	val address: String? = null,
	val coordinates: List<Double?>? = null,
	val type: String? = null
)

data class DeliveryMen(
	val lastName: String? = null,
	val accountCardName: String? = null,
	val countResendCode: Int? = null,
	val isVerified: Boolean? = null,
	val vehicleFrontImage: String? = null,
	val idNumber: Long? = null,
	val vehicleSerialNumber: Long? = null,
	val vehicleBrand: String? = null,
	val createdAt: CreatedAt? = null,
	val approved: String? = null,
	val yearManufacture: Int? = null,
	val walletBalance: Float? = null,
	val vehicleBackImage: String? = null,
	val vehicleModel: String? = null,
	val bankAccountNumber: String? = null,
	val id: Int? = null,
	val email: String? = null,
	val image: String? = null,
	val cardIdImage: String? = null,
	val dataState: DataState? = null,
	val published: Boolean? = null,
	val driveryLicenseImage: String? = null,
	val accessToken: String? = null,
	val birthDate: String? = null,
	val verificationCode: Int? = null,
	val firstName: String? = null,
	val requested: Int? = null,
	val phoneNumber: String? = null,
	val name: String? = null,
	val location: Location? = null,
	val orders: List<Any?>? = null,
	val codePhoneNumber: Int? = null,
	val status: Boolean? = null
)


data class StatusLogItem(
	val createdAt: CreatedAt? = null,
	val creator: String? = null,
	val orderId: Int? = null,
	val statusText: String? = null,
	val orderDeliveryId: Int? = null,
	val status: String? = null
)

data class City(
	val name: String? = null,
	val id: Int? = null
)



data class CreatedAt(
	val humanTime: String? = null,
	val format: String? = null,
	val text: String? = null,
	val timestamp: Int? = null
)

data class PaymentMethodInfo(
	val code: String? = null,
	val name: String? = null,
	val icon: String? = null
)

data class OpenToday(
	val available: String? = null,
	val close: String? = null,
	val day: String? = null,
	val open: String? = null
)

data class TypeOfFoodRestaurantItem(
	val createdAt: Any? = null,
	val usage: Int? = null,
	val name: String? = null,
	val id: Int? = null,
	val published: Boolean? = null
)

data class Location(
		val restaurant: Coords? = null,
		val me: Coords? = null,
		val customer: Coords? = null,
		val address: String? = null,
		val coordinates: List<Double>? = null,
		val type: String? = null
)

data class Customer(
	val firstName: String? = null,
	val lastName: String? = null,
	val phoneNumber: String? = null,
	val id: String? = null,
	val email: String? = null,
	val address: String? = null,
	val coordinates: List<Double?>? = null,
	val type: String? = null
)

data class Restaurant(
	val address: String? = null,
	val coordinates: List<Double>? = null,
	val type: String? = null,
	val logoText: String? = null,
	val delivery: Boolean? = null,
	val isBusy: Boolean? = null,
	val city: City? = null,
	val commercialRegisterNumber: Long? = null,
	val commercialRegisterImage: String? = null,
	val countItems: Int? = null,
	val rating: Float? = null,
	val totalRating: Float? = null,
	val published: Boolean? = null,
	val deliveryValueText: String? = null,
	val typeOfFoodRestaurant: List<TypeOfFoodRestaurantItem>? = null,
	val minimumOrders: Int? = null,
	val cover: String? = null,
	val logoImage: String? = null,
	val isClosed: Boolean? = null,
	val minimumOrdersText: String? = null,
	val workTimes: List<WorkTimesItem>? = null,
	val name: String? = null,
	val location: Location? = null,
	val openToday: OpenToday? = null,
	val id: Int? = null,
	val categories: List<CategoriesItem>? = null,
	val profitRatio: Int? = null
)

data class CategoriesItem(
	val image: String? = null,
	//val name: String? = null,
	val title: String? = null,
	val id: Int? = null,
	val hasChild: Boolean? = true,
	val published: Boolean? = null,
	var isChecked:Boolean=false,
	val type: String? = null,
	val slug: String? = null
)

data class AddressOrderCustomer(
	val lastName: String? = null,
	val country: Any? = null,
	val address: String? = null,
	//val city: City? = null,
	val receiverName: Any? = null,
	val flatNumber: String? = null,
	val verified: Boolean? = null,
	val type: Any? = null,
	val verificationCode: Any? = null,
	val firstName: String? = null,
	val phoneNumber: String? = null,
	val isPrimary: Boolean? = null,
	val district: String? = null,
	val floorNumber: Any? = null,
	val buildingNumber: String? = null,
	val specialMark: String? = null,
	val location: Location? = null,
	val id: Int? = null,
	val email: String? = null
)

