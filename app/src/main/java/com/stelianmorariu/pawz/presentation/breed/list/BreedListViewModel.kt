/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stelianmorariu.pawz.domain.errors.PawzError
import com.stelianmorariu.pawz.domain.errors.PawzGenericError
import com.stelianmorariu.pawz.domain.usecases.GetAllBreedsUseCase
import com.stelianmorariu.pawz.presentation.common.BaseViewModel
import javax.inject.Inject

class BreedListViewModel @Inject constructor(private val allBreedsUseCase: GetAllBreedsUseCase) :
    BaseViewModel() {

    val viewState: LiveData<BreedListViewState>
        get() = _viewState

    private val _viewState: MutableLiveData<BreedListViewState> = MutableLiveData(Default)

    fun loadDataIfNecessary() {
        if (_viewState.value is Default) {
            _viewState.postValue(LoadingState)
            compositeDisposable.add(
                allBreedsUseCase.getAllBreeds()
                    .subscribe { breedList, error ->
                        if (error != null) {
                            // check error type if there are multiple types
                            handleError(error)
                        } else {
                            // list content is checked at repository level and a PawzNoDataError
                            // is thrown if there are no breeds
                            _viewState.postValue(DisplayBreedsState(breedList))
                        }
                    }
            )
        }
    }

    private fun handleError(error: Throwable) {
        val state: BreedListViewState = if (error is PawzError) {
            ErrorState(error)
        } else {
            ErrorState(PawzGenericError(error))
        }

        _viewState.postValue(state)
    }

}