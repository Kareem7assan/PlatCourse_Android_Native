package com.platCourse.platCourseAndroid.menu.notifications

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.rowaad.app.data.model.notification_model.NotificationType
import com.rowaad.app.data.model.notification_model.getNotificationTypeEnum
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show

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

        binding.tvAll.setOnClickListener {
            notificationAdapter.seeAllNotifications().also {
                viewModel.seeAllNotifications()
            }
        }
    }

    private fun onNotificationClick(notificationItem: NotificationItem, pos: Int) {
        //mark notification as a read
        if (!notificationItem.read) {
            notificationAdapter.updateSelectedItem(pos, item = notificationItem.also {
                it.read = true
            })
            viewModel.seeNotification(notificationId = notificationItem.id)
        }

        //navigate to notification's destination view
        //TODO add more actions to enum and here determine where click listener should navigate to
        when(notificationItem.getNotificationTypeEnum()){
            NotificationType.QUIZ -> {
                findNavController().navigate(R.id.quizFragment, bundleOf(
                    "course_id" to notificationItem.notification?.course_id
                ))
            }
            NotificationType.UNKNOWN -> {}
            null -> {}
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
        binding.emptyLay.root.show()
        binding.rvNotifications.show()
        binding.tvAll.hide()
    }

    private fun sendRequestNotifications() {
        viewModel.showNotifications()
    }

    private fun setupRec() {
        binding.rvNotifications.layoutManager=LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter=notificationAdapter

    }
}