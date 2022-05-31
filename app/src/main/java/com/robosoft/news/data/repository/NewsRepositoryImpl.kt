package com.robosoft.news.data.repository

import com.robosoft.news.common.ErrorEntity
import com.robosoft.news.common.Result
import com.robosoft.news.data.remote.NewsApi
import com.robosoft.news.data.remote.mapper.toPopularNews
import com.robosoft.news.data.remote.mapper.toTopNews
import com.robosoft.news.domain.model.PopularNews
import com.robosoft.news.domain.model.TopNews
import com.robosoft.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.HttpURLConnection

class NewsRepositoryImpl(private val newsApi: NewsApi) : NewsRepository {

    /**
     * Fetches top headlines news
     *
     * @param country code of the country to get headlines
     */
    override suspend fun getTopNews(country: String): Flow<Result<TopNews, ErrorEntity>> {
        return flow {
            val response = newsApi.getTopNews(country = country)
            if (response.isSuccessful) {
                response.body()?.toTopNews()?.let { topNews ->
                    emit(Result.Success(topNews))
                } ?: emit(Result.Error<ErrorEntity>(ErrorEntity.NotFound))
            } else {
                val errorEntity = getErrorEntity(response.code())
                emit(Result.Error(errorEntity))
            }
        }
    }

    /**
     * Fetches popular news
     *
     * @param country code of the country to get headlines
     * @param category category  to get headlines
     * @param pageSize page size per request
     * @param page page number
     */
    override suspend fun getPopularNews(
        country: String,
        category: String,
        pageSize: Int,
        page: Int
    ): Flow<Result<PopularNews, ErrorEntity>> {
        return flow {
            val response = newsApi.getPopularNews(
                country = country,
                pageSize = pageSize,
                page = page,
                category = category
            )
            if (response.isSuccessful) {
                response.body()?.toPopularNews()?.let { popularNews ->
                    emit(Result.Success(popularNews))
                } ?: emit(Result.Error(ErrorEntity.NotFound))
            } else {
                val errorEntity = getErrorEntity(response.code())
                emit(Result.Error(errorEntity))
            }
        }
    }

    /**
     * Maps the API error to [ErrorEntity] and returns
     *
     * @param code HTTP status code
     */
    private fun getErrorEntity(code: Int): ErrorEntity {
        return when (code) {

            // Not found
            HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound

            // Access denied
            HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied

            // Unauthorized
            HttpURLConnection.HTTP_UNAUTHORIZED -> ErrorEntity.Unauthorized

            // Unavailable service
            HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable

            // Unknown error
            else -> ErrorEntity.Unknown
        }
    }
}
