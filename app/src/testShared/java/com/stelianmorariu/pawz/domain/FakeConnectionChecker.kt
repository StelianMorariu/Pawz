/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain

import com.stelianmorariu.pawz.domain.retrofit.PawzConnectionChecker

/**
 * Implementation of [PawzConnectionChecker] that will be mocked in unit tests.
 */
class FakeConnectionChecker : PawzConnectionChecker {

    override fun isConnected(): Boolean {
        return false
    }
}