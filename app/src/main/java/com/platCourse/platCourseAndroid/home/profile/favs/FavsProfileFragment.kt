package com.platCourse.platCourseAndroid.home.profile.favs

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.chanel_model.RoomChat
import com.rowaad.app.data.model.chanel_model.TweetIds
import com.rowaad.app.data.model.tweets_model.PaginationInfo
import com.rowaad.app.data.model.tweets_model.Tweets
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentFavouritesBinding
import com.platCourse.platCourseAndroid.databinding.FragmentFavouritesProfileBinding
import com.platCourse.platCourseAndroid.home.dialogs.retweet.RetweetBottomSheet
import com.platCourse.platCourseAndroid.home.favourites.viewmodel.FavouriteViewModel
import com.platCourse.platCourseAndroid.home.profile.ProfileActivity
import com.platCourse.platCourseAndroid.home.profile.tweets.TweetsProfileFragment
import com.platCourse.platCourseAndroid.home.profile.viewmodel.ProfileViewModel
import com.platCourse.platCourseAndroid.home.time_line.adapter.TweetAdapter
import com.platCourse.platCourseAndroid.listener.TweetActions
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.*
import org.jetbrains.anko.support.v4.toast
import java.util.*

class FavsProfileFragment : BaseFragment(R.layout.fragment_favourites_profile), TweetActions {



    private val viewModel: FavouriteViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var totalPages: Int=0
    private var pageNumber=1
    private val tweetsAdapter by lazy { TweetAdapter(this,viewModel.isGuest(),viewModel.getUser()?.id) }

    private val binding by viewBinding<FragmentFavouritesProfileBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleTweetsRec()
        observeTweets()
        handlePage()
        sendRequestTweets()
        handleRemove()
    }

    private fun handleRemove() {
        tweetsAdapter.onClickItemRemove=::onRemove
    }

    private fun onRemove(tweets: Tweets, index: Int, isLast: Boolean) {
        tweetsAdapter.removeWithIndex(index)
        if (isLast){ showEmptyTweets() }
        viewModel.like(tweets.id.toString())

    }


    private fun handleTweetsRec() {
        tweetsAdapter.clear()
        binding.rvTweets.layoutManager= LinearLayoutManager(requireContext())
        binding.rvTweets.adapter=tweetsAdapter
    }



    private fun observeTweets() {
        handleSharedFlow(profileViewModel.favFlow ,onShowProgress = {
            if (pageNumber>1) showBottomProgress() else showProgressContent()
        },onHideProgress = {
            if (pageNumber>1) hideBottomProgress() else hideProgressContent()

        } ,onSuccess = {it as Pair<List<Tweets>?, PaginationInfo?>
            totalPages=it.second?.numberOfPages ?: 0
            if (pageNumber==1 && it.first!!.isEmpty()){
                showEmptyTweets()
            }
            else {
                tweetsAdapter.addData(it.first!!).also { showTweets() }
            }
        })
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
    }

    private fun showEmptyTweets() {
        binding.emptyLay.root.show()
        binding.rvTweets.hide()
        binding.progressLay.root.hide()
    }

    private fun handlePage() {
        binding.root.handlePagination {
            pageNumber++
            if (hasNext()) viewModel.sendRequestFavs(pageNumber)

        }
    }

    private fun hasNext(): Boolean {
        return pageNumber <= totalPages
    }

    private fun sendRequestTweets() {
        profileViewModel.sendRequestFavs(pageNumber)
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
        viewModel.like(tweetId = item.id.toString())
    }
    override fun onClickRetweet(item: Tweets, isRetweet: Boolean, onRetweetBack: () -> Unit) {
        RetweetBottomSheet(isRetweet = isRetweet,onRetweet = {
            onRetweetBack.invoke()
            viewModel.retweet(item.id.toString())
        }).show(requireActivity().supportFragmentManager,"")

    }

    override fun onClickBlock(item: Tweets) {
        findNavController().navigate(R.id.action_global_reportFragment,
                bundleOf("tweetId" to item.id.toString())

        )
    }

    override fun onClickAction(item: Tweets) {
        findNavController().navigate(R.id.action_global_detailsFragment, bundleOf(
                "tweetId" to item.id.toString()
        ))
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



    override fun onClickProfile(item: Tweets) {
        startActivity(Intent(requireContext(), ProfileActivity::class.java).also { it.putExtra("userId",item.customer?.id) })
    }


    override fun onClickGuest() {
        (requireActivity() as BaseActivity).showVisitorDialog(binding.root){}
    }



    companion object{
        fun newInstance(): FavsProfileFragment {
            val args = Bundle()
            val fragment = FavsProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        pageNumber=1

    }
}