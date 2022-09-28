package com.platCourse.platCourseAndroid.home.course_sections.lessons

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemLessonMediaBinding
import com.rowaad.app.data.model.lessons.VideoModel
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.invisible
import com.rowaad.utils.extention.show
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class LessonAdapter : RecyclerView.Adapter<LessonAdapter.LessonVH>() {

    private var data: MutableList<VideoModel> = ArrayList()
    private var lessonId:Int? = null
    var onClickItemVideo: ((VideoModel, Int,LessonId:Int?) -> Unit)? = null
    var onClickItemLink: ((VideoModel, Int) -> Unit)? = null
    var onClickItemDoc: ((VideoModel, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonVH {
        return LessonVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_lesson_media, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LessonVH, position: Int) = holder.bind(data[position])

    fun swapData(lessonId:Int,data: List<VideoModel>) {
        this.lessonId=lessonId
        this.data = data as MutableList<VideoModel>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

   inner class LessonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:VideoModel) = with(ItemLessonMediaBinding.bind(itemView)) {
            if (item.video_link!=null || item.video_file!=null) {
                videoLay.show()
                tvTitleVideo.text = item.videoName
            }
            else{
                videoLay.invisible()
            }

            if (item.file!=null) {
                docLay.show()
                tvTitleDoc.text = item.videoName
            }
            else{
                docLay.invisible()
                tvTitleVideo.text = item.videoName
            }

            ivPlay.onClick {
                if (item.video_file!=null)
                    onClickItemVideo?.invoke(item,bindingAdapterPosition,lessonId)
                else
                    onClickItemLink?.invoke(item,bindingAdapterPosition)
            }



            ivDoc.onClick {
                onClickItemDoc?.invoke(item,bindingAdapterPosition)
            }

        }
    }
}