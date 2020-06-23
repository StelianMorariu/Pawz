/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.dagger.modules


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stelianmorariu.pawz.domain.dagger.PawzViewModelFactory
import com.stelianmorariu.pawz.domain.dagger.utils.ViewModelKey
import com.stelianmorariu.pawz.presentation.breed.list.BreedListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class AppViewModelsModule {


//    @Binds
//    @IntoMap
//    @ViewModelKey(SplashViewModel::class)
//    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(BreedListViewModel::class)
    abstract fun bindBreedListViewModel(breedListViewModel: BreedListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: PawzViewModelFactory): ViewModelProvider.Factory

}
