/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.stelianmorariu.pawz.domain.errors.PawzGenericError
import com.stelianmorariu.pawz.domain.errors.PawzNoDataError
import com.stelianmorariu.pawz.domain.errors.PawzServerError
import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.domain.repositories.DogBreedRepository
import com.stelianmorariu.pawz.domain.scheduler.TestSchedulerProvider
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

/**
 * Tests that the view model generates the expected [BreedGalleryViewState] based on repository results.
 */
class BreedGalleryViewModelTest {

    private val mockRepository: DogBreedRepository = mock()
    private val schedulerProvider = TestSchedulerProvider()
    private lateinit var viewModel: BreedGalleryViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `check that view model begins with correct state`() {
        viewModel = BreedGalleryViewModel(mockRepository, schedulerProvider)

        Assert.assertThat(viewModel.viewState.value, CoreMatchers.instanceOf(Default::class.java))
    }

    @Test
    fun `check that breed image urls are loaded`() {
        whenever(mockRepository.getBreedImages(default_dog_breed))
            .thenReturn(Single.just(breed_image_urls))

        viewModel = BreedGalleryViewModel(mockRepository, schedulerProvider)
        viewModel.loadDataIfNecessary(default_dog_breed)

        val expectedState = DisplayGalleryState(breed_image_urls)

        assertEquals(expectedState, viewModel.viewState.value)
        assertEquals(expectedState.images[0], breed_image_urls[0])
    }

    @Test
    fun `check that empty state is returned when the repository returns an empty list`() {
        val pawzError = PawzNoDataError(RuntimeException("no images"))

        whenever(mockRepository.getBreedImages(default_dog_breed))
            .thenReturn(Single.error(pawzError))

        viewModel = BreedGalleryViewModel(mockRepository, schedulerProvider)
        viewModel.loadDataIfNecessary(default_dog_breed)

        val expectedState = ErrorState(pawzError)

        assertEquals(expectedState, viewModel.viewState.value)
        assertEquals(expectedState.pawzError, pawzError)
    }

    @Test
    fun `check that server error is propagated correctly`() {
        val pawzError = PawzServerError("Server error message", 500, RuntimeException("500"))

        whenever(mockRepository.getBreedImages(default_dog_breed))
            .thenReturn(Single.error(pawzError))

        viewModel = BreedGalleryViewModel(mockRepository, schedulerProvider)
        viewModel.loadDataIfNecessary(default_dog_breed)

        val expectedState = ErrorState(pawzError)

        assertEquals(expectedState, viewModel.viewState.value)
        assertEquals(expectedState.pawzError, pawzError)
        assertEquals(expectedState.pawzError.message, pawzError.message)
    }


    @Test
    fun `check that generic runtime error is wrapped in PawzError`() {
        val genericRuntimeException = RuntimeException("test exception")

        whenever(mockRepository.getBreedImages(default_dog_breed))
            .thenReturn(Single.error(genericRuntimeException))

        viewModel = BreedGalleryViewModel(mockRepository, schedulerProvider)
        viewModel.loadDataIfNecessary(default_dog_breed)


        assertThat(
            viewModel.viewState.value,
            CoreMatchers.instanceOf(ErrorState::class.java)
        )
        assertThat(
            (viewModel.viewState.value as ErrorState).pawzError,
            instanceOf(PawzGenericError::class.java)
        )
        assertEquals(
            (viewModel.viewState.value as ErrorState).pawzError.throwable,
            genericRuntimeException
        )
    }

    companion object {

        private val default_dog_breed = DogBreed("display name", "breed")

        private val breed_image_urls = listOf(
            "url1", "url2", "url3"
        )
    }
}