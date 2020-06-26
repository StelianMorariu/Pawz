/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule


/**
 * Create a custom module so we can access GLide resize features more easily.
 */
@GlideModule
class PawzGlideModule : AppGlideModule()