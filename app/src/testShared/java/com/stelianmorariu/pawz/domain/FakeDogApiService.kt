/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain

import com.stelianmorariu.pawz.data.network.DogApiResponseDto
import com.stelianmorariu.pawz.data.network.DogApiService
import com.stelianmorariu.pawz.domain.errors.PawzNoInternetError
import com.stelianmorariu.pawz.domain.errors.PawzServerError
import io.reactivex.Single
import java.net.UnknownHostException

class FakeDogApiService : DogApiService {

    var throwNoInternetError: Boolean = false
    var throwServerError: Boolean = false
    var throwNoDataError: Boolean = false

    val breedsMap: Map<String, List<String>> = mapOf(
        "affenpinscher" to emptyList(),
        "african" to emptyList(),
        "airedale" to emptyList(),
        "akita" to emptyList(),
        "appenzeller" to emptyList(),
        "australian" to listOf("shepherd"),
        "basenji" to emptyList(),
        "beagle" to emptyList(),
        "bluetick" to emptyList(),
        "borzoi" to emptyList(),
        "bouvier" to emptyList(),
        "boxer" to emptyList(),
        "brabancon" to emptyList(),
        "briard" to emptyList(),
        "buhund" to listOf("norwegian")
    )

    override fun getAllBreads(): Single<DogApiResponseDto<Map<String, List<String>>>> {
        return if (throwNoDataError) {
            Single.just(DogApiResponseDto(emptyMap(), DogApiResponseDto.STATUS_SUCCESS))
        } else if (throwServerError) {
            Single.error(PawzServerError("Server error", 500, RuntimeException("test")))

        } else if (throwNoInternetError) {
            Single.error(PawzNoInternetError(UnknownHostException("thron from test")))
        } else {
            Single.just(DogApiResponseDto(breedsMap, DogApiResponseDto.STATUS_SUCCESS))
        }

    }

    override fun getBreedImageList(breed: String): Single<DogApiResponseDto<List<String>>> {
        TODO("Not yet implemented")
    }

    override fun getSubBreedImageList(
        breed: String,
        subBreed: String
    ): Single<DogApiResponseDto<List<String>>> {
        TODO("Not yet implemented")
    }
}