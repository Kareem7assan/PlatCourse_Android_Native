package com.platCourse.platCourseAndroid.home.course_sections.rates.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemReviewBinding
import com.rowaad.app.data.model.reviews.Review
import com.rowaad.utils.extention.convertDate
import com.rowaad.utils.extention.formatRate
import java.util.*

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewVH>() {

    private var data: MutableList<Review> = ArrayList()

    var onClickItem: ((Review, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewVH {
        return ReviewVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_review, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ReviewVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<Review>) {
        this.data = data.reversed() as MutableList<Review>
        notifyDataSetChanged()
    }
    fun addReview(review: Review) {
        this.data.add(0,review)
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    class ReviewVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:Review) = with(ItemReviewBinding.bind(itemView)) {
            tvName.text=item.reviewer_name
            tvComment.text=item.description
            rateView.rating=item.rate ?: 1f
            tvRate.formatRate(item.rate ?: 1f)
            tvCreated.text=item.created_at?.convertDate()
        }
    }
}