package com.platCourse.platCourseAndroid.menu.notifications.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemNotificationBinding
import com.rowaad.app.data.model.notification_model.NotificationItem
import com.rowaad.utils.extention.convertDate
import com.rowaad.utils.extention.tint
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.NotificationsVH>() {

     var data: MutableList<NotificationItem> = ArrayList()

    var onClickItem: ((NotificationItem, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int,item:NotificationItem) {
        selectedItemPosition = position
        data[position]=item
        notifyItemChanged(position)
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
        val oldPosition=data.size
        this.data.addAll(data as MutableList<NotificationItem>)
        val newItemCount=data.size
        notifyItemRangeChanged(oldPosition,newItemCount)
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }
    fun seeAllNotifications(){
        data.map { it.read=true }
        notifyItemRangeChanged(0,data.size)
    }

    inner class NotificationsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:NotificationItem) = with(ItemNotificationBinding.bind(itemView)) {
            tvTitle.text=item.notification?.name
            tvDesc.text=item.notification?.body
            tvCreated.text=item.createdAt?.convertDate()
            //item.read = selectedItemPosition==bindingAdapterPosition
            if (item.read)
                root.tint(R.color.colorOnMyBackground)
            else
                root.tint(R.color.color_not_selected)


            itemView.onClick {
                onClickItem?.invoke(item,bindingAdapterPosition)
            }
        }
    }
}