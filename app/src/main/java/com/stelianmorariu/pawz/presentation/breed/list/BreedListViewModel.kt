/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stelianmorariu.pawz.domain.repositories.DogBreedRepository
import com.stelianmorariu.pawz.domain.scheduler.SchedulersProvider
import com.stelianmorariu.pawz.presentation.common.BaseViewModel
import javax.inject.Inject

class BreedListViewModel @Inject constructor(
    private val breedsRepository: DogBreedRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {

    val viewState: LiveData<BreedListViewState>
        get() = _viewState

    private val _viewState: MutableLiveData<BreedListViewState> = MutableLiveData()

}