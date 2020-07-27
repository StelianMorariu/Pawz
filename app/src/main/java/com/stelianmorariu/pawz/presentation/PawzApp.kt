/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation

import com.stelianmorariu.pawz.domain.PawzConfig
import com.stelianmorariu.pawz.domain.dagger.DaggerPawzAppComponent
import com.stelianmorariu.pawz.domain.dagger.utils.AppInjector
import com.stelianmorariu.pawz.domain.scheduler.WorkerSchedulerProvider
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

open class PawzApp : DaggerApplication() {

    @Inject
    lateinit var appConfiguration: PawzConfig

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
        val daggerAppComponent = DaggerPawzAppComponent.builder()
            .application(this)
            .schedulerProvider(WorkerSchedulerProvider())
            .build()

        daggerAppComponent.inject(this)

        AppInjector.init(this)

        return daggerAppComponent

    }
}