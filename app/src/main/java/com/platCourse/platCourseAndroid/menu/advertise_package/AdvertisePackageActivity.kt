package com.platCourse.platCourseAndroid.menu.advertise_package

import android.content.Intent
import android.content.res.ColorStateList
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.model.chanel_model.RoomChat
import com.rowaad.app.data.model.chanel_model.TweetIds
import com.rowaad.app.data.model.my_subscription.CurrentSubscription
import com.rowaad.app.data.model.my_subscription.MySubscriptionModel
import com.rowaad.app.data.model.tweets_model.Tweets
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentAdvertisePackageBinding
import com.platCourse.platCourseAndroid.home.HomeActivity
import com.platCourse.platCourseAndroid.home.profile.ProfileActivity
import com.platCourse.platCourseAndroid.listener.TweetActions
import com.platCourse.platCourseAndroid.menu.advertise_package.adapter.TweetPrevAdapter
import com.platCourse.platCourseAndroid.menu.advertise_package.viewmodel.AdvertiseViewModel
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.*

class AdvertisePackageActivity : BaseActivity(R.layout.fragment_advertise_package), TweetActions {

    private lateinit var binding: FragmentAdvertisePackageBinding
    private val viewModel: AdvertiseViewModel by viewModels()
    private val tweetsAdapter by lazy { TweetPrevAdapter(this) }

    override fun init() {
        binding = FragmentAdvertisePackageBinding.bind(findViewById(R.id.rootView))
        handleToolbar()
        setupRec()
        sendRequest()
        setupObservables()
    }


    private fun setupObservables() {
        handleSharedFlow(viewModel.subscriptionFlow,onSuccess = { val resp=it as MySubscriptionModel
            handlePackage(resp.currentSubscription).also { handleAds(resp.tweets) }
        })

    }

    private fun handleAds(tweets: List<Tweets>?) {
        if (tweets.isNullOrEmpty()){
            handleEmpty()
        }
        else {
            tweets.let { tweetsAdapter.swapData(it) }
        }
    }

    private fun handleEmpty() {
        showEmptyTweets()
    }

    private fun showEmptyTweets() {
        binding.emptyLay.root.show()
        binding.packageDetailsLay.hide()
        binding.emptyLay.tvEmpty.text=getString(R.string.no_special_ads)
    }

    private fun handlePackage(currentSubscription: CurrentSubscription?) {
        if (currentSubscription?.paymentMethod.isNullOrBlank()){
            binding.paymentLay.hide()
        }
        else{
            binding.paymentLay.show()
        }
        binding.tvNumAds.text=currentSubscription?.subscribedPackage?.numOfAds.toString()
        binding.tvConsumedAd.text=currentSubscription?.totalUsed.toString()
        binding.tvRest.text=currentSubscription?.totalRemained.toString()
        binding.tvPayment.text=currentSubscription?.paymentMethod.toString()
        if (currentSubscription?.subscribedPackage?.hasDiscount==true)
            binding.tvPaymentValue.text=currentSubscription?.subscribedPackage?.priceAfterDiscountText
        else
            binding.tvPaymentValue.text=currentSubscription?.subscribedPackage?.priceBeforeDiscountText

        when (currentSubscription?.status) {
            "approved" -> binding.tvStatus.text=getString(R.string.package_run).also { binding.statusLay.backgroundTintList=
                ColorStateList.valueOf(ContextCompat.getColor(this,R.color.soft_green))
            }
            "pending" -> binding.tvStatus.text=getString(R.string.package_pending).also { binding.statusLay.backgroundTintList=
                ColorStateList.valueOf(ContextCompat.getColor(this,R.color.light_grey_blue))
            }
            else -> binding.tvStatus.text=getString(R.string.package_completed).also { binding.statusLay.backgroundTintList=
                ColorStateList.valueOf(ContextCompat.getColor(this,R.color.tomato))
            }
        }


    }

    private fun sendRequest() {
        viewModel.getSubscriptions()
    }

    private fun setupRec() {
        binding.rvTweets.layoutManager= LinearLayoutManager(this)
        binding.rvTweets.adapter=tweetsAdapter
    }

    private fun handleToolbar() {
        binding.toolbar.tvTitle.text=getString(R.string.packages)
        binding.toolbar.ivBack.setOnClickListener { onBackPressed() }
    }


     override fun onClickWhats(item: String?) {
        IntentUtils.openWhatsappIntent(item ?: "", this)
    }

    override fun onClickCopy(item: String) {
        copy(item).also { toast(getString(R.string.copied)) }
    }

    override fun onClickLike(item: Tweets) {

    }

    override fun onClickRetweet(item: Tweets, isRetweet: Boolean, onRetweetBack: () -> Unit) {

    }


    override fun onClickChat(item: Tweets) {
        val room= RoomChat(id = item.id!!,
                tweet = TweetIds(id = item.id!!,whatsappNumber = item.whatsappNumber,tweetId = 0),
                createdAt = item.createdAt,
                otherCustomer = item.customer
        ).toJson()
        navigateToRoom(room)
    }


    override fun onClickBlock(item: Tweets) {
        navigateToBlock(item)
    }

    override fun onClickAction(item: Tweets) {
        navigateToDetails(item)
    }

    override fun onClickHashTag(word: String?,item: Tweets) {
        if (word?.contains("#",true)==true) {
            navigateToHashTag(word)
        }
        else{
            navigateToDetails(item)
        }
    }



    override fun onClickProfile(item: Tweets) {
        startActivity(Intent(this, ProfileActivity::class.java).also { it.putExtra("userId",item.customer?.id) })
    }


    override fun onClickGuest() {
        showVisitorDialog(binding.root){}
    }


    private fun navigateToBlock( item:Tweets) {
        val pendingIntent = NavDeepLinkBuilder(this)
                .setComponentName(HomeActivity::class.java)
                .setGraph(R.navigation.home_nav_graph)
                .setDestination(R.id.reportFragment)
                .setArguments(bundleOf(
                        "tweetId"
                                to
                         item.id.toString()
                ))
                .createPendingIntent()
        pendingIntent.send()
    }

    private fun navigateToHashTag( word:String?) {
        val pendingIntent = NavDeepLinkBuilder(this)
                .setComponentName(HomeActivity::class.java)
                .setGraph(R.navigation.home_nav_graph)
                .setDestination(R.id.hashFragment)
                .setArguments(bundleOf(
                        "hashTag"
                                to
                                word?.trim()
                ))
                .createPendingIntent()
        pendingIntent.send()
    }

    private fun navigateToRoom( room:String?) {
        val pendingIntent = NavDeepLinkBuilder(this)
                .setComponentName(HomeActivity::class.java)
                .setGraph(R.navigation.home_nav_graph)
                .setDestination(R.id.chatFragment)
                .setArguments( bundleOf(
                        "chat"
                           to
                         room
                )
                )
                .createPendingIntent()
        pendingIntent.send()
    }

    private fun navigateToDetails( item: Tweets) {
        val pendingIntent = NavDeepLinkBuilder(this)
                .setComponentName(HomeActivity::class.java)
                .setGraph(R.navigation.home_nav_graph)
                .setDestination(R.id.detailsFragment)
                .setArguments(bundleOf(
                        "tweetId"
                                to
                                item.id.toString()
                ))
                .createPendingIntent()
        pendingIntent.send()
    }
}


