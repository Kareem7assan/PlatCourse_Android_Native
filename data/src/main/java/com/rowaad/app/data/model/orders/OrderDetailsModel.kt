package com.rowaad.app.data.model.orders


data class SubscriptionCart(
	val itemId: Any? = null,
	val options: Any? = null,
	val inCart: Boolean? = null,
	val quantityInCart: Int? = null
)


data class Currency(
	val symbol: String? = null,
	val code: String? = null,
	val name: String? = null,
	val decimalValue: Int? = null,
	val value: Int? = null
)

data class RestaurantManager(
	val totalNotifications: Any? = null,
	val isVerified: Boolean? = null,
	val restaurant: Restaurant? = null,
	val name: String? = null,
	val id: Int? = null,
	val published: Boolean? = null,
	val accessToken: Any? = null,
	val email: String? = null,
	val verificationCode: Any? = null
)



data class ThirdWeek(
	val deliveryDate: Any? = null
)


data class TotalsItem(
	val price: Double? = null,
	val text: String? = null,
	val type: String? = null,
	val priceText: String? = null
)



data class ItemsItem(
	val createdAt: CreatedAt? = null,
	val product: Product? = null,
	val quantity: Int? = null,
	val nextStatus: Any? = null,
	val totalPrice: Double? = null,
	val price: Double? = null,
	val isRated: Any? = null,
	val options: List<Any?>? = null,
	val id: Int? = null,
	val rewardPoints: Int? = null,
	val totalPriceText: String? = null,
	val requestReturning: Any? = null
)

data class ShippingAddress(
	val lastName: String? = null,
	val country: Any? = null,
	val address: String? = null,
	val city: City? = null,
	val flatNumber: Any? = null,
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

data class SecondWeek(
	val deliveryDate: Any? = null
)

data class Product(
	val rating: Int? = null,
	val finalPrice: Double? = null,
	val description: String? = null,
	val type: String? = null,
	val cart: Cart? = null,
	val finalPriceText: String? = null,
	val hasMaxQuantity: Boolean? = null,
	val purchaseRewardPoints: Int? = null,
	val price: Double? = null,
	val id: Int? = null,
	val rewardPoints: Int? = null,
	val priceText: String? = null,
	val slug: String? = null,
	val specialDietPercentage: SpecialDietPercentage? = null,
	val images: List<String?>? = null,
	val quantity: Int? = null,
	val hasDiscount: Boolean? = null,
	val restaurant: Restaurant? = null,
	val subscriptionCart: SubscriptionCart? = null,
	val totalRating: Int? = null,
	val hasRequiredOptions: Boolean? = null,
	val published: Boolean? = null,
	val url: String? = null,
	val specialDietGrams: SpecialDietGrams? = null,
	val priceInSubscriptionText: String? = null,
	val name: String? = null,
	val category: Any? = null,
	val isFavorite: Boolean? = null
)

data class SpecialDietGrams(
	val carbs: Double? = null,
	val protein: Double? = null,
	val fat: Double? = null,
	val calories: Double? = null
)



data class FirstWeek(
	val deliveryDate: Any? = null
)

data class FourthWeek(
	val deliveryDate: Any? = null
)

data class OrderDetails(
	val shippingFees: Double? = null,
	val notes: String? = null,
	val originalPrice: Double? = null,
	val restaurantManager: RestaurantManager? = null,
	val statusColor: String? = null,
	val statusIcon: String? = null,
	val finalPrice: Double? = null,
	val taxes: Double? = null,
	val subTotal: Double? = null,
	val subscription: Any? = null,
	val secondWeek: SecondWeek? = null,
	val createdAt: CreatedAt? = null,
	val totalQuantity: Int? = null,
	val cashOnDeliveryPrice: String? = null,
	val currency: Currency? = null,
	val id: Int? = null,
	val rewardPoints: Int? = null,
	val fourthWeek: FourthWeek? = null,
	val finalPriceTextOrder: String? = null,
	val expectedDeliveryIn: String? = null,
	val wallet: Double? = null,
	val nextStatus: List<String?>? = null,
	val thirdWeek: ThirdWeek? = null,
	val shippingMethod: Any? = null,
	val partialReturn: Any? = null,
	val deliveryType: String? = null,
	val statusLog: List<StatusLogItem?>? = null,
	val firstWeek: FirstWeek? = null,
	val totals: List<TotalsItem?>? = null,
	val cancelingReason: Any? = null,
	val statusText: String? = null,
	val paymentMethod: String? = null,
	val shippingAddress: ShippingAddress? = null,
	val productsType: String? = null,
	val returnedAmount: Any? = null,
	val items: List<ItemsItem?>? = null,
	val canReturnOrder: Boolean? = null,
	val status: String? = null,
	val customer: Customer? = null
)

data class OrderLog(
		val logTxt:String,
		val orderId:String,
		val fullName:String
)

data class SpecialDietPercentage(
	val carbs: Double? = null,
	val protein: Double? = null,
	val fat: Double? = null,
	val calories: Double? = null
)

data class Cart(
	val itemId: Any? = null,
	val options: Any? = null,
	val inCart: Boolean? = null,
	val quantityInCart: Int? = null
)

