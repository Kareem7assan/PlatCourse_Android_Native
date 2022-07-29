package com.rowaad.app.data.model.articles

data class ArticlesModel(val articles:List<Article> ? = listOf())
data class Article(val id:Int,val title:String?=null,val text:String?=null,val created_at:String?=null)
