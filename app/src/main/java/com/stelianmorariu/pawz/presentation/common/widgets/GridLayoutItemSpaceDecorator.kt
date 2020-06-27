/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.common.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridLayoutItemSpaceDecorator(private val pixelSpacing: Int, private val columnCount: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter!!.itemCount

        // start with half padding for all items
        outRect.left = pixelSpacing / 2
        outRect.right = pixelSpacing / 2
        outRect.top = pixelSpacing / 2
        outRect.bottom = pixelSpacing / 2

        when {
            // add full top spacing to items on first row
            position < columnCount -> {
                outRect.top = pixelSpacing
            }

            position >= itemCount - columnCount -> {
                outRect.bottom = pixelSpacing
            }

            // add full left spacing  to the items on the first column
            position.rem(columnCount) == 0 -> {
                outRect.left = pixelSpacing
            }

            // add full right spacing to the items on the last column
            position.rem(columnCount) == columnCount - 1 -> {
                outRect.right = pixelSpacing
            }


        }


    }

}