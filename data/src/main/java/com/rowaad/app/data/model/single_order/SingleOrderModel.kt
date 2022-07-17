package com.rowaad.app.data.model.single_order

import com.rowaad.app.data.model.orders.OrderDetails
import com.rowaad.app.data.model.orders.OrderItem

data class SingleOrderModel(
    val record: OrderItem?=null
)