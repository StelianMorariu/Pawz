/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.errors

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.stelianmorariu.pawz.R

/**
 * Define a generic domain level error for Pawz that contains all the necessary information
 * for displaying the error to the user.
 *
 * These domain level errors are used to abstract underlying causes
 * and make process of displaying them to the user easier and more consistent.
 */
sealed class PawzError(
    override val message: String = "",
    @DrawableRes val imageId: Int = R.drawable.ic_dog_sad,
    @StringRes val localizedDisplayMessageResId: Int,
    val throwable: Throwable
) : RuntimeException(throwable)

class PawzGenericError(throwable: Throwable) : PawzError(
    imageId = R.drawable.ic_dog_sad,
    localizedDisplayMessageResId = R.string.err_default_msg,
    throwable = throwable
)

/**
 * Error used for empty state situations.
 */
class PawzNoDataError(throwable: Throwable) : PawzError(
    imageId = R.drawable.ic_dog_digging,
    localizedDisplayMessageResId = R.string.err_empty_state_msg,
    throwable = throwable
)

/**
 * Error used when the server is able to give us a reason for the failed request.
 */
class PawzServerError(
    override val message: String,
    val code: Int,
    throwable: Throwable
) : PawzError(
    message = message,
    imageId = R.drawable.ic_dog_sad,
    localizedDisplayMessageResId = R.string.err_server_error_msg,
    throwable = throwable
)

/**
 * Error used when there is no internet connection.
 */
class PawzNoInternetError(throwable: Throwable) : PawzError(
    imageId = R.drawable.ic_dog_with_stick,
    localizedDisplayMessageResId = R.string.err_no_internet_msg,
    throwable = throwable
)
