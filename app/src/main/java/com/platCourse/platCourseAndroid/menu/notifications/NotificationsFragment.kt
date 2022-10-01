package com.platCourse.platCourseAndroid.menu.notifications

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentNotificationsBinding
import com.platCourse.platCourseAndroid.home.course_sections.files.PdfReaderActivity
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.platCourse.platCourseAndroid.menu.notifications.adapter.NotificationsAdapter
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.app.data.model.notification_model.NotificationModel
import com.rowaad.app.data.model.notification_model.NotificationType
import com.rowaad.app.data.model.notification_model.getNotificationTypeEnum
import com.rowaad.utils.extention.handlePagination
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show


class NotificationsFragment : BaseFragment(R.layout.fragment_notifications) {

    private val viewModel: MenuViewModel by viewModels()
    private val binding by viewBinding<FragmentNotificationsBinding>()
    private val notificationAdapter by lazy { NotificationsAdapter() }

    lateinit var layoutManager: LinearLayoutManager

    // Store a member variable for the listener
    private var page=1
    private var next:String?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRec()
        sendRequestNotifications()
        handleObservables()
        setupActions()
        handlePage()
    }

    private fun handlePage() {
        binding.rvNotifications.handlePagination {
            if (hasNext()) {
                page++
                sendRequestNotifications()
            }
        }
    }

    private fun setupActions() {
        notificationAdapter.onClickItem = ::onNotificationClick

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
        when (notificationItem.getNotificationTypeEnum()) {
            NotificationType.QUIZ -> {
                findNavController().navigate(
                    R.id.action_global_quizWebViewFragment, bundleOf(
                        "course_id" to notificationItem.notification?.course_id,
                        "quiz" to notificationItem.notification?.object_id
                    )
                )
            }
            NotificationType.UNKNOWN -> {}
            NotificationType.LESSON -> {
                // expected lesson_id , course id
                // fetch course lessons by course id
                // filter lessons with lesson_id
                // Then open course details with response

                //but for now lessons by course id
                if (notificationItem.notification?.course_id != null) {
                    findNavController().navigate(R.id.courseLessonsFragment, bundleOf(
                        "course_id" to notificationItem.notification?.course_id

                    ))
                }
            }
            NotificationType.COURSE_FILE -> {
                startActivity(Intent(requireContext(), PdfReaderActivity::class.java).also {
                    it.putExtra("pdf", notificationItem.notification?.action_url)
                })
            }
            NotificationType.ANNOUNCEMENT -> {
                //TODO to be implemented later
            }
        }


    }

    private fun handleObservables() {
        handleSharedFlow(viewModel.notificationFlow, onSuccess = {
            it as NotificationModel
            if (it.result.isEmpty()) showEmpty()

            else {
                next=it.next
                notificationAdapter.swapData(it.result)
            }
        })
        handleSharedFlow(viewModel.notificationRemoveFlow, onSuccess = {})
    }

    private fun showEmpty() {
        binding.emptyLay.root.show()
        binding.rvNotifications.show()
        binding.tvAll.hide()
    }

    private fun sendRequestNotifications() {
        viewModel.showNotifications(page)
    }

    private fun setupRec() {
        layoutManager= LinearLayoutManager(requireContext())
        binding.rvNotifications.layoutManager = layoutManager
        binding.rvNotifications.adapter = notificationAdapter

    }

    fun hasNext()=next!=null
}