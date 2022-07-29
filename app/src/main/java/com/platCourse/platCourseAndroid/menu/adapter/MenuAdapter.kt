package com.platCourse.platCourseAndroid.menu.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rowaad.app.data.model.Menu
import com.rowaad.app.data.model.MenuModel
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemMenuBinding
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import java.util.*

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.MenuVH>() {

    private var data: MutableList<MenuModel> = ArrayList()

    var onClickItem: ((Menu) -> Unit)? = null
    var onClickItemNight: ((Boolean) -> Unit)? = null

    private var isDark=false
    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    fun enableDarkMode(isDark:Boolean){
        this.isDark=isDark
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuVH {
        return MenuVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_menu, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MenuVH, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(data: List<MenuModel>) {
        this.data = data as MutableList<MenuModel>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

   inner class MenuVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:MenuModel) = with(ItemMenuBinding.bind(itemView)) {
            ivLogo.setImageResource(item.resImg)
            tvItem.setText(item.name)
            if (item.menuItem==Menu.NIGHT)
                rbCheck.show().also { rbCheck.isChecked=isDark }.also { ivAction.hide() }
            else
                rbCheck.hide().also { ivAction.show() }

            rbCheck.setOnClickListener {
                isDark=isDark.not()
                if (isDark)
                    onClickItemNight?.invoke(isDark)
            }

             itemView.setOnClickListener { onClickItem?.invoke(item.menuItem) }
        }
    }
}