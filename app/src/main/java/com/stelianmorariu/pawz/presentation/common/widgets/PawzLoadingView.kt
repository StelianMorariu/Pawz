/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.common.widgets

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.stelianmorariu.pawz.R
import com.stelianmorariu.pawz.databinding.LayoutLoadingStateBinding


class PawzLoadingView : LinearLayout {

    private var loadingAnimation: AnimatedVectorDrawable? = null
    private var loading = false
    private lateinit var binding: LayoutLoadingStateBinding

    var pawzLoadingAnimationListener: PawzLoadingAnimationListener? = null

    private val animatableListener: AnimationCallback by lazy { getAnimatableListener() }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        View.inflate(context, R.layout.layout_loading_state, this)

        binding = LayoutLoadingStateBinding.bind(this)
        orientation = VERTICAL

        loadingAnimation = binding.loadingIv.drawable as AnimatedVectorDrawable
        loadingAnimation?.registerAnimationCallback(animatableListener)
    }


    fun startAnimating() {
        loading = true
        loadingAnimation?.start()
    }

    fun stopAnimation() {
        loading = false
    }

    private fun getAnimatableListener() =
        object : AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                if (loading) {
                    (drawable as Animatable).start()
                } else {
                    pawzLoadingAnimationListener?.onPawzLoadingAnimationCompleted()
                }
            }
        }


    interface PawzLoadingAnimationListener {

        fun onPawzLoadingAnimationCompleted()
    }
}