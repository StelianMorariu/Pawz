/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.errors

class PawzNoDataError(override val message: String) : RuntimeException(message) {
}