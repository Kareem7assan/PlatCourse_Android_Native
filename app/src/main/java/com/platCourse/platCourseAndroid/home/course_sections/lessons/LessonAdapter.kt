package com.platCourse.platCourseAndroid.home.course_sections.lessons

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemLessonMediaBinding
import com.rowaad.app.data.model.lessons.VideoModel
import com.rowaad.utils.extention.loadImage
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class LessonAdapter : RecyclerView.Adapter<LessonAdapter.LessonVH>() {

    private var data: MutableList<VideoModel> = ArrayList()

    var onClickItemVideo: ((VideoModel, Int) -> Unit)? = null
    var onClickItemLink: ((VideoModel, Int) -> Unit)? = null
    var onClickItemDoc: ((VideoModel, Int) -> Unit)? = null

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

    override fun onBindViewHolder(holder: LessonVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<VideoModel>) {
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

    fun checkItemType(item:VideoModel):ItemTypeEnum{
        return when{
            item.video_link!=null || item.video_file!=null -> ItemTypeEnum.VIDEO
            item.file!=null -> ItemTypeEnum.FILE
            else -> ItemTypeEnum.UNKNOWN
        }
    }

   inner class LessonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:VideoModel) = with(ItemLessonMediaBinding.bind(itemView)) {

            tvTitle.text = item.videoName

            when(checkItemType(item)){
                ItemTypeEnum.VIDEO ->{
                    //update Mime Icon
                    ivMime.loadImage(R.drawable.ic_baseline_play_circle_outline_24)

                    // add click listener
                    ivMime.onClick {
                        if (item.video_file!=null)
                            onClickItemVideo?.invoke(item,bindingAdapterPosition)
                        else
                            onClickItemLink?.invoke(item,bindingAdapterPosition)
                    }

                }
                ItemTypeEnum.FILE ->{
                    //update Mime Icon
                    ivMime.loadImage(R.drawable.document)

                    // add click listener
                    ivMime.onClick {
                        onClickItemDoc?.invoke(item,bindingAdapterPosition)
                    }
                }
                ItemTypeEnum.UNKNOWN ->{
                    //Do nothing
                }
            }
        }
    }
}