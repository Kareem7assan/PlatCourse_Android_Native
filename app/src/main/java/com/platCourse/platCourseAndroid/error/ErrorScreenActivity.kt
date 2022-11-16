package com.platCourse.platCourseAndroid.error

import android.hardware.display.DisplayManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rowaad.app.base.R
import com.rowaad.utils.extention.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorScreenActivity : AppCompatActivity() {
    private lateinit var displayManager: DisplayManager

    private val ref by lazy {
        Firebase.database("https://platcourse-ce46f-default-rtdb.firebaseio.com/").reference
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        val msg=intent.getStringExtra("error")
        val tvMsg=findViewById<TextView>(R.id.tvMsg)
        tvMsg.text=msg

        displayManager = applicationContext.getSystemService(DISPLAY_SERVICE) as DisplayManager
        displayManager.registerDisplayListener(object : DisplayManager.DisplayListener {
            override fun onDisplayAdded(p0: Int) {

            }

            override fun onDisplayRemoved(p0: Int) {
                //removeValue(key = "recording" )
                //removeValue(key = "uid" )
                finish()
            }

            override fun onDisplayChanged(p0: Int) {


            }

        }, null)
    }



}