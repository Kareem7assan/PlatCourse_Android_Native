package com.platCourse.platCourseAndroid.home.notifications

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.chanel_model.RoomChat
import com.rowaad.app.data.model.chanel_model.TweetIds
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.app.data.model.tweets_model.PaginationInfo
import com.rowaad.app.data.model.tweets_model.Tweets
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentFavouritesBinding
import com.platCourse.platCourseAndroid.databinding.FragmentNotificationsBinding
import com.platCourse.platCourseAndroid.home.favourites.viewmodel.FavouriteViewModel
import com.platCourse.platCourseAndroid.home.notifications.adapter.NotificationsAdapter
import com.platCourse.platCourseAndroid.home.time_line.adapter.TweetAdapter
import com.platCourse.platCourseAndroid.home.time_line.viewmodel.TimeLineViewModel
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.rowaad.utils.extention.handlePagination
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import com.rowaad.utils.extention.toJson

class NotificationsFragment : BaseFragment(R.layout.fragment_notifications){

    private val viewModel: MenuViewModel by activityViewModels()
    private var totalPages: Int=0
    private var pageNumber=1
    private val adapter by lazy { NotificationsAdapter() }
    private val binding by viewBinding<FragmentNotificationsBinding>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleNotificationsRec()
        observeNotifications()
        handlePage()
        sendRequestNotif()
        handleActions()

    }

    private fun handleActions() {
        adapter.onClickItem=::onClickItem
    }

    private fun onClickItem(notificationItem: NotificationItem, i: Int) {

        if (notificationItem.type=="tweet"){
            findNavController().navigate(R.id.action_global_detailsFragment,
            bundleOf(
                    "tweetId"
                        to
                   notificationItem.typeId.toString()
            )
                    )
        }
        else if (notificationItem.type=="message"){
            val room=RoomChat(otherCustomer = notificationItem.customer,id = notificationItem.tweet?.id ?: 0,tweet = TweetIds(id =notificationItem.tweet?.id ?: 0,0)).toJson()
            findNavController().navigate(R.id.action_global_chatFragment,
                    bundleOf(
                            "chat"
                              to
                            room
                    )
                    )
        }
    }

    private fun sendRequestNotif() {
        viewModel.showNotifications(pageNumber)
    }

    private fun handleNotificationsRec() {
        adapter.clear()
        binding.rvMessages.layoutManager=LinearLayoutManager(requireContext())
        binding.rvMessages.adapter=adapter
    }

    private fun observeNotifications() {
        handleSharedFlow(viewModel.notificationFlow ,onShowProgress = {
            if (pageNumber>1) showBottomProgress() else showProgressContent()
        },onHideProgress = {
            if (pageNumber>1) hideBottomProgress() else hideProgressContent()

        } ,onSuccess = {it as Pair<List<NotificationItem>?, PaginationInfo?>
            totalPages=it.second?.numberOfPages ?: 0
            if (pageNumber==1 && it.first!!.isEmpty()){
                showEmpty()
            }
            else {
                adapter.addData(it.first!!).also { showNotifications() }
            }
        })
    }

    private fun showBottomProgress() {
        binding.progressMore?.show()
    }
    private fun showProgressContent() {
        binding.progressLay.root.show()
        binding.rvMessages.hide()
        binding.emptyLay.root.hide()
    }
    private fun hideProgressContent() {
        binding.progressLay.root.hide()
    }

    private fun hideBottomProgress() {
        binding.progressMore.hide()
    }

    private fun showNotifications() {
        binding.emptyLay.root.hide()
        binding.rvMessages.show()
    }

    private fun showEmpty() {
        binding.emptyLay.root.show()
        binding.rvMessages.hide()
        binding.progressLay.root.hide()
    }

    private fun handlePage() {
        binding.root.handlePagination {
            pageNumber++
            if (hasNext()) viewModel.showNotifications(pageNumber)

        }
    }
    private fun hasNext(): Boolean {
        return pageNumber <= totalPages
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pageNumber=1
    }
}