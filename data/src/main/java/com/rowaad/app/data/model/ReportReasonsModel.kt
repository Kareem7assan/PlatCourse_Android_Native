package com.rowaad.app.data.model

import com.rowaad.app.data.model.tweets_model.City

object Report{
        val reasons:List<City> = listOf(
                City(title = "خطأ في السعر / الصورة / الفئة",isChecked = false),
                City(title ="إعلان غير لائق",isChecked = false),
                City(title ="تم بيع السلعة",isChecked = false),
                City(title ="تم بيع السلعة",isChecked = false),
                City(title ="نصب / واحتيال",isChecked = false),
                City(title ="بائع غير لائق",isChecked = false),
                City(title ="سبب آخر",isChecked = false)
        )
}
