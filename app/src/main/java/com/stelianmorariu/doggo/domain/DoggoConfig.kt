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
)