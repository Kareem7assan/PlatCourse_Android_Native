package com.platCourse.platCourseAndroid.home.course_details

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemCourseDetailsBinding
import com.rowaad.app.data.model.attribute_course_model.Action
import com.rowaad.app.data.model.attribute_course_model.CourseListener
import com.rowaad.app.data.model.attribute_course_model.ItemCourseDetails
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import java.util.*

class AttributeAdapter(val listener:CourseListener) : RecyclerView.Adapter<AttributeAdapter.AttributeVH>() {

    private var data: MutableList<ItemCourseDetails> = ArrayList()

    var onClickItem: ((ItemCourseDetails, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeVH {
        return AttributeVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_course_details, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AttributeVH, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(data: List<ItemCourseDetails>) {
        this.data = data as MutableList<ItemCourseDetails>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

   inner class AttributeVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:ItemCourseDetails) = with(ItemCourseDetailsBinding.bind(itemView)) {
            tvTitle.text=item.title
            if (item.desc.isNullOrBlank())
                tvDetails.hide()
            else
                tvDetails.show().also {tvDetails.text=item.desc}


            ivAction.isVisible=item.showForward ?: false

            ivAction.setOnClickListener {
                when(item.type){
                    Action.RATES->{
                        listener.onClickRates()
                    }
                    Action.LESSONS->{
                        listener.onClickLessons()
                    }
                    Action.DISCUSSIONS->{
                        listener.onClickDisc()
                    }
                    Action.QUIZES->{
                        listener.onClickQuiz()
                    }
                    Action.FILES->{
                        listener.onClickFiles()
                    }
                    Action.ADS->{
                        listener.onClickAds()
                    }
                }
            }
        }
    }
}