package com.platCourse.platCourseAndroid.home.course_details.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kotvertolet.youtubejextractor.models.newModels.AdaptiveFormatsItem
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemQualityBinding
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class QualityAdapter : RecyclerView.Adapter<QualityAdapter.QualityAdapterVH>() {

    private var data: MutableList<AdaptiveFormatsItem> = ArrayList()

    var onClickItem: ((AdaptiveFormatsItem, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QualityAdapterVH {
        return QualityAdapterVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quality, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: QualityAdapterVH, position: Int) =
        holder.bind(data[position])

    fun swapData(data: List<AdaptiveFormatsItem>) {
        Log.e("data",data.map { it.qualityLabel }.toString())

            if (data.filter { it.qualityLabel=="144p" }.size > 1)
                {this.data.add(data.first { it.qualityLabel=="144p" })}
            if (data.filter { it.qualityLabel=="240p" }.size > 1 )
                {this.data.add(data.first { it.qualityLabel=="240p" })}
            if (data.filter { it.qualityLabel=="360p" }.size > 1)
                {this.data.add(data.first { it.qualityLabel=="360p" })}
            if (data.filter { it.qualityLabel=="480p" }.size > 1)
                {this.data.add(data.first { it.qualityLabel=="480p" })}
            if (data.filter { it.qualityLabel=="720p" }.size > 1 )
                {this.data.add(data.first { it.qualityLabel=="720p" })}
            if (data.filter { it.qualityLabel=="1080p" }.size > 1 )
                {this.data.add(data.first { it.qualityLabel=="1080p" })}

        //this.data = data as MutableList<AdaptiveFormatsItem>
            notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class QualityAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:AdaptiveFormatsItem) = with(ItemQualityBinding.bind(itemView)) {
            tvQuality.text=item.qualityLabel
            root.onClick {
                onClickItem?.invoke(item,bindingAdapterPosition)
            }
        }
    }
}