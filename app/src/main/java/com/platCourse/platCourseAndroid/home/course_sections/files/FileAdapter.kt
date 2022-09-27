package com.platCourse.platCourseAndroid.home.course_sections.files

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemFileBinding
import com.rowaad.app.data.model.files.File
import com.rowaad.app.data.model.files.FilesModel
import com.rowaad.utils.extention.convertDate
import com.rowaad.utils.extention.isArabic
import com.rowaad.utils.extention.isEnglish
import java.util.*

class FileAdapter : RecyclerView.Adapter<FileAdapter.FileVH>() {

    private var data: MutableList<File> = ArrayList()

    var onClickItem: ((File, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileVH {
        return FileVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_file, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FileVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<File>) {
        this.data = data as MutableList<File>
        notifyDataSetChanged()
    }

    fun addData(data: List<File>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clear(){
        data.clear()
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class FileVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:File) = with(ItemFileBinding.bind(itemView)) {
            if (item.file_name?.isEnglish()==true) tvTitle.append(bindingAdapterPosition.plus(1).toString()+"-"+item.file_name)
            else tvTitle.append(item.file_name+"-"+bindingAdapterPosition.plus(1).toString())
            tvDate.text=item.created_at?.convertDate()
            if (item.downloadable==true)
                downloadBtn.text=itemView.context.getString(R.string.download)
            else
                downloadBtn.text=itemView.context.getString(R.string.preview)

            downloadBtn.setOnClickListener {
                onClickItem?.invoke(item,bindingAdapterPosition)
            }
        }
    }
}