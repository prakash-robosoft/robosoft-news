package com.robosoft.news.data.remote.util

import com.robosoft.news.BuildConfig
import com.robosoft.news.common.Constants.PARAM_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Request interceptor that appends API key to every request
 */
class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url.newBuilder().addQueryParameter(
            PARAM_API_KEY,
            BuildConfig.NEWS_API_KEY
        )
        val newRequest = request.newBuilder().url(newUrl.build()).build()
        return chain.proceed(newRequest)
    }
}
