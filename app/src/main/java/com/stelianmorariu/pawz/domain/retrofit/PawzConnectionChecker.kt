/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Simple class that checks if the current network can connect to the internet.
 */
class PawzConnectionChecker(private val context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isConnected(): Boolean {
        return connectivityManager.activeNetwork?.let {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(it)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false &&
                    networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
        } ?: false
    }
}