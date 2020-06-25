/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.repositories

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

}