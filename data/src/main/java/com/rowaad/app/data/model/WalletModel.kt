package com.rowaad.app.data.model


data class WalletModel(
        val records:List<WalletItem>?=null,
        val walletBalance:Float?=0f,
        val walletBalanceText:String?=null,
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
/*
    val paymentMethodInfo:PaymentMethodInfo?=null,
*/


)