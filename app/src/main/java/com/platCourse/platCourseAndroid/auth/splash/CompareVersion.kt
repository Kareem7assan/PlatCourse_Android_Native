package com.platCourse.platCourseAndroid.auth.splash

import android.util.Log
import java.util.regex.Matcher
import java.util.regex.Pattern

object CompareVersion{
fun isFirmwareNewer(localVersion: String?, webVersion: String?): Boolean {
    val localVer: IntArray = getVersionNumbers(localVersion)
    val webVer: IntArray = getVersionNumbers(webVersion)
    var isNewer=false
    webVer.forEachIndexed { index, i ->
        if (i > localVer[index]){
            isNewer=true
        }
    }

    return isNewer
}
fun getVersionNumbers(ver: String?): IntArray {
    val m: Matcher = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)")
        .matcher(ver)
    require(m.matches()) { "Malformed FW version" }
    return intArrayOf(
        m.group(1).toInt(), m.group(2).toInt(), m.group(3).toInt()  // rev.

    )
}
}