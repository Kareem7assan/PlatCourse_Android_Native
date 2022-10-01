package com.platCourse.platCourseAndroid.home.course_sections.lessons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.platCourse.platCourseAndroid.databinding.ItemSectionBinding
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.lessons.LessonsResponse
import com.rowaad.app.data.model.lessons.VideoModel

class SectionAdapter:RecyclerView.Adapter<SectionAdapter.SectionHolder>() {

    private var data = mutableListOf<LessonsResponse>()

    var onClickItem: ((LessonsModel, Int) -> Unit)? = null

    var onClickItemVideo: ((VideoModel, Int, LessonId:Int?) -> Unit)? = null
    var onClickItemLink: ((VideoModel, Int) -> Unit)? = null
    var onClickItemDoc: ((VideoModel, Int) -> Unit)? = null

    var onDropDownClicked:((parentPosition: Int,childPosition: Int) -> Unit)? = null

   inner class SectionHolder(private val binding:ItemSectionBinding):RecyclerView.ViewHolder(binding.root){
        private val adapter by lazy {
            LessonTitleAdapter()
        }

        init {

            binding.rvLessons.adapter=adapter

            adapter.onClickItemVideo= ::onClickVideo
            adapter.onClickItemLink= ::onClickLink
            adapter.onClickItemDoc= ::onClickDoc
            adapter.onDropDownClicked = { expanded , position ->
               adapter.updateExpandStatus(expanded,position)
            }

        }

        fun bind(item:LessonsResponse)= with(binding){
            tvTitle.text=item.title
            tvDesc.text=item.description
            adapter.swapData(item.lessons)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionHolder {
        return SectionHolder(ItemSectionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SectionHolder, position: Int) {
        holder.bind(data[position])
    }
    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }
    fun swapData(data: List<LessonsResponse>) {
        val oldSize=this.data.size
        this.data = data as MutableList<LessonsResponse>
        val newItemCount=this.data.size
        notifyItemRangeChanged(oldSize,newItemCount)
    }
    override fun getItemCount()=data.size

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