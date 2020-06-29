/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.common.widgets

import android.content.Context
import android.graphics.PointF
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller

class GalleryLinearSmoothScroller(private val context: Context) : LinearSmoothScroller(context) {


    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        return (layoutManager as GridLayoutManager).computeScrollVectorForPosition(targetPosition);
    }
}