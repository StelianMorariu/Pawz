/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.doggo.domain

/**
 * Use a configuration object to pass different flags and configuration options in the app.
 */
data class DoggoConfig(
    val enableLogs: Boolean,
    val dogApiUrl: String
) {
    /**
     * The API URL needs to end with a backslash(`/`).
     *
     * This function checks the above condition and adds the required backslash if necessary.
     */
    fun getSafeUrl(): String {
        return if (this.dogApiUrl.endsWith("/"))
            this.dogApiUrl
        else
            "${this.dogApiUrl}/"
    }
}

