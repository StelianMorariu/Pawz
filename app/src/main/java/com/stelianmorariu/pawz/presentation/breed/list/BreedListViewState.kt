/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

import com.stelianmorariu.pawz.domain.model.DogBreed

/**
 * Base class that defines the UI state for the [BreedsListActivity].
 */
sealed class BreedListViewState

data class DisplayBreedsState(val breeds: List<DogBreed>) : BreedListViewState()

object LoadingState : BreedListViewState()

object EmptyState : BreedListViewState()

object ErrorState : BreedListViewState()
