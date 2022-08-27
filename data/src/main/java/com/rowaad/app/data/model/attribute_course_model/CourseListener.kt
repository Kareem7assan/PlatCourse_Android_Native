package com.rowaad.app.data.model.attribute_course_model

import com.rowaad.app.data.model.courses_model.CourseItem

interface CourseListener {
    fun onClickLessons()
    fun onClickQuiz()
    fun onClickDisc()
    fun onClickRates()
    fun onClickFiles()
    fun onClickAds()
}