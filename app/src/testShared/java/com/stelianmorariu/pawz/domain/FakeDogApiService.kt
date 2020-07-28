/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain

import com.stelianmorariu.pawz.data.network.DogApiResponseDto
import com.stelianmorariu.pawz.data.network.DogApiService
import com.stelianmorariu.pawz.domain.errors.PawzGenericError
import com.stelianmorariu.pawz.domain.errors.PawzNoInternetError
import com.stelianmorariu.pawz.domain.errors.PawzServerError
import io.reactivex.Single
import java.net.UnknownHostException

/**
 * Fake implementation of [DogApiService].
 *
 * In production code errors are managed via [PawzCallAdapterFactory] but for simplicity
 * that behaviour is emulated directly in this implementation.
 *
 * The purpose of this implementation is to provide some sample data for tests without hitting the network
 */
class FakeDogApiService : DogApiService {

    var throwNoInternetError: Boolean = false
    var throwServerError: Boolean = false
    var throwGenericError: Boolean = false
    var throwNoDataError: Boolean = false

    // the list of breeds and sub-breeds as it's being sent by the API
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

    // list of images, same images returned for all breeds and sub-breeds
    val imageList: List<String> = listOf(
        "https://images.dog.ceo/breeds/terrier-tibetan/n02097474_7834.jpg",
        "https://images.dog.ceo/breeds/malinois/n02105162_6116.jpg",
        "https://images.dog.ceo/breeds/dingo/n02115641_11220.jpg",
        "https://images.dog.ceo/breeds/pyrenees/n02111500_1879.jpg",
        "https://images.dog.ceo/breeds/dane-great/n02109047_2009.jpg",
        "https://images.dog.ceo/breeds/pointer-germanlonghair/hans1.jpg"
    )

    override fun getAllBreads(): Single<DogApiResponseDto<Map<String, List<String>>>> {
        return when {
            throwNoDataError -> {
                Single.just(DogApiResponseDto(emptyMap(), DogApiResponseDto.STATUS_SUCCESS))
            }
            throwServerError -> {
                Single.error(PawzServerError("Server error", 500, RuntimeException("test")))
            }
            throwGenericError -> {
                Single.error(PawzGenericError(RuntimeException("test")))
            }
            throwNoInternetError -> {
                Single.error(PawzNoInternetError(UnknownHostException("thrown from test")))
            }
            else -> {
                Single.just(DogApiResponseDto(breedsMap, DogApiResponseDto.STATUS_SUCCESS))
            }
        }

    }

    override fun getBreedImageList(breed: String): Single<DogApiResponseDto<List<String>>> {
        return when {
            throwNoDataError -> {
                Single.just(DogApiResponseDto(listOf(), DogApiResponseDto.STATUS_SUCCESS))
            }
            throwServerError -> {
                Single.error(PawzServerError("Server error", 500, RuntimeException("test")))
            }
            throwGenericError -> {
                Single.error(PawzGenericError(RuntimeException("test")))
            }
            throwNoInternetError -> {
                Single.error(PawzNoInternetError(UnknownHostException("thrown from test")))
            }
            else -> {
                Single.just(DogApiResponseDto(imageList, DogApiResponseDto.STATUS_SUCCESS))
            }
        }
    }

    override fun getSubBreedImageList(
        breed: String,
        subBreed: String
    ): Single<DogApiResponseDto<List<String>>> {
        return getBreedImageList(breed)
    }


}