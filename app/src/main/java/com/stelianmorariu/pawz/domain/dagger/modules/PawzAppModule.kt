/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.dagger.modules

import android.app.Application
import android.content.Context
import com.stelianmorariu.pawz.BuildConfig
import com.stelianmorariu.pawz.domain.PawzConfig
import com.stelianmorariu.pawz.domain.retrofit.PawzConnectionChecker
import com.stelianmorariu.pawz.domain.retrofit.PawzConnectionCheckerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PawzAppModule {

    /**
     * Provide a [Context] object across the whole app.
     */
    @Provides
    @Singleton
    fun provideContext(app: Application) = app.applicationContext


    /**
     * Provide a [PawzConfig] object across the whole app.
     */
    @Provides
    @Singleton
    fun provideConfiguration(): PawzConfig =
        PawzConfig(BuildConfig.LOGS_ENABLED, BuildConfig.DOG_API_URL)


    @Provides
    @Singleton
    fun provideConnectivityChecker(context: Context): PawzConnectionChecker =
        PawzConnectionCheckerImpl(context)
}