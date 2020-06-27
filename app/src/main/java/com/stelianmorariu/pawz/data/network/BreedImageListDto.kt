/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.data.network

import com.google.gson.annotations.SerializedName


/**
 * Data from the API is received as:
 *
 *```
 * {  "message": [
 *      "https:\/\/images.dog.ceo\/breeds\/hound-afghan\/n02088094_1003.jpg",
 *      "https:\/\/images.dog.ceo\/breeds\/hound-afghan\/n02088094_1007.jpg"
 *   ],
 *   status = "success"
 *}
 * ```
 *
 */
data class BreedImageListDto(
    @SerializedName("message")
    val breedImages: List<String>,
    val status: String
) {
    companion object {
        const val STATUS_SUCCESS = "success"
    }
}