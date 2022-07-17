package com.platCourse.platCourseAndroid.home.profile.replies

import android.R.attr.bitmap
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.data.model.chanel_model.RoomChat
import com.rowaad.app.data.model.chanel_model.TweetIds
import com.rowaad.app.data.model.tweets_model.PaginationInfo
import com.rowaad.app.data.model.tweets_model.Tweets
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentRepliesBinding
import com.platCourse.platCourseAndroid.home.dialogs.retweet.RetweetBottomSheet
import com.platCourse.platCourseAndroid.home.profile.ProfileActivity
import com.platCourse.platCourseAndroid.home.profile.viewmodel.ProfileViewModel
import com.platCourse.platCourseAndroid.home.time_line.adapter.TweetAdapter
import com.platCourse.platCourseAndroid.home.time_line.viewmodel.TimeLineViewModel
import com.platCourse.platCourseAndroid.listener.TweetActions
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.*
import org.jetbrains.anko.support.v4.toast


class RepliesProfileFragment :BaseFragment(R.layout.fragment_replies), TweetActions {

    private var manger: LinearLayoutManager? = null
    private lateinit var binding: FragmentRepliesBinding
    private val viewModel: TimeLineViewModel by viewModels()
    private val profileViewModel : ProfileViewModel by activityViewModels()
    private var totalPages: Int=0
    private var pageNumber=1
    private val tweetsAdapter by lazy { TweetAdapter(this, viewModel.isGuest(),viewModel.getUser()?.id) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentRepliesBinding.bind(view)
        handleTweetsRec()
        handlePage()
        sendRequestTweets()
        observeTweets()

    }

    private fun handlePage() {
        binding.scrollView.handlePagination {
            pageNumber++
            if (hasNext())sendRequestTweets()

        }
    }

    private fun sendRequestTweets() {
        profileViewModel.sendRequestReplies(pageNumber)
    }


    private fun hasNext(): Boolean {
        return pageNumber <= totalPages
    }


    private fun handleTweetsRec() {
        manger =  LinearLayoutManager(requireContext())
        binding.rvTweets.layoutManager=manger
        binding.rvTweets.adapter=tweetsAdapter
    }

    private fun observeTweets() {
        handleSharedFlow(profileViewModel.tweetRepliesFlow, onShowProgress = {
            if (pageNumber > 1) showBottomProgress() else showProgressContent()
        }, onHideProgress = {
            if (pageNumber > 1) hideBottomProgress() else hideProgressContent()

        }, onSuccess = {
            it as Pair<List<Tweets>?, PaginationInfo?>
            totalPages = it.second?.numberOfPages ?: 0
            if (pageNumber == 1 && it.first!!.isEmpty()) {
                showEmptyTweets()
            } else {
                tweetsAdapter.addData(it.first!!).also { showTweets() }
            }
        }, lifeCycle = Lifecycle.State.CREATED)
    }

    private fun showBottomProgress() {
        binding.progressMore.show()
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
        RetweetBottomSheet(isRetweet = isRetweet, onRetweet = {
            onRetweetBack.invoke()
            viewModel.retweet(item.id.toString())
        }).show(requireActivity().supportFragmentManager, "")

    }



    override fun onClickBlock(item: Tweets) {
        findNavController().navigate(R.id.action_global_reportFragment,
                bundleOf(
                        "tweetId"
                                to
                                item.id.toString()
                )

        )
    }

    override fun onClickAction(item: Tweets) {

            findNavController().navigate(
                R.id.action_global_detailsFragment, bundleOf(
                    "tweetId" to item.id.toString()
                )
            )

    }

    override fun onClickHashTag(word: String?, item: Tweets) {
        if (word?.contains("#", true)==true) {
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
        startActivity(Intent(requireContext(), ProfileActivity::class.java).also { it.putExtra("userId", item.customer?.id) })
    }


    override fun onClickGuest() {
        (requireActivity() as BaseActivity).showVisitorDialog(binding.root){}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pageNumber=1

    }

}