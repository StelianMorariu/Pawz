/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.dagger

import android.app.Application
import android.content.Context
import com.stelianmorariu.pawz.domain.FakeConnectionChecker
import com.stelianmorariu.pawz.domain.PawzConfig
import com.stelianmorariu.pawz.domain.retrofit.PawzConnectionChecker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestPawzAppModule() {

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
        PawzConfig(true, "/")

    @Provides
    @Singleton
    fun provideConnectivityChecker(context: Context): PawzConnectionChecker =
        FakeConnectionChecker()


}