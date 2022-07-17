package com.rowaad.app.data.model

import com.rowaad.app.data.model.orders.PaymentMethodInfo
import com.rowaad.app.data.model.tweets_model.PaginationInfo

data class WalletModel(
        val records:List<WalletItem>?=null,
        val walletBalance:Float?=0f,
        val walletBalanceText:String?=null,
        val paginationInfo: PaginationInfo?=null,
        val deliveryMen:UserModel?=null
)

data class WalletItem(
    val id:Int,
    val title:String?=null,
    val creatorType:String?=null,
    val transactionType:String?=null,
    val reason:String?=null,
    val amount:Float?=null,
    val orderId:Int?=null,
    val commissionDiteMarket:Float?=null,
    val totalAmountOrder:Float?=null,
    val createdAt:CreatedAt?=null,
    val amountText:String?=null,
    val balanceBefore:String?=null,
    val balanceAfter:String?=null,
    val color:String?=null,
    val colorIcon:String?=null,
    val image:String?=null,
    val commissionDiteMarketText:String?=null,
    val totalAmountOrderText:String?=null,
    val paymentMethodInfo:PaymentMethodInfo?=null,


)