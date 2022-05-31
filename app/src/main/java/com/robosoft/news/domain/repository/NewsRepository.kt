package com.robosoft.news.domain.repository

import com.robosoft.news.common.ErrorEntity
import com.robosoft.news.common.Result
import com.robosoft.news.domain.model.PopularNews
import com.robosoft.news.domain.model.TopNews
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    /**
     * Fetches top headlines news
     *
     * @param country code of the country to get headlines
     */
    suspend fun getTopNews(
        country: String
    ): Flow<Result<TopNews, ErrorEntity>>

    /**
     * Fetches popular news
     *
     * @param country code of the country to get headlines
     * @param category category  to get headlines
     * @param pageSize page size per request
     * @param page page number
     */
    suspend fun getPopularNews(
        country: String,
        category: String,
        pageSize: Int,
        page: Int
    ): Flow<Result<PopularNews, ErrorEntity>>

}
