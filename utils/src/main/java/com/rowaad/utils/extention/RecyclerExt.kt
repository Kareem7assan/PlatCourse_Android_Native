package com.rowaad.utils.extention

import android.content.Context
import android.graphics.Color
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.rowaad.utils.R
import com.rowaad.utils.SwipeHelper

fun RecyclerView.setAnimation(context: Context, anim:Int){
    val animation = AnimationUtils.loadLayoutAnimation(context,anim)
    layoutAnimation = animation
}
fun RecyclerView.customSwipe(context: Context,onAction:(Int)->Unit){
    object : SwipeHelper(context, this@customSwipe) {
        override fun instantiateUnderlayButton(
            viewHolder: RecyclerView.ViewHolder?,
            underlayButtons: MutableList<UnderlayButton>?
        ) {
            underlayButtons!!.add(
                UnderlayButton(context, context.getString(R.string.delete_notif),
                    0,
                    Color.parseColor("#FF3c30"),
                    object : UnderlayButtonClickListener {
                        override fun onClick(pos: Int) {
                            onAction.invoke(pos)
                        }
                    }

                ))
        }

    }
}
fun RecyclerView.swipeDelete(onAction:(Int)->Unit){
    ItemTouchHelper(
        object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onAction.invoke(viewHolder.adapterPosition)
            }

        }
    ).attachToRecyclerView(this)

}
