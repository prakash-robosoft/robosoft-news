package com.robosoft.news.data.remote

import com.robosoft.news.BuildConfig
import com.robosoft.news.common.Constants
import com.robosoft.news.data.remote.dto.NewsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    // TODO: Request Interceptor is already implemented to append the key but for some reason it is not called.
    @GET("/v2/top-headlines")
    suspend fun getTopNews(
        @Query(Constants.PARAM_API_KEY) key: String = BuildConfig.NEWS_API_KEY,
        @Query("country") country: String,
        @Query("pageSize") pageSize: Int = 1,
        @Query("page") page: Int = 0
    ): Response<NewsDto>

    @GET("/v2/top-headlines")
    suspend fun getPopularNews(
        @Query(Constants.PARAM_API_KEY) key: String = BuildConfig.NEWS_API_KEY,
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): Response<NewsDto>
}
