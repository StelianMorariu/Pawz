/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.dagger.modules


import androidx.lifecycle.ViewModelProvider
import com.stelianmorariu.pawz.domain.dagger.PawzViewModelFactory
import dagger.Binds
import dagger.Module


@Module
abstract class AppViewModelsModule {


//    @Binds
//    @IntoMap
//    @ViewModelKey(SplashViewModel::class)
//    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: PawzViewModelFactory): ViewModelProvider.Factory

}
