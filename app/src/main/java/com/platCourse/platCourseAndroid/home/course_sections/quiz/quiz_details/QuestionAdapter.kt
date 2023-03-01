package com.platCourse.platCourseAndroid.home.course_sections.quiz.quiz_details

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemQuestionBinding
import com.platCourse.platCourseAndroid.databinding.ItemQuizBinding
import com.rowaad.app.data.model.quiz_model.QuestionItem
import com.rowaad.app.data.model.quiz_model.QuizItem
import com.rowaad.app.data.model.quiz_model.QuizQuestion
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.loadImage
import com.rowaad.utils.extention.show
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import java.util.*

class QuestionAdapter : RecyclerView.Adapter<QuestionAdapter.QuestionVH>() {

      var studentAnswers: MutableList<QuizQuestion> = mutableListOf()
    private var solved: Boolean = false
    private var passed: Boolean = false
      var quizQuestions: MutableList<QuestionItem> = mutableListOf()
    //private var data: MutableList<QuizItem> = ArrayList()

    var onClickItem: ((QuizItem, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionVH {
        return QuestionVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_question, parent, false)
        )
    }

    override fun getItemCount() = quizQuestions.size

    override fun onBindViewHolder(holder: QuestionVH, position: Int) {
        holder.bind(quizQuestions[position])
    }

    fun swapData(quizQuestions: MutableList<QuestionItem>, studentAnswers:MutableList<QuizQuestion>,solved:Boolean,passed:Boolean) {
        this.quizQuestions = quizQuestions
        this.studentAnswers= studentAnswers
        this.solved=solved
        this.passed=passed

        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        quizQuestions.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

   inner class QuestionVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:QuestionItem) = with(ItemQuestionBinding.bind(itemView)) {
            if (item.image.isNullOrBlank())
                ivQues.hide().also { tvQuestion.text=item.question }
            else
                ivQues.loadImage(item.image).also { if (item.question.isNullOrBlank()) tvQuestion.hide() else tvQuestion.show().also { tvQuestion.text=item.question }}

            rbAns1.setBackgroundResource(R.drawable.bg_stroke_normal_question)
            rbAns2.setBackgroundResource(R.drawable.bg_stroke_normal_question)
            rbAns3.setBackgroundResource(R.drawable.bg_stroke_normal_question)
            rbAns4.setBackgroundResource(R.drawable.bg_stroke_normal_question)
            rbAns5.setBackgroundResource(R.drawable.bg_stroke_normal_question)

            if (solved ){
                if (passed) {
                    if (studentAnswers.isNotEmpty()) {
                        when (studentAnswers[bindingAdapterPosition].student_answer) {
                            item.choice_1 -> rbAns1.setBackgroundResource(R.drawable.bg_stroke_error_question)
                            item.choice_2 -> rbAns2.setBackgroundResource(R.drawable.bg_stroke_error_question)
                            item.choice_3 -> rbAns3.setBackgroundResource(R.drawable.bg_stroke_error_question)
                            item.choice_4 -> rbAns4.setBackgroundResource(R.drawable.bg_stroke_error_question)
                            item.choice_5 -> rbAns5.setBackgroundResource(R.drawable.bg_stroke_error_question)
                        }
                        when (studentAnswers[bindingAdapterPosition].right_answer) {
                            item.choice_1 -> rbAns1.setBackgroundResource(R.drawable.bg_stroke_correct_question)
                            item.choice_2 -> rbAns2.setBackgroundResource(R.drawable.bg_stroke_correct_question)
                            item.choice_3 -> rbAns3.setBackgroundResource(R.drawable.bg_stroke_correct_question)
                            item.choice_4 -> rbAns4.setBackgroundResource(R.drawable.bg_stroke_correct_question)
                            item.choice_5 -> rbAns5.setBackgroundResource(R.drawable.bg_stroke_correct_question)
                        }
                    }
                }
            }
            else{
                when(item.selected_answer){
                    item.choice_1->rbAns1.setBackgroundResource(R.drawable.bg_stroke_correct_question)
                    item.choice_2->rbAns2.setBackgroundResource(R.drawable.bg_stroke_correct_question)
                    item.choice_3->rbAns3.setBackgroundResource(R.drawable.bg_stroke_correct_question)
                    item.choice_4->rbAns4.setBackgroundResource(R.drawable.bg_stroke_correct_question)
                    item.choice_5->rbAns5.setBackgroundResource(R.drawable.bg_stroke_correct_question)
                }
            }

            rbAns1.text = item.choice_1
            rbAns2.text = item.choice_2
            if (item.choice_3.isNullOrBlank()) rbAns3.hide() else rbAns3.show().also { rbAns3.text = item.choice_3 }
            if (item.choice_4.isNullOrBlank()) rbAns4.hide() else rbAns4.show().also { rbAns4.text = item.choice_4 }
            if (item.choice_5.isNullOrBlank()) rbAns5.hide() else rbAns5.show().also { rbAns5.text = item.choice_5 }

            rbAns1.onCheckedChange { buttonView, isChecked ->
                if (isChecked) item.selected_answer=item.choice_1
            }
            rbAns2.onCheckedChange { buttonView, isChecked ->
                if (isChecked) item.selected_answer=item.choice_2
            }
            rbAns3.onCheckedChange { buttonView, isChecked ->
                if (isChecked) item.selected_answer=item.choice_3
            }
            rbAns4.onCheckedChange { buttonView, isChecked ->
                if (isChecked) item.selected_answer=item.choice_4
            }
            rbAns5.onCheckedChange { buttonView, isChecked ->
                if (isChecked) item.selected_answer=item.choice_5
            }
        }
    }
}
