package com.platCourse.platCourseAndroid.menu.notifications

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentMenuBinding
import com.platCourse.platCourseAndroid.databinding.FragmentNotificationsBinding
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.platCourse.platCourseAndroid.menu.adapter.MenuAdapter
import com.platCourse.platCourseAndroid.menu.notifications.adapter.NotificationsAdapter
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.notification_model.NotificationItem

class NotificationsFragment : BaseFragment(R.layout.fragment_notifications) {

    private val viewModel: MenuViewModel by viewModels()
    private val binding by viewBinding<FragmentNotificationsBinding>()
    private val notificationAdapter by lazy { NotificationsAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRec()
        sendRequestNotifications()
        handleObservables()
        setupActions()
    }

    private fun setupActions() {
        notificationAdapter.onClickItem=::onNotificationClick
    }

    private fun onNotificationClick(notificationItem: NotificationItem, pos: Int) {
        notificationAdapter.updateSelectedItem(position = pos)
        binding.tvAll.setOnClickListener {
            notificationAdapter.seeAllNotifications()
        }
    }

    private fun handleObservables() {
        handleSharedFlow(viewModel.notificationFlow,onSuccess = { it as List<NotificationItem>
            if (it.isEmpty()) showEmpty()
            else notificationAdapter.swapData(it)
        })
        handleSharedFlow(viewModel.notificationRemoveFlow,onSuccess = {})
    }

    private fun showEmpty() {

    }

    private fun sendRequestNotifications() {
        viewModel.showNotifications()
    }

    private fun setupRec() {
        binding.rvNotifications.layoutManager=LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter=notificationAdapter

    }
}