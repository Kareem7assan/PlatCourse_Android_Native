package com.platCourse.platCourseAndroid.home.course_sections.quiz

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemQuizBinding
import com.platCourse.platCourseAndroid.home.course_sections.lessons.LessonAdapter
import com.rowaad.app.data.model.quiz_model.QuizItem
import com.rowaad.app.data.model.quiz_model.QuizModel
import java.util.*

class QuizTitleAdapter : RecyclerView.Adapter<QuizTitleAdapter.QuizVH>() {

    private var data: MutableList<QuizModel> = ArrayList()

    var onClickItem: ((QuizModel, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizVH {
        return QuizVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_quiz, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: QuizVH, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(data: List<QuizModel>) {
        val oldPosition=data.size
        this.data = data as MutableList<QuizModel>
        val newItemCount=data.size
        notifyItemRangeChanged(oldPosition,newItemCount)
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class QuizVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item:QuizModel) = with(ItemQuizBinding.bind(itemView)) {
            tvQuiz.text=item.quiz?.quiz_title
            tvQuestions.text=String.format(itemView.context.getString(R.string.questions_num),item.quiz?.questions)
            when{
                item.quiz?.solved==true && item.quiz?.passed==true->{
                    //green color & show grade
                    root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#72e65e")))
                            .also { tvAnswers.text= String.format(itemView.context.getString(R.string.result_mark),item.quiz?.score) }
                }
                item.quiz?.solved==true && item.quiz?.passed!!.not()->{
                    root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#e39376")))
                            .also { tvAnswers.text= String.format(itemView.context.getString(R.string.result_mark),item.quiz?.score) }
                }
                item.quiz?.solved!!.not()->{
                    root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")))
                }
            }
            tvQuestions.setOnClickListener {
                onClickItem?.invoke(item,bindingAdapterPosition)
            }
        }
    }
}