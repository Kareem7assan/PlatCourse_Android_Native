package com.rowaad.app.data.remote

import com.kareem.dietDelivery.data.model.movies_model.MoviesModel
import com.rowaad.app.data.model.*
import com.rowaad.app.data.model.bank_accounts_model.BankAccountsModel
import com.rowaad.app.data.model.categories_model.CategoriesModel
import com.rowaad.app.data.model.chanel_model.CustomerChat
import com.rowaad.app.data.model.chanel_model.MessageModel
import com.rowaad.app.data.model.chanel_model.MessagesModel
import com.rowaad.app.data.model.chanel_model.RoomsModel
import com.rowaad.app.data.model.contact_us_model.ContactUsModel
import com.rowaad.app.data.model.create_order.CreateTweetModel
import com.rowaad.app.data.model.history_model.HistoryModel
import com.rowaad.app.data.model.map_location_model.MapLocationModel
import com.rowaad.app.data.model.my_subscription.MySubscriptionModel
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.orders.CategoriesItem
import com.rowaad.app.data.model.orders.OrdersModel
import com.rowaad.app.data.model.orders.PaymentMethodInfo
import com.rowaad.app.data.model.packages_model.PackagesModel
import com.rowaad.app.data.model.reasons_model.ReasonsModel
import com.rowaad.app.data.model.regions_model.RegionsModel
import com.rowaad.app.data.model.register_model.RegisterModel
import com.rowaad.app.data.model.settings.SettingsModel
import com.rowaad.app.data.model.single_order.SingleOrderModel
import com.rowaad.app.data.model.tweets_model.TweetsModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("now_playing")
    suspend fun getLatestMovies(
        @Query("page") page:Int=1,
        @Query("language") language:String="en-US",
        @Query("api_key") apiKey:String="e7c8c79bac155e8f4a21476bfe58c90e"
    ): Response<MoviesModel>

 @POST("login")
    suspend fun login(
        @Query("email") email:String,
        @Query("password") password:String,
        @Query("device[token]") fireBaseToken:String?=null,
        @Query("device[type]") type:String="Android"
 ): Response<EndPointResponse<RegisterModel>>

    @POST("login/guests")
    suspend fun guestToken(): Response<EndPointResponse<BaseResponse>>

  @GET("pages/privacy-policy")
    suspend fun privacy(): Response<EndPointResponse<SettingsModel>>

  @GET("pages/sale-policy")
    suspend fun salesPolicy(): Response<EndPointResponse<SettingsModel>>


  @GET("pages/about-us")
    suspend fun aboutUs(): Response<EndPointResponse<SettingsModel>>


    @POST("register")
    suspend fun register(
         @Query("name") name:String,
         @Query("email") email:String,
         @Query("phoneNumber") phoneNumber:String,
         @Query("password") password:String,
         @Query("passwordConfirmation") password_confirmation:String,
         @Query("device[token]") fireBaseToken:String,
         @Query("device[type]") type: String?="Android"
     ): Response<EndPointResponse<RegisterModel>>

    @POST("forget-password")
    suspend fun forgetPassword(
        @Query("email") email:String
    ): Response<EndPointResponse<RegisterModel>>

    @POST("verify-code")
    suspend fun verify(
        @Query("code") verificationCode:String,
        @Query("email") email:String,
        @Query("type") type:String?="register"
    ): Response<EndPointResponse<RegisterModel>>

    @POST("forget-password")
    suspend fun resend(
        @Query("email") email:String,
        @Query("type") type:String?="register"
    ): Response<EndPointResponse<RegisterModel>>

    @POST("reset-password")
    suspend fun resetPassword(
        @Query("password") password:String,
        @Query("passwordConfirmation") password_confirmation:String,
        @Query("email") email:String,
        @Query("code") code:String?="0",
    ): Response<EndPointResponse<RegisterModel>>

    @GET("bank-accounts")
    suspend fun bankAccounts(): Response<EndPointResponse<BankAccountsModel>>

    @GET("tweets")
    suspend fun myTweets(
        ): Response<EndPointResponse<TweetsModel>>

    @Multipart
    @POST("bank-transfers/store")
    suspend fun bankTransfer(
            @Query("transformerName") transName:String,
            @Query("description") note:String?=null,
            @Query("paidMoney") paidMoney:String,
            @Query("bankAccount") bankAccount:String,
            @Query("tweetId") tweetId:String?=null,
            @Part img: MultipartBody.Part?=null,
            @Query("transferDate") transferDate:String?=null,
            ): Response<EndPointResponse<Any>>



    @POST("bank-transfers/store")
    suspend fun bankTransfer(
            @Query("transformerName") transName:String,
            @Query("description") note:String?=null,
            @Query("paidMoney") paidMoney:String,
            @Query("bankAccount") bankAccount:String,
            @Query("tweetId") tweetId:String?=null,
            @Query("transferDate") transferDate:String?=null,
            ): Response<EndPointResponse<Any>>


    //sprint2

    @GET("categories")
    suspend fun categories(
        @Query("parent") parent:String?="1",
        @Query("paginate") paginate: Boolean?=false
    ): Response<EndPointResponse<CategoriesModel>>

    @GET("categories")
    suspend fun subCategories(
        @Query("categoryId") categoryId:String,
        @Query("paginate") paginate: Boolean?=false
    ): Response<EndPointResponse<CategoriesModel>>

   @GET("tweets")
   suspend fun myTweets(
           @Query("category") category:Int?=null,
           @Query("location[lat]") lat:Double?=null,
           @Query("location[lng]") lng:Double?=null,
           @Query("region[]") regions:List<Int>? = null,
           @Query("city[]") city:List<Int>? = null,
           @Query("following") following:Int? = null,
           @Query("title") title:String? = null,
           @Query("page") page:String
   ): Response<EndPointResponse<TweetsModel>>



   @GET("tweets")
   suspend fun userLikes(
           @Query("liked") userId:Int,
           @Query("page") page:String
   ): Response<EndPointResponse<TweetsModel>>


    @GET("regions")
    suspend fun regions(
        @Query("paginate") paginate: Boolean?=false
    ): Response<EndPointResponse<RegionsModel>>

    @GET("packages")
    suspend fun myPackages(
        @Query("paginate") paginate: Boolean?=false
    ): Response<EndPointResponse<PackagesModel>>

    @GET("payment-method")
    suspend fun paymentMethods(
    ): Response<EndPointResponse<PaymentMethodModel>>

    @Multipart
    @POST("tweets")
    suspend fun createTweetFree(
            @Query("title") title:String,
            @Query("category") category:Int,
            @Query("region") region:Int,
            @Query("city") city:Int,
            @Query("whatsappNumber") phone:String?=null,
            @Part photos: List<MultipartBody.Part>?=null,
    ): Response<EndPointResponse<CreateTweetModel>>

 @POST("tweets")
    suspend fun createTweetFree(
            @Query("title") title:String,
            @Query("category") category:Int,
            @Query("region") region:Int,
            @Query("city") city:Int,
            @Query("whatsappNumber") phone:String?=null
    ): Response<EndPointResponse<CreateTweetModel>>

    @GET("tweets/{id}")
    suspend fun tweetDetails(
            @Path("id") id:String
    ): Response<EndPointResponse<CreateTweetModel>>

    @POST("tweets/{id}/update-status")
    suspend fun tweetUpdate(
        @Path("id") id:String,
        @Query("status") status:String,
        @Query("_method") _method:String="PATCH",
    ): Response<EndPointResponse<CreateTweetModel>>

    @Multipart
    @POST("tweets")
    suspend fun createTweet(
            @Query("title") title:String,
            @Query("category") category:Int,
            @Query("region") region:Int,
            @Query("city") city:Int,
            @Query("whatsappNumber") phone:String?=null,
            @Part photos: List<MultipartBody.Part>?=null,
            @Query("package") packageId:String,
            @Query("paymentMethod") paymentMethod:String?=null,
            @Query("isPremium") isPremium:String?="1"
    ): Response<EndPointResponse<CreateTweetModel>>

    @POST("tweets")
    suspend fun createTweet(
            @Query("title") title:String,
            @Query("category") category:Int,
            @Query("region") region:Int,
            @Query("city") city:Int,
            @Query("whatsappNumber") phone:String?=null,
            @Query("package") packageId:String,
            @Query("paymentMethod") paymentMethod:String?=null,
            @Query("isPremium") isPremium:String?="1"
    ): Response<EndPointResponse<CreateTweetModel>>


    @POST("comments")
    suspend fun addComments(
            @Query("comment") comment:String,
            @Query("typeId") typeId:String,
            @Query("type") type:String?="tweet",
    ): Response<EndPointResponse<Any>>

    @POST("tweets/{id}/add-likes")
    suspend fun like(
            @Path("id") id:String
    ): Response<EndPointResponse<Any>>


    @POST("tweets/{id}/retweet")
    suspend fun retweet(
            @Path("id") id:String
    ): Response<EndPointResponse<Any>>

    @POST("tweets/{id}/reports")
    suspend fun report(
            @Path("id") id:String,
            @Query("reason") reason:String,
            @Query("description") description:String
    ): Response<EndPointResponse<Any>>

    @GET("my-subscription")
    suspend fun mySubscriptions(): Response<EndPointResponse<MySubscriptionModel>>

    @GET("contact-us")
    suspend fun contactUs(): Response<EndPointResponse<ContactUsModel>>

    @POST("contact-us/submit")
    suspend fun postContactUs(
        @Query("name") name:String,
        @Query("email") email:String,
        @Query("subject") subject:String,
        @Query("message") message:String
    ): Response<EndPointResponse<Any>>

    @GET("customers/{id}")
    suspend fun profile(
        @Path("id") id:String ): Response<EndPointResponse<RegisterModel>>


    @POST("follow")
    suspend fun follow(
        @Query("customerId") customerId:String
    ): Response<EndPointResponse<Any>>

    @GET("tweets")
    suspend fun tweetsReplies(
        @Query("tweetsAndReply") customerId:String,
        @Query("page") page: Int
    ): Response<EndPointResponse<TweetsModel>>

    @GET("tweets")
    suspend fun userTweets(
        @Query("customer") customerId:String,
        @Query("page") page: Int
    ): Response<EndPointResponse<TweetsModel>>



    @Multipart
    @POST("update-account")
    suspend fun updateProfile(
         @Query("name") name:String,
         @Query("phone") phoneNumber:String,
         @Query("email") email:String,
         @Query("username") username:String?=null,
         @Query("bio") bio:String?=null,
         @Part image: MultipartBody.Part?=null,
         @Part header: MultipartBody.Part?=null,
        @Query("_method") method:String?="PUT"
     ): Response<EndPointResponse<RegisterModel>>

    @POST("update-account")
    suspend fun updateProfile(
        @Query("name") name:String,
        @Query("phone") phoneNumber:String,
        @Query("email") email:String,
        @Query("username") username:String?=null,
        @Query("bio") bio:String?=null,
        @Query("_method") method:String?="PUT"
     ): Response<EndPointResponse<RegisterModel>>

    @POST("logout")
    suspend fun logout(
     @Query("device[token]") fireBaseToken:String?=null,
     @Query("device[type]") type:String="Android",

     ): Response<EndPointResponse<RegisterModel>>

    @POST("update-password")
    suspend fun updatePassword(
        @Query("oldPassword") oldPassword:String,
        @Query("password") password:String,
        @Query("passwordConfirmation") password_confirmation:String,
        @Query("_method") _method:String?="PATCH"
        ): Response<EndPointResponse<RegisterModel>>

    @GET("me")
    suspend fun myProfile(): Response<EndPointResponse<RegisterModel>>

    @GET("allSettingDeliveryMen")
    suspend fun settings(): Response<EndPointResponse<SettingsModel>>

    @GET("chat/channels")
    suspend fun channels(
            @Query("page") page: Int?=1
    ): Response<EndPointResponse<RoomsModel>>

    @GET("channel/messages")
    suspend fun messages(
            @Query("userId") otherId:Int,
            @Query("tweet") tweet:String,
            @Query("userType") userType:String?="customers",
            @Query("paginate") paginate:Boolean?=false,
            ): Response<EndPointResponse<MessagesModel>>


    @POST("message")
    suspend fun sendMsg(
            @Query("user") otherId:Int,
            @Query("tweet") tweet:Int,
            @Query("message") message:String?=null,
            @Query("type") type:String?="text",
            @Query("userType") userType:String?="customers"
    ): Response<EndPointResponse<MessageModel>>

    @Multipart
    @POST("message")
    suspend fun sendMsg(
            @Query("user") otherId:Int,
            @Query("tweet") tweet:Int,
            @Query("message") message:String?=null,
            @Query("type") type:String?="text",
            @Query("userType") userType:String?="customers",
            @Part files:List<MultipartBody.Part>?=null
            ): Response<EndPointResponse<MessageModel>>

    /*@GET("chat/channels")
    suspend fun channels(): Response<EndPointResponse<ChannelModel>>

    @GET("channel/messages")
    suspend fun messages(
            @Query("userId") otherUserId:String?=null,
            @Query("tweetId") tweetId:String,
            @Query("userType") userType:String?="customers",
            @Query("paginate") paginate:Boolean?=false
    ): Response<EndPointResponse<ChannelModel>>*/

    @GET("orders/now")
    suspend fun incomingOrders(): Response<EndPointResponse<OrdersModel>>

    @GET("orders/{order_id}/accepted")
    suspend fun acceptOrder(
            @Path("order_id") orderId:String
            ): Response<EndPointResponse<OrdersModel>>

    @GET("orders/{order_id}/rejected")
    suspend fun rejectOrder(
            @Path("order_id") orderId:String,
            @Query("deliveryReasonsRejected") reasonId:Int?=null,
            @Query("deliveryReasonsRejectedNotes") note:String?=null
            ): Response<EndPointResponse<OrdersModel>>

    @GET("deliveryReasons-rejected")
    suspend fun rejectReasons(): Response<EndPointResponse<ReasonsModel>>

    @GET("deliveryReasons-notCompleted")
    suspend fun notReceivedReasons(): Response<EndPointResponse<ReasonsModel>>

    @POST("orders/deliveryOnTheWay")
    suspend fun deliverFromRestaurant(
        @Query("orders[]") orderIds:List<Int>? = null
    ): Response<EndPointResponse<OrdersModel>>

    @POST("orders/{order_id}/completed")
    suspend fun completeOrder(
        @Path("order_id") orderId:String
    ): Response<EndPointResponse<OrdersModel>>


    @POST("orders/{order_id}/notCompleted")
    suspend fun notCompleteOrder(
        @Path("order_id") orderId:String,
        @Query("deliveryReasonsNotCompletedOrder") reasonId:Int?=null,
        @Query("deliveryReasonsNotCompletedOrderNotes") note:String?=null
    ): Response<EndPointResponse<OrdersModel>>



    @GET("orders/{order_id}")
    suspend fun showSingleOrder(
        @Path("order_id") orderId:String
    ): Response<EndPointResponse<SingleOrderModel>>

    @GET("orders/history")
    suspend fun history(
        @Query("from") from:String?=null,
        @Query("to") to:String?=null,
        @Query("date") date:String?=null,
        @Query("page") page:Int=1

    ): Response<EndPointResponse<HistoryModel>>

    @GET("me/location-listRestaurant")
    suspend fun showMap(): Response<EndPointResponse<MapLocationModel>>

    @GET("walletDelivery")
    suspend fun wallet(
            @Query("page") page:Int=1
    ): Response<EndPointResponse<WalletModel>>


    @GET("notifications")
    suspend fun notifications(
            @Query("page") page:Int=1
    ): Response<EndPointResponse<NotificationModel>>

    @DELETE("notifications/{id}")
    suspend fun removeNotification(
        @Path("id") id:String

    ): Response<EndPointResponse<Any>>



    @GET("conditions-working-delivery")
    suspend fun conditionsWorkDelivery(): Response<EndPointResponse<SettingsModel>>

















}