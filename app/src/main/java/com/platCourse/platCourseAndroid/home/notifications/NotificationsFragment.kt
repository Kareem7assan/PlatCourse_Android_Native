package com.platCourse.platCourseAndroid.home.notifications

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentNotificationsBinding
import com.platCourse.platCourseAndroid.home.notifications.adapter.NotificationsAdapter
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.utils.extention.handlePagination
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show

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