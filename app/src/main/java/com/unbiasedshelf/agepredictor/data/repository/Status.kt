package com.unbiasedshelf.agepredictor.data.repository

import androidx.annotation.StringRes

sealed class Status<out T>(@StringRes val messageId: Int? = null) {
    class Success<out T>(val value: T, messageId: Int? = null): Status<T>(messageId)
    class Error<out T>(messageId: Int): Status<T>(messageId)
}