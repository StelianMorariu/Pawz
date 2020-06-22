/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.doggo.presentation

import com.stelianmorariu.doggo.BuildConfig
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class DoggoApp : DaggerApplication() {


    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.LOGS_ENABLED) {
            Timber.plant(Timber.DebugTree())
        }
    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        TODO("Not yet implemented")
    }
}