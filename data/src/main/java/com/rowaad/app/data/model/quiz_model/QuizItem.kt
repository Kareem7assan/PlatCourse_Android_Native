package com.rowaad.app.data.model.quiz_model

data class QuizModel (val id:Int,val quiz:QuizItem?=null,
                      val questions:List<QuestionItem>?=null,
                      val solved:Boolean?=false,
                      val passed:Boolean?=false,
                      //another key return with solving the quiz because of brilliance of backend
                      val is_passed:Boolean?=false,
                      val score:Float?=0f,
                      val quiz_questions:List<QuizQuestion>?=null
)

data class QuizItem(
        val id:Int,
        val course:Int,
        //val lesson:LessonsModel?=null,
        val quiz_title:String?=null,
        val time:Float?=null,
        val questions:List<QuestionItem>?=null,
        val solved:Boolean?=false,
        val passed:Boolean?=false,
        val is_passed:Boolean?=false,
        val score:Float?=0f,
        val quiz_questions:List<QuizQuestion>?=null

        )

data class QuestionItem(val id:Int,
                        val question:String?=null,
                        val image:String?=null,
                        val right_answer:String?=null,
                        var selected_answer:String?=null,
                        val choice_1:String?=null,
                        val choice_2:String?=null,
                        val choice_3:String?=null,
                        val choice_4:String?=null,
                        val choice_5:String?=null
)

data class QuizQuestion(val question_id:Int,
                        val right_answer:String?=null,
                        val student_answer:String?=null
)

data class AnswersModel(val questions:List<PostAnswers>)
data class PostAnswers(val id:Int,val answer:String?=null)

