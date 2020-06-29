/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stelianmorariu.pawz.domain.errors.PawzError
import com.stelianmorariu.pawz.domain.errors.PawzGenericError
import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.domain.repositories.DogBreedRepository
import com.stelianmorariu.pawz.domain.scheduler.SchedulersProvider
import com.stelianmorariu.pawz.presentation.common.BaseViewModel
import javax.inject.Inject

class BreedGalleryViewModel @Inject constructor(
    private val breedsRepository: DogBreedRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {

    val viewState: LiveData<BreedGalleryViewState>
        get() = _viewState

    private val _viewState: MutableLiveData<BreedGalleryViewState> = MutableLiveData(Default)

    fun loadDataIfNecessary(dogBreed: DogBreed) {
        if (_viewState.value is Default) {
            _viewState.postValue(LoadingState)
            compositeDisposable.add(
                breedsRepository.getBreedImages(dogBreed)
                    .subscribeOn(schedulersProvider.io())
                    .observeOn(schedulersProvider.ui())
                    .subscribe { breedImageList, error ->
                        if (error != null) {
                            // check error type if there are multiple types
                            handleError(error)
                        } else {
                            // list content is checked at repository level and a PawzNoDataError
                            // is thrown if there are no breeds
                            _viewState.postValue(DisplayGalleryState(breedImageList))
                        }
                    }
            )
        }
    }

    private fun handleError(error: Throwable) {
        val state: BreedGalleryViewState = if (error is PawzError) {
            ErrorState(error)
        } else {
            ErrorState(PawzGenericError(error))
        }

        _viewState.postValue(state)
    }

}