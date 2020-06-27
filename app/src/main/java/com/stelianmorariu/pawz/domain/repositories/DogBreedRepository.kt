/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.repositories

import com.stelianmorariu.pawz.data.network.BreedImageListDto
import com.stelianmorariu.pawz.data.network.DogApiService
import com.stelianmorariu.pawz.data.network.DogBreedListDto
import com.stelianmorariu.pawz.domain.errors.PawzNoDataError
import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.domain.toDomainModel
import io.reactivex.Single
import javax.inject.Inject

class DogBreedRepository @Inject constructor(private val dogApiService: DogApiService) {

    fun getAllBreeds(): Single<List<DogBreed>> =
        dogApiService.getAllBreads().flatMap {
            if (it.status == DogBreedListDto.STATUS_SUCCESS) {
                val mapped = it.toDomainModel()

                if (mapped.isNullOrEmpty()) {
                    Single.error(PawzNoDataError("failed mapping items"))
                } else {
                    Single.just(mapped)
                }
            } else {
                Single.error(PawzNoDataError(it.status))
            }
        }

    fun getBreedImages(dogBreed: DogBreed) =
        getCorrectImageEndpoint(dogBreed)
            .flatMap {
                if (it.status == BreedImageListDto.STATUS_SUCCESS) {
                    if (it.breedImages.isNullOrEmpty()) {
                        Single.error(PawzNoDataError("failed mapping items"))
                    } else {
                        Single.just(it.breedImages)
                    }
                } else {
                    Single.error(PawzNoDataError(it.status))
                }
            }

    /**
     * Determine if we have a sub-breed or a breed with no sub-breeds
     */
    private fun getCorrectImageEndpoint(dogBreed: DogBreed): Single<BreedImageListDto> {
        return if (dogBreed.subBreed.isEmpty()) {
            dogApiService.getBreedImageList(dogBreed.breed)
        } else {
            dogApiService.getSubBreedImageList(dogBreed.breed, dogBreed.subBreed)
        }
    }

}