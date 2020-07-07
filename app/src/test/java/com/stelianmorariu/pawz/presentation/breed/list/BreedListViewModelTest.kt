/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

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
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

/**
 * Tests that the view model generates the expected [BreedListViewState] based on repository results.
 */
class BreedListViewModelTest {

    private val mockRepository: DogBreedRepository = mock()
    private val schedulerProvider = TestSchedulerProvider()
    private lateinit var viewModel: BreedListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `check that view model begins with correct state`() {
        viewModel = BreedListViewModel(mockRepository, schedulerProvider)

        assertThat(viewModel.viewState.value, instanceOf(Default::class.java))
    }

    @Test
    fun `check that breeds are loaded`() {
        whenever(mockRepository.getAllBreeds())
            .thenReturn(Single.just(dog_breeds))

        viewModel = BreedListViewModel(mockRepository, schedulerProvider)
        viewModel.loadDataIfNecessary()

        val expectedState = DisplayBreedsState(dog_breeds)

        assertEquals(expectedState, viewModel.viewState.value)
        assertEquals(expectedState.breeds[0], dog_breeds[0])
    }

    @Test
    fun `check that empty state is returned when the repository returns an empty list`() {
        val pawzError = PawzNoDataError(RuntimeException("failed mapping items"))

        whenever(mockRepository.getAllBreeds())
            .thenReturn(Single.error(pawzError))

        viewModel = BreedListViewModel(mockRepository, schedulerProvider)
        viewModel.loadDataIfNecessary()

        val expectedState = ErrorState(pawzError)

        assertEquals(expectedState, viewModel.viewState.value)
        assertEquals(expectedState.pawzError, pawzError)
    }

    @Test
    fun `check that server error is propagated correctly`() {
        val pawzError = PawzServerError("Server error message", 500, RuntimeException("500"))

        whenever(mockRepository.getAllBreeds())
            .thenReturn(Single.error(pawzError))

        viewModel = BreedListViewModel(mockRepository, schedulerProvider)
        viewModel.loadDataIfNecessary()

        val expectedState = ErrorState(pawzError)

        assertEquals(expectedState, viewModel.viewState.value)
        assertEquals(expectedState.pawzError, pawzError)
        assertEquals(expectedState.pawzError.message, pawzError.message)
    }


    @Test
    fun `check that generic runtime error is wrapped in PawzError`() {
        val genericRuntimeException = RuntimeException("test exception")

        whenever(mockRepository.getAllBreeds())
            .thenReturn(Single.error(genericRuntimeException))

        viewModel = BreedListViewModel(mockRepository, schedulerProvider)
        viewModel.loadDataIfNecessary()


        assertThat(viewModel.viewState.value, instanceOf(ErrorState::class.java))
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

        private val dog_breeds = listOf(
            DogBreed("test1", "t1"),
            DogBreed("test2", "t2", "subbreed"),
            DogBreed("test3", "t3"),
            DogBreed("test4", "t4")
        )
    }
}