package com.platCourse.platCourseAndroid.home.courses.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemCourseBinding
import com.rowaad.app.data.model.courses_model.CourseItem
import com.rowaad.utils.extention.*
import java.util.*

class CoursesAdapter : RecyclerView.Adapter<CoursesAdapter.CoursesVH>() {

    private var data: MutableList<CourseItem> = ArrayList()

    var onClickItem: ((CourseItem, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesVH {
        return CoursesVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_course, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CoursesVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<CourseItem>) {
        this.data = data as MutableList<CourseItem>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    class CoursesVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:CourseItem) = with(ItemCourseBinding.bind(itemView)) {
            tvTitleCourse.text=item.title
            tvDescCourse.text=item.overview
            tvTeacherName.text=item.ownerName
            rbCourse.rating=item.rate ?: 0f
            if (item.discountPrice==null || item.discountPrice==0f) {
                tvPriceAfter.formatPrice(item.price ?: 0f,"ج.م")
                tvPriceBefore.hide()
            }
            else {
                tvPriceBefore.formatPrice(item.price ?: 0f,"ج.م")
                tvPriceBefore.textWithNegativeFlag()
                tvPriceAfter.formatPrice(item.discountPrice ?: 0f,"ج.م")
                tvPriceBefore.show()

            }


            ivImgCourse.loadImage(item.cover)
        }
    }
}