/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.data.network

import com.google.gson.annotations.SerializedName


/**
 * Data from the API is received as:
 *
 *```
 * {  "message": {
 *      "affenpinscher": [],
 *       "african": [],
 *       "airedale": [],
 *       "akita": [],
 *       "appenzeller": [],
 *       "australian": [ "shepherd"]
 *   },
 *   status = "success"
 *}
 * ```
 *
 */
data class DogBreedListDto(
    @SerializedName("message")
    val breedDtoMap: Map<String, List<String>>,
    val status: String
) {
    companion object {
        const val STATUS_SUCCESS = "success"
    }
}