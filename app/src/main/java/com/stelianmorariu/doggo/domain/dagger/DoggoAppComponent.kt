/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.doggo.domain.dagger

import android.app.Application
import com.stelianmorariu.doggo.domain.DoggoConfig
import com.stelianmorariu.doggo.domain.dagger.modules.AppActivityContributorModule
import com.stelianmorariu.doggo.domain.dagger.modules.AppViewModelsModule
import com.stelianmorariu.doggo.domain.dagger.modules.DoggoAppModule
import com.stelianmorariu.doggo.domain.scheduler.SchedulersProvider
import com.stelianmorariu.doggo.presentation.DoggoApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        DoggoAppModule::class,
        AppViewModelsModule::class,
        AppActivityContributorModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class]
)
interface DoggoAppComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: DoggoApp)

    fun schedulers(): SchedulersProvider

    fun configuration(): DoggoConfig


    /**
     * This interface is used to provide parameters for modules.
     *
     *  Every method annotated with [BindsInstance] will link the method return type
     * to the method input type for the entire dependency graph - this means that we can't have
     * 2 methods with [BindsInstance] that accept the same type of parameters !!
     */
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): DoggoAppComponent.Builder

        @BindsInstance
        fun schedulerProvider(schedulersProvider: SchedulersProvider): DoggoAppComponent.Builder


        fun build(): DoggoAppComponent
    }

}