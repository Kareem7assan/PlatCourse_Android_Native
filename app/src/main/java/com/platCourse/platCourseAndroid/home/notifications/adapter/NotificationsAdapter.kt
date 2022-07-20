package com.platCourse.platCourseAndroid.home.notifications.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemNotificationBinding
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.loadImage
import com.rowaad.utils.extention.show
import java.util.*

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.NotificationVH>() {

    private var data: MutableList<NotificationItem> = ArrayList()

    var onClickItem: ((NotificationItem, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH {
        return NotificationVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
        )
    }

    fun clear(){
        data.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: NotificationVH, position: Int) =
        holder.bind(data[position])

    fun swapData(data: List<NotificationItem>) {
        this.data = data as MutableList<NotificationItem>
        notifyDataSetChanged()
    }

    fun addData(data: List<NotificationItem>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class NotificationVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:NotificationItem) = with(ItemNotificationBinding.bind(itemView)) {
            tvNotif.text=item.body
            //ivAvatar.hide().takeIf { item.customer?.image == null } ?: ivAvatar.show().also { ivAvatar.loadImage(item.customer?.image) }
            tvSince.text=item.createdAt?.humanTime
            itemView.setOnClickListener {
                onClickItem?.invoke(item,adapterPosition)
            }
        }
    }
}