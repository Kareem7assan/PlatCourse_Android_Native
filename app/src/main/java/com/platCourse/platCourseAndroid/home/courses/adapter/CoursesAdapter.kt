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

class CoursesAdapter(val isVertical:Boolean?=false) : RecyclerView.Adapter<CoursesAdapter.CoursesVH>() {

    private var data: MutableList<CourseItem> = ArrayList()

    var onClickItem: ((CourseItem, Int) -> Unit)? = null
    var onClickItemProfile: ((CourseItem, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesVH {
        return if (isVertical==true)
            CoursesVH(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_course_vertical, parent, false)
            )
            else CoursesVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_course, parent, false)
        )
    }

    fun clear(){
        data.clear()
        notifyDataSetChanged()
    }
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CoursesVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<CourseItem>) {
        this.data = data as MutableList<CourseItem>
        notifyDataSetChanged()
    }
    fun addData(data: List<CourseItem>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }


    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }



    inner class CoursesVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

            itemView.setOnClickListener {
                onClickItem?.invoke(item,bindingAdapterPosition)
            }

            tvTeacherName.setOnClickListener {
                onClickItemProfile?.invoke(item,bindingAdapterPosition)
            }


            ivImgCourse.loadImage(item.cover)
        }
    }
}