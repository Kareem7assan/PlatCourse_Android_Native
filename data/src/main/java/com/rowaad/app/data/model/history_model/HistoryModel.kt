package com.rowaad.app.data.model.history_model

import com.rowaad.app.data.model.orders.OrderItem
import com.rowaad.app.data.model.tweets_model.PaginationInfo

data class HistoryModel (
        val records:List<OrderItem>?=null,
        val paginationInfo: PaginationInfo?=null,
        val lastOrdeDelivered:OrderItem?=null,
        val assignDateDelivery:String?=null,
        val countOrders:String?=null,
        val countSumKm:String?=null,
        val countSumProfits:String?=null,
        val countSumProfitsText:String?=null,
        val date:String?=null,
        val dateText:String?=null,
        val dateTextYear:String?=null
    )

