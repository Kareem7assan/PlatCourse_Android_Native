package com.platCourse.platCourseAndroid

import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertTrue(isValidUserName("كريم حسن"))
        //assertEquals(isValidFormat("3.0.0"),"3.0.0")
    }


    fun isValidFormat(code: String): String {
        return String.format( "%.s.s", code)
    }

    fun isValidUserName(name: String):Boolean = true.takeIf { name.matches("^[\u0600-\u06FFa-zA-Z]{2,20}+\\s[\u0600-\u06FFa-zA-Z].{2,20}".toRegex()) } ?: false

}