package com.platCourse.platCourseAndroid.home.course_sections.lessons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemLessonBinding
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.lessons.VideoModel
import com.rowaad.utils.extention.loadImage
import java.util.*

class LessonTitleAdapter : RecyclerView.Adapter<LessonTitleAdapter.LessonTitleVH>() {

    private var data: MutableList<LessonsModel> = ArrayList()

    var onClickItem: ((LessonsModel, Int) -> Unit)? = null

    var onClickItemVideo: ((VideoModel, Int,LessonId:Int?) -> Unit)? = null
    var onClickItemLink: ((VideoModel, Int) -> Unit)? = null
    var onClickItemDoc: ((VideoModel, Int) -> Unit)? = null

    var onDropDownClicked:((isExpanded:Boolean,position:Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    fun updateExpandStatus(isExpanded: Boolean,position: Int){
        data[position].isExpanded=isExpanded
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonTitleVH {
        return LessonTitleVH(
                ItemLessonBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LessonTitleVH, position: Int){
        holder.bind(data[position])

        holder.binding.rvLessons.isVisible= data[position].isExpanded
    }

    fun swapData(data: List<LessonsModel>) {
        val oldSize=this.data.size
        this.data = data as MutableList<LessonsModel>
        val newItemCount=this.data.size
        notifyItemRangeChanged(oldSize,newItemCount)
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

   inner class LessonTitleVH( val binding:ItemLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        private val adapter by lazy {
            LessonAdapter()
        }

       init {
           binding.ivDropDown.setOnClickListener {
               onDropDownClicked?.invoke(data[bindingAdapterPosition].isExpanded.not(),bindingAdapterPosition)
           }
       }
        fun bind(item:LessonsModel) = with(binding) {
            tvSection.text="${item.lesson_no}. ${item.title}"
            rvLessons.layoutManager=LinearLayoutManager(itemView.context)
            rvLessons.adapter=adapter

            ivDropDown.loadImage(if (selectedItemPosition==bindingAdapterPosition) R.drawable.ic_arrow_up_24 else R.drawable.ic_arrow_down_24)

            adapter.swapData(lessonId = item.id,data = item.videos!!.map {
                it.videoName=item.title
                it.file=item.file
                it
            })

            adapter.onClickItemVideo=::onClickVideo
            adapter.onClickItemLink=::onClickLink
            adapter.onClickItemDoc=::onClickDoc
        }


    }

    private fun onClickDoc(videoModel: VideoModel, i: Int) {
        onClickItemDoc?.invoke(videoModel,i)
    }
    private fun onClickLink(videoModel: VideoModel, i: Int) {
        onClickItemLink?.invoke(videoModel,i)
    }
    private fun onClickVideo(videoModel: VideoModel, i: Int,lessonId:Int?) {
        onClickItemVideo?.invoke(videoModel,i,lessonId)
    }
}