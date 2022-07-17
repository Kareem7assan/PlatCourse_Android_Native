package com.platCourse.platCourseAndroid.listener

import com.rowaad.app.data.model.tweets_model.Tweets

interface TweetActions {
    fun onClickWhats(item: String?=null)
    fun onClickCopy(item: String)
    fun onClickChat(item: Tweets)
    fun onClickLike(item: Tweets)
    fun onClickRetweet(item: Tweets,isRetweet:Boolean, onRetweetBack:()->Unit)
    fun onClickGuest()
    fun onClickBlock(item: Tweets)
    fun onClickAction(item: Tweets)
    fun onClickProfile(item: Tweets)
    fun onClickHashTag(word:String?,item: Tweets)
}