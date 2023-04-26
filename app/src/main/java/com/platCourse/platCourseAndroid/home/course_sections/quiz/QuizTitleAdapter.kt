package com.platCourse.platCourseAndroid.home.course_sections.quiz

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemQuizBinding
import com.rowaad.app.data.model.quiz_model.QuizItem
import com.rowaad.app.data.model.quiz_model.QuizModel
import com.rowaad.utils.extention.hide
import com.rowaad.utils.extention.show
import java.util.*
import kotlin.math.roundToInt

class QuizTitleAdapter : RecyclerView.Adapter<QuizTitleAdapter.QuizVH>() {

    private var data: MutableList<QuizModel> = ArrayList()

    var onClickItem: ((QuizModel, Int) -> Unit)? = null
    var onClickItemDisabled: ((QuizModel, Int) -> Unit)? = null

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
        this.data = data as MutableList<QuizModel>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class QuizVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item:QuizModel) = with(ItemQuizBinding.bind(itemView)) {
            tvQuiz.text=item.quiz?.quiz_title
            Log.e("item",item.passed.toString()+","+item.solved)
            tvQuestions.text=String.format(itemView.context.getString(R.string.questions_num),item.quiz?.questions?.size)
            if (bindingAdapterPosition==0){
                when{
                    //when quiz passed
                    item.quiz?.solved==true && item.quiz?.passed==true->{
                        //green color & show grade
                        tvAnswers.show()
                        root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#72e65e")))
                            .also { tvAnswers.text= String.format(itemView.context.getString(R.string.result_mark),item.quiz?.score?.roundToInt())+"/"+item.quiz?.questions?.size.toString() }
                    }
                    //when quiz solved but not passed
                    //orange color
                    item.quiz?.solved==true && item.quiz?.passed!!.not()->{
                        tvAnswers.show()
                        root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#e39376")))
                            .also { tvAnswers.text= String.format(itemView.context.getString(R.string.result_mark),item.quiz?.score?.roundToInt())+"/"+item.quiz?.questions?.size.toString()  }
                    }
                    //when quiz not solved
                    //white color
                    (item.quiz?.solved==false && item.quiz?.passed==false)  ->{
                        tvAnswers.hide()
                        root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")))
                    }
                }

            }
            else{
                when{
                    //when quiz disabled
                     data[bindingAdapterPosition-1].quiz?.passed?.not()==true->{
                         //disable grey
                         if (item.quiz?.passed?.not()==true && item.quiz?.solved?.not()==true) {
                             tvAnswers.hide()
                             root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#a3a39d")))
                         }
                         else if (item.quiz?.solved==true && item.quiz?.passed==true){
                             //green color & show grade
                             tvAnswers.show()
                             root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#72e65e")))
                                 .also { tvAnswers.text= String.format(itemView.context.getString(R.string.result_mark),item.quiz?.score?.roundToInt())+"/"+item.quiz?.questions?.size.toString() }
                         }
                         else{
                             //orange
                             tvAnswers.show()
                             root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#e39376")))
                                 .also { tvAnswers.text= String.format(itemView.context.getString(R.string.result_mark),item.quiz?.score?.roundToInt())+"/"+item.quiz?.questions?.size.toString()  }
                         }
                    }
                    data[bindingAdapterPosition-1].quiz?.passed == true->{
                        //green
                        if (item.quiz?.solved==true && item.quiz?.passed==true){
                            tvAnswers.show()
                            root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#72e65e")))
                                .also { tvAnswers.text= String.format(itemView.context.getString(R.string.result_mark),item.quiz?.score?.roundToInt())+"/"+item.quiz?.questions?.size.toString() }
                        }
                        //orange
                        else if (item.quiz?.solved==true && item.quiz?.passed!!.not()){
                            tvAnswers.show()
                            root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#e39376")))
                                .also { tvAnswers.text= String.format(itemView.context.getString(R.string.result_mark),item.quiz?.score?.roundToInt())+"/"+item.quiz?.questions?.size.toString()  }
                        }
                        //white
                        else{
                            tvAnswers.hide()
                            root.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#ffffff")))
                                .also { tvAnswers.text= String.format(itemView.context.getString(R.string.result_mark),item.quiz?.score?.roundToInt())+"/"+item.quiz?.questions?.size.toString()  }

                        }

                    }



                }
            }




            root.setOnClickListener {
                if (bindingAdapterPosition > 0 && data[bindingAdapterPosition-1].quiz?.passed==true) onClickItem?.invoke(item,bindingAdapterPosition)
                else if (bindingAdapterPosition == 0 ) onClickItem?.invoke(item,bindingAdapterPosition)
                else onClickItemDisabled?.invoke(item,bindingAdapterPosition)
            }
        }
    }
}