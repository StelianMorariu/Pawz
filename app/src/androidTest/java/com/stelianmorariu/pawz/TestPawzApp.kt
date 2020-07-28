/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz

import com.stelianmorariu.pawz.domain.FakeDogApiService
import com.stelianmorariu.pawz.domain.dagger.DaggerTestPawzAppComponent
import com.stelianmorariu.pawz.domain.dagger.TestPawzAppComponent
import com.stelianmorariu.pawz.domain.dagger.utils.AppInjector
import com.stelianmorariu.pawz.domain.scheduler.TestSchedulerProvider
import com.stelianmorariu.pawz.presentation.PawzApp
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TestPawzApp : PawzApp() {
    lateinit var daggerAppComponent: TestPawzAppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        daggerAppComponent = DaggerTestPawzAppComponent.builder()
            .application(this)
            .dogApiService(FakeDogApiService())
            .schedulerProvider(TestSchedulerProvider())
            .build()


        daggerAppComponent.inject(this)

        AppInjector.init(this)

        return daggerAppComponent

    }

}