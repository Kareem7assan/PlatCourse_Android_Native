package com.rowaad.utils.extention

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


fun NestedScrollView.handlePagination(onLoadMore:()->Unit){
    setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
        if (v.getChildAt(v.childCount - 1) != null) {
            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                scrollY > oldScrollY) {
                onLoadMore.invoke()
            }

        }
    }
}

fun RecyclerView.handlePagination(visibleThreshold:Int=1, onLoadMore:()->Unit){
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layManger=recyclerView.layoutManager
            var lastVisibleItem=0
            val totalItemCount = layManger?.itemCount ?: 0
            when(layManger ){
                is LinearLayoutManager -> lastVisibleItem=layManger.findLastVisibleItemPosition()
                is GridLayoutManager -> lastVisibleItem=layManger.findLastVisibleItemPosition()
            }
            if ( dy > 0 && totalItemCount <= lastVisibleItem + visibleThreshold) {
                onLoadMore.invoke()
            }


        }
    })
}