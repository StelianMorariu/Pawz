/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.dagger

import android.app.Application
import com.stelianmorariu.pawz.domain.PawzConfig
import com.stelianmorariu.pawz.domain.dagger.modules.AppActivityContributorModule
import com.stelianmorariu.pawz.domain.dagger.modules.AppViewModelsModule
import com.stelianmorariu.pawz.domain.dagger.modules.PawzApiModule
import com.stelianmorariu.pawz.domain.dagger.modules.PawzAppModule
import com.stelianmorariu.pawz.domain.scheduler.SchedulersProvider
import com.stelianmorariu.pawz.presentation.PawzApp
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
        PawzAppModule::class,
        PawzApiModule::class,
        AppViewModelsModule::class,
        AppActivityContributorModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class]
)
interface PawzAppComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: PawzApp)

    fun schedulers(): SchedulersProvider

    fun configuration(): PawzConfig


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
        fun application(application: Application): PawzAppComponent.Builder

        @BindsInstance
        fun schedulerProvider(schedulersProvider: SchedulersProvider): PawzAppComponent.Builder


        fun build(): PawzAppComponent
    }

}