package com.platCourse.platCourseAndroid.home.course_sections.lessons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemLessonBinding
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.lessons.VideoModel
import java.util.*

class LessonTitleAdapter : RecyclerView.Adapter<LessonTitleAdapter.LessonTitleVH>() {

    private var data: MutableList<LessonsModel> = ArrayList()

    var onClickItem: ((LessonsModel, Int) -> Unit)? = null

    var onClickItemVideo: ((VideoModel, Int) -> Unit)? = null
    var onClickItemLink: ((VideoModel, Int) -> Unit)? = null
    var onClickItemDoc: ((VideoModel, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonTitleVH {
        return LessonTitleVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_lesson, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LessonTitleVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<LessonsModel>) {
        this.data = data as MutableList<LessonsModel>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

   inner class LessonTitleVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val adapter by lazy {
            LessonAdapter()
        }
        fun bind(item:LessonsModel) = with(ItemLessonBinding.bind(itemView)) {
            tvSection.text=item.lesson_no.toString()+" "+  if (item.section?.title!=null) item.section?.title else ""
            rvLessons.layoutManager=LinearLayoutManager(itemView.context)
            rvLessons.adapter=adapter

            adapter.swapData(item.videos!!.map {
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
    private fun onClickVideo(videoModel: VideoModel, i: Int) {
        onClickItemVideo?.invoke(videoModel,i)
    }
}