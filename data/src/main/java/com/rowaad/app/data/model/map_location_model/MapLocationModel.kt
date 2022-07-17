package com.rowaad.app.data.model.map_location_model

import com.rowaad.app.data.model.orders.Location
import com.rowaad.app.data.model.orders.OrderItem

data class MapLocationModel(
        val deliveryLocation: Location?=null,
        val restaurantLocation: List<RestaurantLocation>?=null,
        val current: List<OrderItem>? = listOf()
        )

data class RestaurantLocation(
    val id:Int,
    val logo:String?=null,
    val location:Location?=null,
    val Km:String?=null,
    val minutes:String?=null
)
