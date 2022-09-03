package com.platCourse.platCourseAndroid.home.course_sections.discussions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemDiscussionsBinding
import com.rowaad.app.data.model.UserModel
import com.rowaad.app.data.model.discussions_model.Comment
import com.rowaad.app.data.model.discussions_model.DiscussionModel
import com.rowaad.utils.extention.clearTxt
import com.rowaad.utils.extention.getContent
import java.util.*

class DiscussionAdapter(val user: UserModel?) : RecyclerView.Adapter<DiscussionAdapter.DiscussionVH>() {

    private var data: MutableList<DiscussionModel> = ArrayList()

    var onClickItemComment: ((DiscussionModel,String, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionVH {
        return DiscussionVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_discussions, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DiscussionVH, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(data: List<DiscussionModel>) {
        this.data = data as MutableList<DiscussionModel>
        notifyDataSetChanged()
    }

    fun insertItem(index: Int,discModel:DiscussionModel) {
        data.add(discModel)
        notifyItemInserted(index)

    }
    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class DiscussionVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val adapter by lazy {
            CommentAdapter()
        }
        fun bind(item: DiscussionModel) = with(ItemDiscussionsBinding.bind(itemView)) {
            tvCreator.text=item.owner_name
            tvDate.text=item.created_at
            tvTitle.text=item.title
            rvComments.layoutManager=LinearLayoutManager(itemView.context)
            rvComments.adapter=adapter
            item.comments?.let { adapter.swapData(it) }
            addCommentBtn.setOnClickListener {
                if (etComment.getContent().isNotBlank()) {
                    onClickItemComment?.invoke(item, etComment.getContent(), bindingAdapterPosition).also {
                        adapter.insertItem(item.comments?.size
                                ?: 0, Comment(id = 0, owner_name = user?.name ?: "kareem", comment = etComment.getContent(), created_at = "الآن"))
                    }.also {
                        etComment.clearTxt()
                    }
                }
                else{
                    Toast.makeText(itemView.context, itemView.context.getString(R.string.comment_required), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}