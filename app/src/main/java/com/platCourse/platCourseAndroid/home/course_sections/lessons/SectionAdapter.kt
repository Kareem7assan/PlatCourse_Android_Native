package com.platCourse.platCourseAndroid.home.course_sections.lessons

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemSectionBinding
import com.rowaad.app.data.model.lessons.LessonsModel
import com.rowaad.app.data.model.lessons.LessonsResponse
import com.rowaad.app.data.model.lessons.VideoModel
import com.rowaad.utils.extention.loadImage

class SectionAdapter:RecyclerView.Adapter<SectionAdapter.SectionHolder>() {

    private var expandedLessonId: Int? = null
    private var data = mutableListOf<LessonsResponse>()

    var onClickItem: ((LessonsModel, Int) -> Unit)? = null
    var onClickItemFirstQuiz: ((LessonsModel, Int) -> Unit)? = null

    var onClickItemVideo: ((VideoModel, Int, LessonId:Int?) -> Unit)? = null
    var onClickItemLink: ((VideoModel, Int) -> Unit)? = null
    var onClickItemDoc: ((VideoModel, Int) -> Unit)? = null
    var onClickItemAssign: ((VideoModel, Int) -> Unit)? = null
    var onClickItemYoutube: ((VideoModel, Int,LessonId:Int?) -> Unit)? = null

    var onDropDownClicked:((parentPosition: Int) -> Unit)? = null
    var selectedItemPosition = -1

   inner class SectionHolder( val binding:ItemSectionBinding):RecyclerView.ViewHolder(binding.root){
        private val adapter by lazy {
            LessonTitleAdapter()
        }

        init {

            binding.rvLessons.adapter=adapter

            adapter.onClickItemVideo= ::onClickVideo
            adapter.onClickItemLink= ::onClickLink
            adapter.onClickItemAssign= ::onClickAssign
            adapter.onClickItemDoc= ::onClickDoc
            adapter.onClickItemYoutube= ::onClickItemYoutube
            adapter.onClickItemFirstQuiz= ::onClickItemFirstQuiz
            adapter.onDropDownClicked = { expanded , position ->
               adapter.updateExpandStatus(expanded,position)
            }



            binding.ivToggle.setOnClickListener {
                if (bindingAdapterPosition==selectedItemPosition){
                    toggle(-1)
                }else{
                    toggle(bindingAdapterPosition)
                }

            }

        }

        fun bind(item:LessonsResponse)= with(binding){
            tvTitle.text=item.title
            tvDesc.text=item.description
            adapter.swapData(item.lessons)

            ivToggle.loadImage(if (selectedItemPosition==bindingAdapterPosition) R.drawable.ic_arrow_up_24 else R.drawable.ic_arrow_down_24)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionHolder {
        return SectionHolder(ItemSectionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SectionHolder, position: Int) {
        holder.bind(data[position])
        holder.binding.rvLessons.isVisible = position==selectedItemPosition
    }

    fun updateSelectedItem(position: Int,expandedLessonId:Int?=null) {
        selectedItemPosition = position
        this.expandedLessonId=expandedLessonId
        notifyDataSetChanged()
    }
    fun swapData(data: List<LessonsResponse>) {
        val oldSize=this.data.size
        this.data = data as MutableList<LessonsResponse>
        val newItemCount=this.data.size
        notifyItemRangeChanged(oldSize,newItemCount)
    }
    override fun getItemCount()=data.size

    private fun onClickItemYoutube(videoModel: VideoModel, i: Int,lessonId:Int?) {
        onClickItemYoutube?.invoke(videoModel,i,lessonId)
    }
    private fun onClickItemFirstQuiz(lesson: LessonsModel, i: Int) {
        onClickItemFirstQuiz?.invoke(lesson,i)
    }
    private fun onClickDoc(videoModel: VideoModel, i: Int) {
        onClickItemDoc?.invoke(videoModel,i)
    }
    private fun onClickAssign(videoModel: VideoModel, i: Int) {
        onClickItemAssign?.invoke(videoModel,i)
    }
    private fun onClickLink(videoModel: VideoModel, i: Int) {
        onClickItemLink?.invoke(videoModel,i)
    }
    private fun onClickVideo(videoModel: VideoModel, i: Int,lessonId:Int?) {
        onClickItemVideo?.invoke(videoModel,i,lessonId)
    }

    private fun toggle(position: Int){
        onDropDownClicked?.invoke(position)
    }
}