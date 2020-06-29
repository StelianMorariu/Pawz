/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.data.network


/**
 * Generic wrapper for server responses.
 *
 * @param message - contains the requested resource or an empty field
 * @param status  - status of the request
 * @param code - optional parameter denoting the http error code if there was an error
 */
data class DogApiResponseDto<T>(
    val message: T,
    val status: String,
    val code: Int = -1
) {

    companion object {
        const val STATUS_SUCCESS = "success"
        const val STATUS_ERROR = "error"
    }
}


