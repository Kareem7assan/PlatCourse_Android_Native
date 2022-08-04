package com.platCourse.platCourseAndroid.menu.notifications.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemNotificationBinding
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.utils.extention.tint
import java.util.*

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.NotificationsVH>() {

    private var data: MutableList<NotificationItem> = ArrayList()

    var onClickItem: ((NotificationItem, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsVH {
        return NotificationsVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: NotificationsVH, position: Int) =
        holder.bind(data[position])

    fun swapData(data: List<NotificationItem>) {
        this.data = data as MutableList<NotificationItem>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }
    fun seeAllNotifications(){
        data.map { it.read=true }
        notifyDataSetChanged()
    }

    inner class NotificationsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:NotificationItem) = with(ItemNotificationBinding.bind(itemView)) {
            tvTitle.text=item.notification?.name
            tvDesc.text=item.notification?.body
            tvCreated.text=item.createdAt
            if (selectedItemPosition==adapterPosition)
                item.read=true
            else
                item.read=item.read

            if (item.read)
                root.tint(R.color.colorOnMyBackground)
            else
                root.tint(R.color.color_not_selected)

        }
    }
}