package com.example.listxml.utill

import android.graphics.Canvas
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteCallback :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        private var cardPicked = true
        private var reset = false

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {

            // elevate only when picked for the first time
            if (cardPicked) {
                val animator = viewHolder.itemView.animate()
                animator.translationZ(16f)
                animator.duration = 200
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.start()
                cardPicked = false
            }

            // when your item is not floating anymore
            if (reset) {
                val animator = viewHolder.itemView.animate()
                animator.translationZ(0f)
                animator.duration = 200
                animator.interpolator = AccelerateInterpolator()
                animator.start()
                cardPicked = true
                reset = false
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }