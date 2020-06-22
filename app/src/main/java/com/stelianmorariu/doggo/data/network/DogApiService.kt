/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.doggo.data.network


import io.reactivex.Single
import retrofit2.http.GET


interface DogApiService {

    @GET(ALL_BREADS)
    fun getAllBreads(): Single<DogBreedListDto>

    companion object {
        const val ALL_BREADS = "breeds/list/all"
    }

}