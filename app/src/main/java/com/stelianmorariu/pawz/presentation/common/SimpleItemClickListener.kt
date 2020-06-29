/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.common

/**
 * Generic item click listener that can be used in RecyclerViews.
 */
interface SimpleItemClickListener<T> {
    fun onItemClicked(item: T)
}