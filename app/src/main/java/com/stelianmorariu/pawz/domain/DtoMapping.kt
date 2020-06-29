/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain

import com.stelianmorariu.pawz.data.network.DogApiResponseDto
import com.stelianmorariu.pawz.domain.model.DogBreed

/**
 * Map the network response for dog breeds to the domain model.
 */
fun DogApiResponseDto<Map<String, List<String>>>.toDogBreedList(): List<DogBreed> {
    val breeds: MutableList<DogBreed> = mutableListOf()

    this.message.forEach { (breed, subBreedList) ->
        if (subBreedList.isEmpty()) {
            breeds.add(DogBreed(breed, breed))
        } else {
            subBreedList.forEach { variant ->
                breeds.add(DogBreed("$variant $breed", breed, variant))
            }
        }
    }

    return breeds
}