package com.platCourse.platCourseAndroid.home.course_sections.files

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemFileBinding
import com.rowaad.app.data.model.files.File
import com.rowaad.app.data.model.files.FilesModel
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

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class FileVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:File) = with(ItemFileBinding.bind(itemView)) {
            tvTitle.text=item.file_name
            tvDate.text=item.created_at
            downloadBtn.setOnClickListener {
                onClickItem?.invoke(item,bindingAdapterPosition)
            }
        }
    }
}