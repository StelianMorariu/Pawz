/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.gallery

import com.stelianmorariu.pawz.domain.errors.PawzError

/**
 * Base class that defines the UI state for the [BreedsGalleryActivity].
 */
sealed class BreedGalleryViewState

object Default : BreedGalleryViewState()

data class DisplayGalleryState(val images: List<String>) : BreedGalleryViewState()

object LoadingState : BreedGalleryViewState()

/**
 * Empty state is considered an error and is represented by [PawzNoDataError].
 */
data class ErrorState(val pawzError: PawzError) : BreedGalleryViewState()
