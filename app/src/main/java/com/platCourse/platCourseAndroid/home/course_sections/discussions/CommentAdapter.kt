package com.platCourse.platCourseAndroid.home.course_sections.discussions

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemCommentBinding
import com.rowaad.app.data.model.discussions_model.Comment
import com.rowaad.app.data.model.discussions_model.DiscussionModel
import com.rowaad.utils.extention.convertDate
import java.util.*

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentVH>() {

    private var data: MutableList<Comment> = ArrayList()

    var onClickItem: ((Comment, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentVH {
        return CommentVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_comment, parent, false)
        )
    }

    fun insertItem(index: Int,comment: Comment) {
        data.add(comment)
        notifyItemInserted(index)

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CommentVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<Comment>) {
        this.data = data as MutableList<Comment>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    class CommentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:Comment) = with(ItemCommentBinding.bind(itemView)) {
            tvName.text=item.owner_name
            tvComment.text=item.comment
            tvSince.text=item.created_at?.convertDate()

        }
    }
}