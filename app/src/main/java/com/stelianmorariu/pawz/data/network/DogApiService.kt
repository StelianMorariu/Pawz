/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.data.network


import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface DogApiService {

    @GET(ALL_BREADS)
    fun getAllBreads(): Single<DogBreedListDto>

    @GET(BREED_GALLERY)
    fun getBreedImageList(
        @Path(BREED_PATH_PARAM) breed: String
    ): Single<BreedImageListDto>

    @GET(SUB_BREED_GALLERY)
    fun getSubBreedImageList(
        @Path(BREED_PATH_PARAM) breed: String,
        @Path(SUBBREED_PATH_PARAM) subBreed: String
    ): Single<BreedImageListDto>

    companion object {
        const val BREED_PATH_PARAM = "breed"
        const val SUBBREED_PATH_PARAM = "sub-breed"
        const val ALL_BREADS = "breeds/list/all"
        const val BREED_GALLERY = "breed/{${BREED_PATH_PARAM}}/images"
        const val SUB_BREED_GALLERY = "breed/{${BREED_PATH_PARAM}}/{${SUBBREED_PATH_PARAM}}/images"


    }

}