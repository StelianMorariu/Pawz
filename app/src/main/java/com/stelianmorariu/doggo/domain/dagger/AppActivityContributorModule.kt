/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.doggo.domain.dagger

import com.stelianmorariu.doggo.presentation.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Let all activities from the `app` module be injected.
 */
@Module
abstract class AppActivityContributorModule {


    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

}