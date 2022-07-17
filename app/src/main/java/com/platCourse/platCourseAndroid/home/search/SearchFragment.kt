package com.platCourse.platCourseAndroid.home.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.chanel_model.RoomChat
import com.rowaad.app.data.model.chanel_model.TweetIds
import com.rowaad.app.data.model.orders.CategoriesItem
import com.rowaad.app.data.model.tweets_model.PaginationInfo
import com.rowaad.app.data.model.tweets_model.Tweets
import com.rowaad.dialogs_utils.WarningPermissionDialog
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentSearchBinding
import com.platCourse.platCourseAndroid.home.dialogs.retweet.RetweetBottomSheet
import com.platCourse.platCourseAndroid.home.profile.ProfileActivity
import com.platCourse.platCourseAndroid.home.search.viewmodel.SearchViewModel
import com.platCourse.platCourseAndroid.home.time_line.adapter.SubCatAdapter
import com.platCourse.platCourseAndroid.home.time_line.adapter.TweetAdapter
import com.platCourse.platCourseAndroid.home.time_line.viewmodel.TimeLineViewModel
import com.platCourse.platCourseAndroid.listener.TweetActions
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.textColorResource

class SearchFragment : BaseFragment(R.layout.fragment_search), TweetActions {

    private var keyWord: String?=null
    private var totalPages: Int=0
    private  var selectedCat: CategoriesItem?=null
    private var pageNumber=1
    private val subCategoryAdapter by lazy { SubCatAdapter() }
    private val tweetsAdapter by lazy { TweetAdapter(this,actionsViewModel.isGuest(),actionsViewModel.getUser()?.id) }
    private val binding by viewBinding<FragmentSearchBinding>()
    private var isNearBy=false
    private var hasRegion=false
    private var hasShowFollowers=false
    private var hasCity=false
    private var isFirstTime=true
    private var selectedPos: Int = 0

