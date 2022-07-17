package com.rowaad.app.data.model

import com.rowaad.app.data.model.orders.CategoriesItem
import com.rowaad.app.data.model.tweets_model.City
import com.rowaad.app.data.model.tweets_model.Region
import java.io.Serializable

data class TweetAdModel(var selectedRegions: Region?=null,
                        var selectedCities: City?=null,
                        var selectedCategory: CategoriesItem?=null,
                        var selectedSubCategory: CategoriesItem?=null,
                        var price:String?="0.0",
                        var phone:String?=null,
                        var description:String?=null,
                        var maxCount:Int=10,
                        var imgsModel:MutableList<ImageModel> = mutableListOf()
):Serializable