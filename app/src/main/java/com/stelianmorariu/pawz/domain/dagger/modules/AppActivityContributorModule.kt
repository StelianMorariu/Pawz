/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.dagger.modules

import com.stelianmorariu.pawz.presentation.breed.list.BreedsListActivity
import com.stelianmorariu.pawz.presentation.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Let all activities from the `app` module be injected.
 */
@Module
abstract class AppActivityContributorModule {


    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeBreedListActivity(): BreedsListActivity

}