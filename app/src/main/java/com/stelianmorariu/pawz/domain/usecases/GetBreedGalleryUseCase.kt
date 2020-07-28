/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.usecases

import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.domain.repositories.DogBreedRepository
import com.stelianmorariu.pawz.domain.scheduler.SchedulersProvider
import io.reactivex.Single
import javax.inject.Inject

class GetBreedGalleryUseCase @Inject constructor(
    private val repository: DogBreedRepository,
    private val schedulersProvider: SchedulersProvider
) {

    fun getBreedImages(dogBreed: DogBreed): Single<List<String>> {
        return repository.getBreedImages(dogBreed)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

}