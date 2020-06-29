/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.common

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.request.RequestOptions
import com.stelianmorariu.pawz.domain.GlideApp


fun ImageView.loadImage(url: String?, @DrawableRes placeHolderResId: Int) {
    GlideApp.with(this)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(placeHolderResId)
                .error(placeHolderResId)
                .centerCrop()
        )
        .into(this)
}

fun ImageView.loadImageNoCrop(url: String?, @DrawableRes placeHolderResId: Int) {
    GlideApp.with(this)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(placeHolderResId)
                .error(placeHolderResId)
                .centerInside()
        )
        .into(this)
}

/**
 * Simple function to load image from a drawable resource.
 */
fun ImageView.loadImage(resId: Int) {
    GlideApp.with(this)
        .load(resId)
        .into(this)
}