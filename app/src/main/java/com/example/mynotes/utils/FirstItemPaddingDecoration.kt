package com.example.mynotes.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class FirstItemPaddingDecoration(private val paddingTop: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)

        // Apply padding only to the first item
        if (itemPosition == 0) {
            outRect.top = paddingTop
        } else {
            outRect.top = 0
        }
    }
}