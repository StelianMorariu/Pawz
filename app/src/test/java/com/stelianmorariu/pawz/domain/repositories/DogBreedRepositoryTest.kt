/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.repositories

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.stelianmorariu.pawz.data.network.DogApiResponseDto
import com.stelianmorariu.pawz.data.network.DogApiService
import com.stelianmorariu.pawz.domain.errors.PawzGenericError
import com.stelianmorariu.pawz.domain.errors.PawzNoDataError
import com.stelianmorariu.pawz.domain.errors.PawzServerError
import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.domain.scheduler.TestSchedulerProvider
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

/**
 * Tests that the repository will return the expected data or a [PawzError]
 */
class DogBreedRepositoryTest {

    private val mockDogApiService: DogApiService = mock()
    private val schedulerProvider = TestSchedulerProvider()
    private lateinit var dogBreedRepository: DogBreedRepository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `check that breeds are returned`() {
        whenever(mockDogApiService.getAllBreads())
            .thenReturn(Single.just(breed_list_dto))

        dogBreedRepository = DogBreedRepository(mockDogApiService)
        val result = dogBreedRepository.getAllBreeds()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertComplete()
        result.assertNoErrors()

        result.assertValueCount(1)
        assertThat(result.values()[0].size, equalTo(3))

    }

    @Test
    fun `check that repository emits empty state for breed list`() {
        whenever(mockDogApiService.getAllBreads())
            .thenReturn(Single.just(empty_breed_list_dto))

        dogBreedRepository = DogBreedRepository(mockDogApiService)

        val result = dogBreedRepository.getAllBreeds()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzNoDataError::class.java)
    }

    @Test
    fun `check that repository propagates server error for breed list`() {
        val serverError = PawzServerError("server error", 500, RuntimeException("500"))
        whenever(mockDogApiService.getAllBreads())
            .thenReturn(Single.error(serverError))

        dogBreedRepository = DogBreedRepository(mockDogApiService)
        val result = dogBreedRepository.getAllBreeds()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzServerError::class.java)
        result.assertErrorMessage(serverError.message)

    }

    @Test
    fun `check that repository emits generic error if DTO status is not success for breed list`() {
        whenever(mockDogApiService.getAllBreads())
            .thenReturn(Single.just(breed_list_failed_dto))

        dogBreedRepository = DogBreedRepository(mockDogApiService)
        val result = dogBreedRepository.getAllBreeds()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzGenericError::class.java)
    }

    @Test
    fun `check that repository calls correct image endpoint for breed`() {

        whenever(
            mockDogApiService.getBreedImageList(breed.breed)
        )
            .thenReturn(Single.just(breed_images_dto))


        dogBreedRepository = DogBreedRepository(mockDogApiService)
        val result = dogBreedRepository.getBreedImages(breed)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertComplete()
        result.assertNoErrors()

        result.assertValueCount(1)
        assertTrue(result.values()[0].isNotEmpty())

        verify(mockDogApiService, times(1)).getBreedImageList("breed")
        verify(mockDogApiService, times(0)).getSubBreedImageList("breed", "subbreed")
    }

    @Test
    fun `check that repository calls correct image endpoint for subbreed`() {

        whenever(
            mockDogApiService.getSubBreedImageList(subbreed.breed, subbreed.subBreed)
        )
            .thenReturn(Single.just(breed_images_dto))

        dogBreedRepository = DogBreedRepository(mockDogApiService)
        val result = dogBreedRepository.getBreedImages(subbreed)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertComplete()
        result.assertNoErrors()

        result.assertValueCount(1)
        assertTrue(result.values()[0].isNotEmpty())

        verify(mockDogApiService, times(0)).getBreedImageList("breed")
        verify(mockDogApiService, times(1)).getSubBreedImageList("breed", "subbreed")
    }

    @Test
    fun `check that repository emits empty state for breed image list`() {

        // breed
        whenever(mockDogApiService.getBreedImageList(breed.breed))
            .thenReturn(Single.just(empty_image_list))

        // subbreed
        whenever(
            mockDogApiService.getSubBreedImageList(subbreed.breed, subbreed.subBreed)
        ).thenReturn(Single.just(empty_image_list))

        // breed images
        dogBreedRepository = DogBreedRepository(mockDogApiService)
        var result = dogBreedRepository.getBreedImages(breed)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzNoDataError::class.java)

        // subbreed images
        result = dogBreedRepository.getBreedImages(subbreed)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzNoDataError::class.java)

    }

    @Test
    fun `check that repository propagates server error for breed image list`() {
        val serverError = PawzServerError("server error", 500, RuntimeException("500"))

        // breed
        whenever(mockDogApiService.getBreedImageList(breed.breed))
            .thenReturn(Single.error(serverError))

        // subbreed
        whenever(
            mockDogApiService.getSubBreedImageList(subbreed.breed, subbreed.subBreed)
        ).thenReturn(Single.error(serverError))

        dogBreedRepository = DogBreedRepository(mockDogApiService)

        // breed images
        var result = dogBreedRepository.getBreedImages(breed)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzServerError::class.java)
        assertEquals(result.errors()[0], serverError)

        // subbreed images
        result = dogBreedRepository.getBreedImages(subbreed)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzServerError::class.java)
        assertEquals(result.errors()[0], serverError)
    }

    @Test
    fun `check that repository emits generic error if DTO status is not success for breed image list`() {

        // breed
        whenever(mockDogApiService.getBreedImageList(breed.breed))
            .thenReturn(Single.just(breed_images_dto_failed))

        // subbreed
        whenever(
            mockDogApiService.getSubBreedImageList(subbreed.breed, subbreed.subBreed)
        ).thenReturn(Single.just(breed_images_dto_failed))

        dogBreedRepository = DogBreedRepository(mockDogApiService)

        // breed images
        var result = dogBreedRepository.getBreedImages(breed)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzGenericError::class.java)

        // subbreed images
        result = dogBreedRepository.getBreedImages(subbreed)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzGenericError::class.java)
    }


    companion object {
        private val breed_list_dto = DogApiResponseDto<Map<String, List<String>>>(
            mapOf(
                "breed1" to listOf<String>(), "breed2" to listOf("s1", "s2")
            ), DogApiResponseDto.STATUS_SUCCESS
        )

        private val breed_list_failed_dto = DogApiResponseDto<Map<String, List<String>>>(
            mapOf(
                "breed1" to listOf<String>(), "breed2" to listOf("s1", "s2")
            ), DogApiResponseDto.STATUS_ERROR
        )

        private val empty_breed_list_dto = DogApiResponseDto<Map<String, List<String>>>(
            mapOf(), DogApiResponseDto.STATUS_SUCCESS
        )

        private val breed = DogBreed("breed", "breed")
        private val subbreed = DogBreed("breed", "breed", "subbreed")

        private val breed_images_dto = DogApiResponseDto<List<String>>(
            listOf("url1", "url2", "url3"),
            DogApiResponseDto.STATUS_SUCCESS
        )

        private val breed_images_dto_failed = DogApiResponseDto<List<String>>(
            listOf("url1", "url2", "url3"),
            DogApiResponseDto.STATUS_ERROR
        )
        private val empty_image_list =
            DogApiResponseDto<List<String>>(listOf(), DogApiResponseDto.STATUS_SUCCESS)

    }

}