/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

import com.stelianmorariu.pawz.domain.errors.PawzError
import com.stelianmorariu.pawz.domain.model.DogBreed

/**
 * Base class that defines the UI state for the [BreedsListActivity].
 */
sealed class BreedListViewState

object Default : BreedListViewState()

data class DisplayBreedsState(val breeds: List<DogBreed>) : BreedListViewState()

object LoadingState : BreedListViewState()

/**
 * Empty state is considered an error and is represented by [PawzNoDataError].
 */
data class ErrorState(val pawzError: PawzError) : BreedListViewState()
