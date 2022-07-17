package com.platCourse.platCourseAndroid.image_details

import android.widget.Toast
import com.davemorrissey.labs.subscaleview.ImageSource
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.utils.Constants_Api
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityImageDetailsBinding
import com.rowaad.utils.BitmapUtils
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers


@AndroidEntryPoint
class ImageDetailsActivity : BaseActivity(R.layout.activity_image_details) {

    private var binding: ActivityImageDetailsBinding? = null

    override fun init() {
        binding = ActivityImageDetailsBinding.bind(findViewById(R.id.rootImg))
        val image = intent.getStringExtra(Constants_Api.INTENT.INTENT_IMG)
        if (image.isNullOrBlank() || image.isImage.not()) {
            Toast.makeText(
                applicationContext,
                getString(R.string.image_not_accurate),
                Toast.LENGTH_SHORT
            ).show()
            binding?.loadProgress?.hide()
            binding?.ivDetails?.setImage(ImageSource.bitmap(BitmapUtils.modifyImgOrientation(image!!)) )
        } else {
            if (image.startsWith("http")) {

                Observable.just(image)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        binding?.loadProgress?.show()
                    }
                    .filter { it.isNullOrBlank().not() }
                    .map { BitmapUtils.getBitmapFromURL(it) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally {
                        binding?.loadProgress?.hide()
                    }
                    .subscribe {
                        it?.let { it1 -> ImageSource.bitmap(it1) }?.let { it2 ->
                            binding?.ivDetails?.setImage(
                                it2
                            )
                        }

                    }
            }
            else{
                binding?.loadProgress?.hide()
                binding?.ivDetails?.setImage(ImageSource.bitmap(BitmapUtils.modifyImgOrientation(image)) )

            }
        }

    }






    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private val String.isImage: Boolean
        get() {
            return when {
                endsWith(".png") -> true
                endsWith(".jpg") -> true
                endsWith(".jpeg") -> true
                endsWith(".svg") -> true
                endsWith(".gif") -> true
                else -> false

            }

        }
}
