package com.robosoft.news.common

/**
 * Error Entity to map the API error response
 */
sealed class ErrorEntity {

    object NotFound : ErrorEntity()

    object AccessDenied : ErrorEntity()

    object Unauthorized : ErrorEntity()

    object ServiceUnavailable : ErrorEntity()

    object Unknown : ErrorEntity()
}
