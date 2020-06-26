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
    override val message: String,
    @DrawableRes val imageId: Int = R.drawable.ic_dog_sad,
    @StringRes val localizedDisplayMessageResId: Int
) : RuntimeException(message)

class PawzGenericError(
    override val message: String
) : PawzError(
    message, R.drawable.ic_dog_sad,
    R.string.err_default_msg
)

/**
 * Error used for empty state situations.
 */
class PawzNoDataError(
    override val message: String
) : PawzError(
    message, R.drawable.ic_dog_digging,
    R.string.err_empty_state_msg
)

/**
 * Error used when we can't reach the server.
 */
class PawzServerError(
    override val message: String
) : PawzError(
    message, R.drawable.ic_dog_digging,
    R.string.err_server_error_msg
)


/**
 * Error used for empty state situations.
 */
class PawzParsingError(
    override val message: String
) : PawzError(
    message, R.drawable.ic_dog_sad,
    R.string.err_parsing_msg
)

/**
 * Error used when there is no internet connection.
 */
class PawzNoInternetError(
    override val message: String
) : PawzError(
    message, R.drawable.ic_dog_with_stick,
    R.string.err_no_internet_msg
)
