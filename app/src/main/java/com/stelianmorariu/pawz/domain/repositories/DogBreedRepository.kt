/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.repositories

import com.stelianmorariu.pawz.data.network.DogApiResponseDto
import com.stelianmorariu.pawz.data.network.DogApiService
import com.stelianmorariu.pawz.domain.errors.PawzGenericError
import com.stelianmorariu.pawz.domain.errors.PawzNoDataError
import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.domain.toDogBreedList
import io.reactivex.Single
import javax.inject.Inject

class DogBreedRepository @Inject constructor(private val dogApiService: DogApiService) {

    fun getAllBreeds(): Single<List<DogBreed>> =
        dogApiService.getAllBreads().flatMap {
            if (it.status == DogApiResponseDto.STATUS_SUCCESS) {
                val mapped = it.toDogBreedList()

                if (mapped.isNullOrEmpty()) {
                    Single.error(PawzNoDataError(RuntimeException("Failed mapping items")))
                } else {
                    Single.just(mapped)
                }
            } else {
                Single.error(PawzGenericError(RuntimeException("Failed to get breeds")))
            }
        }


    fun getBreedImages(dogBreed: DogBreed) =
        getCorrectImageEndpoint(dogBreed)
            .flatMap {
                if (it.status == DogApiResponseDto.STATUS_SUCCESS) {
                    if (it.message.isNullOrEmpty()) {
                        Single.error(PawzNoDataError(RuntimeException("No breed images")))
                    } else {
                        Single.just(it.message)
                    }
                } else {
                    Single.error(PawzGenericError(RuntimeException("Failed to get breed images")))
                }
            }


    /**
     * Determine if we have a sub-breed or a breed with no sub-breeds
     */
    private fun getCorrectImageEndpoint(dogBreed: DogBreed): Single<DogApiResponseDto<List<String>>> {
        return if (dogBreed.subBreed.isEmpty()) {
            dogApiService.getBreedImageList(dogBreed.breed)
        } else {
            dogApiService.getSubBreedImageList(dogBreed.breed, dogBreed.subBreed)
        }
    }

}