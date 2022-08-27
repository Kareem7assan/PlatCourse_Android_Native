package com.rowaad.app.data.model.quiz_model

import com.rowaad.app.data.model.lessons.LessonsModel

data class QuizModel (val id:Int,val quiz:QuizItem?=null)
data class QuizItem(
        val id:Int,
        val course:Int,
        val lesson:LessonsModel?=null,
        val quiz_title:String?=null,
        val time:Any?=null,
        val questions:List<QuestionItem>?=null,
        val solved:Boolean?=false,
        val passed:Boolean?=false,
        val score:Float?=0f,
        val quiz_questions:List<Any>?=null

        )

data class QuestionItem(val id:Int,val question:String?=null,val right_answer:String?=null,
                        val choice_1:String?=null,
                        val choice_2:String?=null,
                        val choice_3:String?=null,
                        val choice_4:String?=null,
                        val choice_5:String?=null
)

