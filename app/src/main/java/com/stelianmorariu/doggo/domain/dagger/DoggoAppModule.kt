/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.doggo.domain.dagger

import android.app.Application
import android.content.Context
import com.stelianmorariu.doggo.BuildConfig
import com.stelianmorariu.doggo.domain.DoggoConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * This module is responsible for creating the dependencies the `app` module needs to provide.
 */
@Module
class DoggoAppModule {


    /**
     * Provide a [Context] object across the whole app.
     */
    @Provides
    @Singleton
    fun provideContext(app: Application) = app.applicationContext


    /**
     * Provide a [DoggoConfig] object across the whole app.
     */
    @Provides
    @Singleton
    fun provideConfiguration(): DoggoConfig =
        DoggoConfig(BuildConfig.LOGS_ENABLED, BuildConfig.DOG_API_URL)
}