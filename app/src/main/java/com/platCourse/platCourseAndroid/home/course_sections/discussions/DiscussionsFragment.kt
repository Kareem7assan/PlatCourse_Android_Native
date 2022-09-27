package com.platCourse.platCourseAndroid.home.course_sections.discussions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentDiscussionsBinding
import com.platCourse.platCourseAndroid.home.course_sections.discussions.dialog.ForumBottomDialog
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.cache.fromJson
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.discussions_model.DiscussionModel

class DiscussionsFragment  : BaseFragment(R.layout.fragment_discussions) {

    private var size: Int=0

    private var course: CourseItem? = null
    private val binding by viewBinding<FragmentDiscussionsBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()
    private val adapter by lazy {
        DiscussionAdapter(viewModel.getUser())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        course = arguments?.getString("course")?.fromJson<CourseItem>()
        handleRec()
        sendRequestDiscussions()
        setupActions()
        handleObservables()
    }

    private fun setupActions() {
        binding.addDiscBtn.setOnClickListener {
            ForumBottomDialog{ title: String, desc: String ->
                sendRequestAddDiscussions(title,desc)
            }.show(requireActivity().supportFragmentManager,"dialog")
        }
        adapter.onClickItemComment=::onClickComment
    }

    private fun onClickComment(discussionModel: DiscussionModel,comment:String, i: Int) {
        viewModel.sendRequestAddComment(discId = discussionModel.id,comment)
    }

    private fun handleObservables() {
        handleSharedFlow(viewModel.discFlow,onSuccess = {it as List<DiscussionModel>
            size=it.size
            adapter.swapData(it)
        })
        handleSharedFlow(viewModel.addDiscFlow,onSuccess = {it as DiscussionModel
            adapter.insertItem(size,it).also {
                size++
            }
        })
    }

    private fun sendRequestDiscussions() {
        viewModel.sendRequestDiscussions(courseId = course?.id ?: 0)
    }

    private fun sendRequestAddDiscussions(title: String, desc: String) {
        viewModel.sendRequestAddDiscussions(courseId = course?.id ?: 0,title,desc)
    }

    private fun handleRec() {
        binding.rvDiscussions.layoutManager=LinearLayoutManager(requireContext())
        binding.rvDiscussions.adapter=adapter
    }


}