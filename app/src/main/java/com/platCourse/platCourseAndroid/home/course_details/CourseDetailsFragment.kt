package com.platCourse.platCourseAndroid.home.course_details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.FragmentDetailsCourseBinding
import com.platCourse.platCourseAndroid.home.course_details.dialog.CouponBottomDialog
import com.platCourse.platCourseAndroid.home.course_details.dialog.CouponPurchaseBottomDialog
import com.platCourse.platCourseAndroid.home.courses.CoursesViewModel
import com.rowaad.app.base.BaseFragment
import com.rowaad.app.base.viewBinding
import com.rowaad.app.data.cache.fromJson
import com.rowaad.app.data.cache.toJson
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.attribute_course_model.Action
import com.rowaad.app.data.model.attribute_course_model.CourseListener
import com.rowaad.app.data.model.attribute_course_model.ItemCourseDetails
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.app.data.model.teacher_model.TeacherModel
import com.rowaad.utils.IntentUtils
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show


class CourseDetailsFragment : BaseFragment(R.layout.fragment_details_course), MotionLayout.TransitionListener, CourseListener {

    private var videoUrl: String? = null
    private var isPlay: Boolean = false
    private var simplePlayer: ExoPlayer? = null
    private var details: CourseItem? = null
    private val binding by viewBinding<FragmentDetailsCourseBinding>()
    private val viewModel: CoursesViewModel by activityViewModels()

    private val adapter by lazy {
        AttributeAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        details=arguments?.getString("details")?.fromJson<CourseItem>()
        videoUrl=arguments?.getString("url")
        binding.frameLayout.addTransitionListener(this)
        handleWaterMark()
        setupVideo()
        setupRec()
        sendRequestUser()
        handleCourses()
        setupActions()
        handleBuyObservable()
        handleCouponObservable()
        handleContactObservable()
    }

    private fun handleCouponObservable() {
        handleSharedFlow(viewModel.couponFlow,onSuccess = {
            showSuccessMsg(it as String)
        })
        handleSharedFlow(viewModel.couponPurchaseFlow,onSuccess = {
            showSuccessMsg(getString(R.string.purchase_successfully)).also { findNavController().navigateUp() }
        })
    }

    private fun handleContactObservable() {
        handleSharedFlow(viewModel.contactFlow,onSuccess = { it as TeacherModel
            if (it.phone_number.isNullOrBlank().not())IntentUtils.openWhatsappIntent(it.phone_number!! ,requireContext())
        })
    }

    private fun handleBuyObservable() {
        handleSharedFlow(viewModel.buyCourseFlow,onSuccess = { it
            showSuccessMsg(getString(R.string.request_sent))
        })
    }

    private fun setupActions() {
        binding.buyBtn.setOnClickListener {
            viewModel.sendRequestBuyCourse(courseId = details?.id ?: 0)
        }
        binding.couponBtn.setOnClickListener {
            showCouponDialog()
        }
        binding.couponPurchaseBtn.setOnClickListener {
            showCouponPurchaseDialog()
        }
        binding.contactBtn.setOnClickListener {
            viewModel.sendRequestTeacher(details?.ownerId ?: 0)
        }
    }

    private fun showCouponPurchaseDialog() {
        CouponPurchaseBottomDialog{
            sendRequestPurchase(it)
        }.show(requireActivity().supportFragmentManager,"coupon")
    }

    private fun sendRequestPurchase(code: String) {
        viewModel.sendRequestCouponPurchase(details?.id ?: 0,code)

    }

    private fun showCouponDialog() {
        CouponBottomDialog{
            sendRequestCoupon(it)
        }.show(requireActivity().supportFragmentManager,"coupon")
    }

    private fun sendRequestCoupon(coupon: String) {
        viewModel.sendRequestCoupon(details?.id ?: 0,coupon)
    }

    private fun sendRequestUser() {
        viewModel.sendRequestUser()
    }

    private fun handleCourses() {
        handleSharedFlow(viewModel.userFlow,onSuccess = { it as UserModel
            setupAdapter()
        })
    }