    private val viewModel: SearchViewModel by activityViewModels()
    private val actionsViewModel: TimeLineViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val locationSettings by lazy {
        LocationRequest.create().apply {
            interval = 300000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleSearch()
        handleToolbar()
        sendRequestCategories()
        observeCategories()
        observeSubCategories()
        observeTweets()
        handleRecSubCat()
        handleTweetsRec()
        setupActions()
        handlePage()
        //handleAnimation()
        setupCallbackLocation()
        handleRegions()
        handleCities()
        disableSearch()
        handleShowFollowers()
    }

    private fun handleShowFollowers() {
        if (actionsViewModel.isGuest())
            binding.showFollowersTweets.hide()
        else
            binding.showFollowersTweets.show().also { handleAnimation() }
    }
    private fun disableSearch() {
        binding.searchBar.etSearch.isClickable=true
        binding.searchBar.etSearch.isFocusable=false
    }

    private fun handleToolbar() {
        binding.searchBar.ivBack.onClick {
            findNavController().popBackStack()
        }
    }

    private fun handleSearch() {
        //handle search
        binding.searchBar.cardSearch.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.searchBar.etSearch.setOnClickListener {
            findNavController().navigateUp()
        }
        keyWord = arguments?.getString("word")
        viewModel.searchKey = keyWord

    }



    private fun handleAnimation() {
        binding.scrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY  >= v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    binding.showFollowersTweets.animateHide()
                }
                else{
                    binding.showFollowersTweets.animateShow()
                }

            }
        }
    }

    private fun handleCities() {
        if (viewModel.selectedCities.isNotEmpty()){
            viewModel.selectedCities.mapIndexed { index, region ->
                if (index==0) binding.tvAllCities.text=""
                if (index==viewModel.selectedCities.lastIndex) {
                    binding.tvAllCities.append(region.title)
                }
                else {
                    if (viewModel.selectedCities.size>3 && index==2) binding.tvAllCities.text=binding.tvAllCities.text.toString()+"+"+(viewModel.selectedCities.size-3).toString()
                    else binding.tvAllCities.append(region.title+",")
                }

                handleCity(true).also { binding.citiesLay.show() }
            }

        }
        else{
            binding.tvAllCities.text=getText(R.string.all_cities)
            handleCity(false).also { binding.citiesLay.hide() }
        }
    }

    private fun handleRegions() {
        if (viewModel.selectedRegions.isNotEmpty()){
            viewModel.selectedRegions.mapIndexed { index, region ->
                if (index==0) binding.tvAllRegions.text=""
                if (index==viewModel.selectedRegions.lastIndex) binding.tvAllRegions.append(region.title)
                else binding.tvAllRegions.append( region.title+",")
                handleRegion(true)
            }

        }
        else{
            binding.tvAllRegions.text=getText(R.string.all_regions)
            handleRegion(false)
        }
    }

    private fun handlePage() {
        binding.scrollView.handlePagination {
            pageNumber++
            if (hasNext())sendRequestTweets(selectedCat?.id)

        }
    }

    private fun hasNext(): Boolean {
        return pageNumber <= totalPages
    }

    private fun observeTweets() {
        handleSharedFlow(viewModel.tweetSearchFlow ,onShowProgress = {
            if (pageNumber>1) showBottomProgress() else showProgressContent()
        },onHideProgress = {
            if (pageNumber>1) hideBottomProgress() else hideProgressContent()

        } ,onSuccess = {it as Pair<List<Tweets>?, PaginationInfo?>
            totalPages=it.second?.numberOfPages ?: 0
            val totalRecords=it.second?.totalRecords
            if (pageNumber==1 && it.first!!.isEmpty()){
                showEmptyTweets()
            }
            else {
                tweetsAdapter.addData(it.first!!).also { showTweets() }.also { handleSearchBarFormula(totalRecords.toString()) }
            }
        })
    }

    private fun handleSearchBarFormula(totalRecords: String) {
        binding.tvCountSearch.text = String.format(getString(R.string.search_result_count),totalRecords,viewModel.searchKey)
    }

    private fun showBottomProgress() {
        binding.progressMore?.show()
    }
    private fun showProgressContent() {
        binding.progressLay.root.show()
        binding.rvTweets.hide()
        binding.emptyLay.root.hide()
    }
    private fun hideProgressContent() {
        binding.progressLay.root.hide()
    }

    private fun hideBottomProgress() {
        binding.progressMore.hide()
    }

    private fun showTweets() {
        binding.emptyLay.root.hide()
        binding.rvTweets.show()
        if (actionsViewModel.isGuest().not()) binding.showFollowersTweets.show()
    }

    private fun showEmptyTweets() {
        binding.emptyLay.root.show()
        binding.tvCountSearch.text = String.format(getString(R.string.search_result_count),0,viewModel.searchKey)

        binding.rvTweets.hide()
        binding.progressLay.root.hide()
        binding.showFollowersTweets.hide()
    }

    private fun handleTweetsRec() {
        tweetsAdapter.clear()
        binding.rvTweets.layoutManager= LinearLayoutManager(requireContext())
        binding.rvTweets.adapter=tweetsAdapter
    }

    private fun observeSubCategories() {
        handleSharedFlow(viewModel.subCategoriesFlow,onSuccess = {it as MutableList<CategoriesItem>
            if (it.isNotEmpty()) {
                it.add( 0, CategoriesItem(id=selectedCat?.id ?: 0,title = getString(R.string.all_categories)))
                subCategoryAdapter.swapData(it).also {
                    binding.rvSubCat.show()
                }
            }
            else{
                binding.rvSubCat.hide()
            }
            //initilaize request for all sub categories
            sendRequestTweets(selectedCat?.id)
        })
    }

    private fun handleRecSubCat() {
        binding.rvSubCat.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvSubCat.adapter=subCategoryAdapter
    }

    private fun setupActions() {
        subCategoryAdapter.onClickItem=::onClickSubCategorey
        binding.nearByLay.setOnClickListener {
            isNearBy=!isNearBy
            viewModel.hasNearBy=isNearBy
            handleNear(isNearBy)
            if (isNearBy) {
                resetPage()
                startLocationUpdates()
            }
            else{
                sendRequestTweets(selectedCat?.id)
            }
        }
        binding.regionsLay.setOnClickListener {
            hasRegion=!hasRegion
            handleRegion(hasRegion)
            findNavController().navigate(R.id.action_searchFragment_to_regionsSearchFragment)
        }
        binding.citiesLay.setOnClickListener {
            hasCity=!hasCity
            handleCity(hasCity)
            findNavController().navigate(R.id.action_searchFragment_to_citiesSearchFragment)
        }
        binding.showFollowersTweets.setOnClickListener {
            hasShowFollowers=!hasShowFollowers
            handleFollowers(hasShowFollowers)
            viewModel.isShowFollowers=hasShowFollowers
            resetPage()
            sendRequestTweets(selectedCat?.id)
        }
    }

    private fun startLocationUpdates() {
        if (requireActivity().isGpsEnabled()) {
            checkPermissionLocation {
                if (it){
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION

                        ),
                        100
                    )
                }
                else{
                    handleFusedLocation()
                }
            }
        }
        else {
            WarningPermissionDialog.show(requireActivity()) { dialog -> navigateSettings().also {dialog.dismiss()}}
        }



    }


    private fun setupCallbackLocation() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                val location=locationResult.locations.last()
                Log.e("location", "$location,$isFirstTime")
                viewModel.location=location
                if (location!=null && isFirstTime) sendRequestTweets(selectedCat?.id).also { isFirstTime=false }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleFusedLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.requestLocationUpdates(
            locationSettings,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun navigateSettings() {
        startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 100)
    }


    private fun handleNear(nearBy: Boolean) {
        if (nearBy)
            binding.nearByLay.backgroundResource=R.drawable.stroke_water_bg_solid_dark
        else
            binding.nearByLay.backgroundResource=R.drawable.stroke_bg_solid_white
    }
    private fun handleRegion(hasRegion: Boolean) {
        if (hasRegion)
            binding.regionsLay.backgroundResource=R.drawable.stroke_water_bg_solid_dark
        else
            binding.regionsLay.backgroundResource=R.drawable.stroke_bg_solid_white
    }
    private fun handleFollowers(isShowFollowers: Boolean) {
        if (isShowFollowers)
        {
            binding.showFollowersTweets.backgroundResource=R.drawable.people_background_btn
            binding.tvShowTweets.textColorResource=R.color.white
            binding.ivFlag.tintImage(R.color.white)
            binding.tvShowTweets.text=getString(R.string.show_all)
        }
        else{
            binding.showFollowersTweets.backgroundResource=R.drawable.people_background_white_btn
            binding.tvShowTweets.textColorResource=R.color.water_blue
            binding.ivFlag.tintImage(R.color.water_blue)
            binding.tvShowTweets.text=getString(R.string.show_tweets_followers)
        }
    }

    private fun handleCity(hasCity: Boolean) {
        if (hasCity)
            binding.citiesLay.backgroundResource=R.drawable.stroke_water_bg_solid_dark
        else
            binding.citiesLay.backgroundResource=R.drawable.stroke_bg_solid_white
    }

    private fun onClickSubCategorey(categoriesItem: CategoriesItem, pos: Int) {
        resetPage()
        subCategoryAdapter.updateSelectedItem(pos).also { sendRequestTweets(categoriesItem.id) }
        this.selectedCat=categoriesItem

    }

    private fun sendRequestTweets(id: Int?=null) {
        viewModel.sendRequestTweetsSearch(catId = id,pageNumber = pageNumber)
    }

    private fun observeCategories() {
        handleSharedFlow(viewModel.categoriesFlow,onSuccess = { it as MutableList<CategoriesItem>
            setupTabs(it).also {
                if (selectedCat?.id==0 || selectedCat?.id==null) sendRequestTweets()
                else sendRequestTweets(id = selectedCat?.id) }
        })
    }

    private fun setupTabs(cats: MutableList<CategoriesItem>) {
        binding.tabLay.removeAllTabs()
        if (cats.isNotEmpty()) viewModel.parentCategory=cats.first()

        cats.add(0,CategoriesItem(title = getString(R.string.all_categories),id = 0))
        cats.map {
            binding.tabLay.addTab(binding.tabLay.newTab().setText(it.title))
        }

        binding.tabLay.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val item=cats[tab?.position ?: 0]
                selectedPos=tab?.position ?: 0
                viewModel.parentCategory=item
                resetPage()
                if (item.id ?: 0 > 0) sendRequestSubCategory(item).also {if (subCategoryAdapter.selectedItemPosition==0) selectedCat=item }
                else sendRequestTweets().also { selectedCat=null }.also { resetSubCategory() }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }
    private fun resetSubCategory() {
        binding.rvSubCat.hide()
        subCategoryAdapter.clear()
    }




    private fun resetPage() {
        pageNumber=1
        tweetsAdapter.clear()
    }

    private fun sendRequestSubCategory(item: CategoriesItem) {
        viewModel.sendRequestSubCategories(item.id!!)
    }

    private fun sendRequestCategories() {
        viewModel.sendRequestCategories()
    }

    override fun onClickWhats(item: String?) {
        IntentUtils.openWhatsappIntent(item ?: "", requireContext())

    }

    override fun onClickCopy(item: String) {
        requireActivity().copy(item).also { toast(getString(R.string.copied)) }
    }

    override fun onClickChat(item: Tweets) {
        val room= RoomChat(id = item.id!!,
                tweet = TweetIds(id = item.id!!,whatsappNumber = item.whatsappNumber,tweetId = 0),
                createdAt = item.createdAt,
                otherCustomer = item.customer
        )
        findNavController().navigate(R.id.action_global_chatFragment,
                bundleOf(
                        "chat"
                                to
                                room.toJson()
                )
        )
    }

    override fun onClickLike(item: Tweets) {
        actionsViewModel.like(tweetId = item.id.toString())

    }

    override fun onClickRetweet(item: Tweets, isRetweet: Boolean, onRetweetBack: () -> Unit) {
        RetweetBottomSheet(isRetweet = isRetweet,onRetweet = {
            onRetweetBack.invoke()
            actionsViewModel.retweet(item.id.toString())
        }).show(requireActivity().supportFragmentManager,"")


    }



    override fun onClickBlock(item: Tweets) {
        findNavController().navigate(R.id.action_global_reportFragment
                ,
                bundleOf(
                        "tweetId"
                                to
                        item.id.toString()
                )

        )
    }

    override fun onClickAction(item: Tweets) {
        findNavController().navigate(R.id.action_global_detailsFragment, bundleOf(
                "tweetId" to item.id.toString()
        ))
    }

    override fun onClickProfile(item: Tweets) {
        startActivity(Intent(requireContext(), ProfileActivity::class.java).also { it.putExtra("userId",item.customer?.id) })
    }

    override fun onClickHashTag(word: String?,item: Tweets) {
        if (word?.contains("#",true)==true) {
            findNavController().navigate(
                    R.id.action_global_hashFragment, bundleOf(
                    "hashTag"
                            to
                            word.trim()
            )
            )
        }
        else{
            findNavController().navigate(R.id.action_global_detailsFragment, bundleOf(
                    "tweetId" to item.id.toString()
            ))
        }
    }


    override fun onClickGuest() {
        (requireActivity() as BaseActivity).showVisitorDialog(binding.root){}
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("onRequestResult", "$requestCode,${grantResults.size}")
        if (requestCode==100 && grantResults.isNotEmpty()) handleFusedLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pageNumber=1
    }


}