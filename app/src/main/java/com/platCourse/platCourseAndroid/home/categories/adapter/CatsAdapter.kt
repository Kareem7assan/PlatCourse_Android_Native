package com.platCourse.platCourseAndroid.home.categories.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemSubCatBinding
import com.rowaad.app.data.model.categories_model.CategoriesItem
import com.rowaad.utils.extention.isNullOrEmptyTrue
import java.util.*

class CatsAdapter : RecyclerView.Adapter<CatsAdapter.CatsVH>() {

    private var data: MutableList<CategoriesItem> = ArrayList()

    var onClickItem: ((CategoriesItem, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatsVH {
        return CatsVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_sub_cat, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CatsVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<CategoriesItem>) {
        this.data = data as MutableList<CategoriesItem>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class CatsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:CategoriesItem) = with(ItemSubCatBinding.bind(itemView)) {
            tvSubCat.text=item.category_name
            itemView.setOnClickListener {
                if (item.sub_category?.isNullOrEmptyTrue()!!.not())
                    onClickItem?.invoke(item,adapterPosition)
            }
        }
    }
}