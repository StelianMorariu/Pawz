/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.dagger

import android.app.Application
import com.stelianmorariu.pawz.TestPawzApp
import com.stelianmorariu.pawz.data.network.DogApiService
import com.stelianmorariu.pawz.domain.dagger.modules.AppActivityContributorModule
import com.stelianmorariu.pawz.domain.dagger.modules.AppViewModelsModule
import com.stelianmorariu.pawz.domain.scheduler.SchedulersProvider
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestPawzAppModule::class,
        AppViewModelsModule::class,
        AppActivityContributorModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class]
)
interface TestPawzAppComponent : PawzAppComponent {

    fun inject(app: TestPawzApp)

    fun dogApiService(): DogApiService

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): TestPawzAppComponent.Builder

        @BindsInstance
        fun dogApiService(dogApiService: DogApiService): TestPawzAppComponent.Builder

        @BindsInstance
        fun schedulerProvider(schedulersProvider: SchedulersProvider): TestPawzAppComponent.Builder


        fun build(): TestPawzAppComponent
    }
}