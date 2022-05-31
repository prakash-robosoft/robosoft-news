package com.robosoft.news.common

/**
 * Result class to map the API response
 */
sealed class Result<out T : Any, out U : Any> {
    data class Success<T : Any>(val data: T) : Result<T, Nothing>()
    data class Error<U : Any>(val error: U) : Result<Nothing, U>()
}
