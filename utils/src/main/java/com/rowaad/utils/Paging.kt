package com.rowaad.utils

object Paging {

    fun hasMoreItems(currentPage:Int,totalPages:Int):Boolean{
        return currentPage<totalPages
    }
}