    private fun handleWaterMark() {
        if (viewModel.isUserLogin()){
            binding.tvOwner.text = viewModel.getUser()?.username
            binding.tvPhone.text = viewModel.getUser()?.phone_number
            binding.markLay.show()
        }
        else {
            binding.markLay.hide()
        }
    }

    private fun setupAdapter() {
        val items=mutableListOf<ItemCourseDetails>()
        items.add(ItemCourseDetails(title = getString(R.string.desc),desc = details?.overview))
        showBuyButton()
        if (viewModel.isUserLogin() && viewModel.isCourseOwner(details?.id ?: 0)) {
            items.add(ItemCourseDetails(title = getString(R.string.rates),showForward = true,type = Action.RATES))
            showContactButton()
        }

        if (viewModel.isUserLogin() && viewModel.isCourseOwner(details?.id ?: 0)) {
            items.add(ItemCourseDetails(title = getString(R.string.lessons), showForward = true, type = Action.LESSONS))
            items.add(ItemCourseDetails(title = getString(R.string.discussions), showForward = true, type = Action.DISCUSSIONS))
            items.add(ItemCourseDetails(title = getString(R.string.quizes), showForward = true, type = Action.QUIZES))
            items.add(ItemCourseDetails(title = getString(R.string.files), showForward = true, type = Action.FILES))
            items.add(ItemCourseDetails(title = getString(R.string.anouneces), showForward = true, type = Action.ADS))
        }
        adapter.swapData(items)
    }

    private fun showContactButton() {
        binding.contactBtn.show()
        binding.buyBtn.hide()
        binding.couponBtn.hide()
        binding.couponPurchaseBtn.hide()
    }

    private fun showBuyButton() {
        binding.contactBtn.hide()
        binding.buyBtn.show()
        binding.couponBtn.show()
        binding.couponPurchaseBtn.show()
    }

    private fun setupRec() {
        binding.rvAttributes.layoutManager=LinearLayoutManager(requireContext())
        binding.rvAttributes.adapter=adapter
    }


    private fun setupVideo() {
         simplePlayer = ExoPlayer.Builder(requireContext()).build()
         binding.styledVideo.player= simplePlayer
         simplePlayer!!.addMediaItem(MediaItem.fromUri(videoUrl ?: details?.intro!!))

         simplePlayer!!.prepare()




        simplePlayer?.addListener(object : Player.Listener{

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                this@CourseDetailsFragment.isPlay=isPlaying
                if (isPlaying && viewModel.isUserLogin()){
                    playingWaterMark()
                }

            }

        })

        binding.styledVideo.setFullscreenButtonClickListener {
            startActivity(Intent(requireContext(), FullScreenActivity::class.java).also {
                it.putExtra("pos", simplePlayer!!.currentPosition)
                it.putExtra("isPlay", simplePlayer!!.isPlaying)
                it.putExtra("name", viewModel.getUser()?.username)
                it.putExtra("url", videoUrl ?: details?.intro!!)

            })
        }

    }

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

    }

    override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {

    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

             if (isPlay && viewModel.isUserLogin()) playingWaterMark()



    }

    override fun onPause() {
        super.onPause()
        simplePlayer?.pause()
        isPlay=false
    }




    private fun playingWaterMark() {
            binding.frameLayout.transitionToStart()
            binding.frameLayout.transitionToEnd()
    }

    override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {

    }

    override fun onClickLessons() {
        findNavController().navigate(R.id.action_global_courseLessonsFragment, bundleOf(
            "course"
                to
            details.toJson()
        ))
    }

    override fun onClickQuiz() {
        findNavController().navigate(R.id.action_global_quizFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        ))
    }

    override fun onClickDisc() {
        findNavController().navigate(R.id.action_global_discussionsFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        ))
    }

    override fun onClickRates() {
        findNavController().navigate(R.id.action_global_rateCourseFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        ))
    }

    override fun onClickFiles() {
        findNavController().navigate(R.id.action_global_courseFilesFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        ))
    }

    override fun onClickAds() {
        findNavController().navigate(R.id.action_global_announcementsFragment, bundleOf(
                "course"
                        to
                        details.toJson()
        ))

    }
}