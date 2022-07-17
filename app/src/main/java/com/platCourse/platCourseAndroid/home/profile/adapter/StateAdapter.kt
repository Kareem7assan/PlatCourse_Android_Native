package com.platCourse.platCourseAndroid.home.profile.adapter

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.platCourse.platCourseAndroid.home.profile.tweets.TweetsProfileFragment

class StateAdapter(fragment: FragmentActivity,val lstFragments:MutableList<Fragment>) :FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }


    override fun createFragment(position: Int): Fragment {
       return when(position){
            0->{lstFragments[0] }
            1->{ lstFragments[1] }
            2->{lstFragments[2]}
           else -> {TweetsProfileFragment()}
       }
    }
}