package com.platCourse.platCourseAndroid.home.categories.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemSubCatBinding
import com.rowaad.app.data.model.categories_model.CategoriesItem
import com.rowaad.app.data.model.categories_model.SubCategory
import com.rowaad.utils.extention.isNullOrEmptyTrue
import java.util.*

class SubCatsAdapter : RecyclerView.Adapter<SubCatsAdapter.CatsVH>() {

    private var data: MutableList<SubCategory> = ArrayList()

    var onClickItem: ((SubCategory, Int) -> Unit)? = null

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

    fun swapData(data: List<SubCategory>) {
        this.data = data as MutableList<SubCategory>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class CatsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:SubCategory) = with(ItemSubCatBinding.bind(itemView)) {
            tvSubCat.text=item.sub_category_name
            itemView.setOnClickListener {
                    onClickItem?.invoke(item,adapterPosition)
            }
        }
    }
}