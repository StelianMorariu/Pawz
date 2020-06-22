/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.doggo.presentation

import com.stelianmorariu.doggo.domain.DoggoConfig
import com.stelianmorariu.doggo.domain.dagger.DaggerDoggoAppComponent
import com.stelianmorariu.doggo.domain.dagger.utils.AppInjector
import com.stelianmorariu.doggo.domain.scheduler.WorkerSchedulerProvider
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class DoggoApp : DaggerApplication() {

    @Inject
    lateinit var appConfiguration: DoggoConfig

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (appConfiguration.enableLogs) {
            Timber.plant(Timber.DebugTree())
        }
    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val daggerAppComponent = DaggerDoggoAppComponent.builder()
            .application(this)
            .schedulerProvider(WorkerSchedulerProvider())
            .build()

        daggerAppComponent.inject(this)

        AppInjector.init(this)

        return daggerAppComponent

    }
}