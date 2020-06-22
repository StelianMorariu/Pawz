/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.model

/**
 * Our domain model represents the source of truth for the  presentation layer.
 * Data acquired from different sources will be mapped to this model.
 */
data class DogBreed(
    val displayName: String,
    val breed: String,
    val subBreed: String = ""
)