package com.platCourse.platCourseAndroid.home.course_sections.lessons

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemLessonMediaBinding
import com.rowaad.app.data.model.lessons.VideoModel
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.invisible
import com.rowaad.utils.extention.loadImage
import com.rowaad.utils.extention.show
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class LessonAdapter : RecyclerView.Adapter<LessonAdapter.LessonVH>() {

    private var data: MutableList<VideoModel> = ArrayList()
    private var lessonId:Int? = null
    var onClickItemVideo: ((VideoModel, Int,LessonId:Int?) -> Unit)? = null
    var onClickItemLink: ((VideoModel, Int) -> Unit)? = null
    var onClickItemYoutube: ((VideoModel, Int) -> Unit)? = null
    var onClickItemDoc: ((VideoModel, Int) -> Unit)? = null
    var onClickItemAssign: ((VideoModel, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonVH {
        return LessonVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_lesson_media, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LessonVH, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(lessonId:Int,data: List<VideoModel>) {
        this.lessonId=lessonId
        val oldSize=this.data.size
        this.data = data as MutableList<VideoModel>
        val newItemCount=this.data.size
        notifyItemRangeChanged(oldSize,newItemCount)
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }


   inner class LessonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:VideoModel) = with(ItemLessonMediaBinding.bind(itemView)) {

            if (bindingAdapterPosition==data.lastIndex && item.file==null){
                    view5.invisible()
            }
            else{
                    view5.show()
            }
            //videos
                if (item.video_link!=null || item.video_file!=null || item.video_id!=null) {
                    tvTitle.text = item.videoName
                    grpVideo.isVisible=true
                    // add click listener
                    ivMime.onClick {
                        when {
                            item.video_file!=null -> onClickItemVideo?.invoke(item,bindingAdapterPosition,lessonId)
                            item.video_id != null -> onClickItemYoutube?.invoke(item,bindingAdapterPosition)
                            else -> onClickItemLink?.invoke(item,bindingAdapterPosition)
                        }
                    }
                    tvTitle.onClick {
                        when {
                            item.video_file!=null -> onClickItemVideo?.invoke(item,bindingAdapterPosition,lessonId)
                            item.video_id != null -> onClickItemYoutube?.invoke(item,bindingAdapterPosition)
                            else -> onClickItemLink?.invoke(item,bindingAdapterPosition)
                        }
                    }

                }
            //files
            if (item.file!=null){
                    tvTitleDoc.text = item.videoName
                    //update Mime Icon
                    grpDoc.isVisible=true

                    // add click listener
                    ivDoc.onClick {
                        onClickItemDoc?.invoke(item,bindingAdapterPosition)
                    }
                // add click listener
                    tvTitleDoc.onClick {
                        onClickItemDoc?.invoke(item,bindingAdapterPosition)
                    }
                }
            else{
                grpDoc.isVisible=false
            }
            //quizzes
            if (item.quizzes?.size ?: 0 > 0){
                    tvTitleAssign.text = item.quizzes?.first()?.quiz?.quiz_title
                    //update Mime Icon
                    grpAssign.isVisible=true

                    // add click listener
                    ivAssign.onClick {
                        onClickItemAssign?.invoke(item,bindingAdapterPosition)
                    }
                // add click listener
                    tvTitleAssign.onClick {
                        onClickItemAssign?.invoke(item,bindingAdapterPosition)
                    }
                }
            else{
                grpAssign.isVisible=false
            }



        }
    }
}