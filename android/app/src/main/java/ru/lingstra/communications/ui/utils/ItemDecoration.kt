package ru.lingstra.communications.ui.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(
    private val space: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.childCount == 1) outRect.top = space
        outRect.bottom = space
        outRect.left = space
        outRect.right = space
    }
}

class ItemDecorationBottom(
    private val space: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.childCount == 1) outRect.top = space
        outRect.bottom = space
    }
}