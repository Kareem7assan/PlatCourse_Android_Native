package com.platCourse.platCourseAndroid.home.course_sections.rates

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentQuizBinding
import com.platCourse.platCourseAndroid.databinding.FragmentRateBinding
import com.platCourse.platCourseAndroid.home.course_sections.rates.adapter.ReviewAdapter
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.cache.fromJson
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.reviews.Review
import org.jetbrains.anko.support.v4.toast

class RateCourseFragment:BaseFragment(R.layout.fragment_rate) {

    private var course: CourseItem? = null
    private val binding by viewBinding<FragmentRateBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()
    private val adapter by lazy {
        ReviewAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        course = arguments?.getString("course")?.fromJson<CourseItem>()
        handleRec()
        sendRequestReviews()
        setupActions()
        handleObservable()

    }

    private fun handleObservable() {
        handleSharedFlow(viewModel.reviewsFlow,onSuccess = { it as List<Review>
           if (it.isNotEmpty()) adapter.swapData(it)
        })
        handleSharedFlow(viewModel.rateFlow,onSuccess = {
            addReview()
        },lifeCycle = Lifecycle.State.CREATED)
    }

    private fun setupActions() {
        binding.rateBtn.setOnClickListener {
            addReviewRequest()
        }
    }

    private fun addReviewRequest() {
        if (viewModel.hasReview(binding.etComment.text.toString())) {
            sendRequestAddReview()
        }
        else{
            toast(getString(R.string.review_required))
        }
    }

    private fun addReview() {
        adapter.addReview(Review(
                rate = binding.ratingBar.rating,
                description = binding.etComment.text.toString(),
                created_at = getString(R.string.now),
                reviewer_name = viewModel.getUser()?.name,
                id = 0
        ))
    }

    private fun sendRequestAddReview() {
        viewModel.sendRequestAddRate(courseId = course?.id ?: 0,binding.ratingBar.rating,binding.etComment.text.toString())
    }

    private fun handleRec() {
        binding.rvRates.layoutManager=LinearLayoutManager(requireContext())
        binding.rvRates.adapter=adapter
    }

    private fun sendRequestReviews() {
        viewModel.sendRequestReviews(courseId = course?.id ?: 0)
    }
}