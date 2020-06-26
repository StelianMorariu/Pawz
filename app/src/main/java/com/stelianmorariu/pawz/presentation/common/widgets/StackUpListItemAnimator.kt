/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.common.widgets

import android.content.Context

import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator


class StackUpListItemAnimator(private val context: Context, private val screenHeight: Int) :
    SimpleItemAnimator() {
    private val animDelay: Int = 100
    private val animDuration: Int =
        context.resources.getInteger(android.R.integer.config_mediumAnimTime)

    /**
     * [RecyclerView.ItemAnimator] by default creates a new identical ViewHolder because the default behavior
     * is to Crossfade between the old and new ViewHolder. This (depending on the animation used by the ItemAnimator)
     * can lead to the old ViewHolder bugging and showing behind the RecyclerView.
     * @param viewHolder
     * @return true always to avoid the bug described above.
     */
    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {

        val position = holder.adapterPosition
        val delay = animDelay * position

        val set = AnimationSet(true)
        set.interpolator = DecelerateInterpolator()

        val translateAnimation = TranslateAnimation(0f, 0f, (screenHeight / 2).toFloat(), 0f)
        translateAnimation.duration = animDuration.toLong()

        set.addAnimation(translateAnimation)

        val alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation.duration = (animDuration * 2).toLong()

        set.addAnimation(alphaAnimation)
        set.startOffset = delay.toLong()

        holder.itemView.startAnimation(set)
        return true
    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        return false
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int
    ): Boolean {
        return false
    }

    override fun runPendingAnimations() {

    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {

    }

    override fun endAnimations() {

    }

    override fun isRunning(): Boolean {
        return false
    }

}
