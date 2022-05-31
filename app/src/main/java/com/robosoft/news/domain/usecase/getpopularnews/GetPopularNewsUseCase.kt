package com.robosoft.news.domain.usecase.getpopularnews

import com.robosoft.news.common.ErrorEntity
import com.robosoft.news.common.Result
import com.robosoft.news.domain.model.PopularNews
import com.robosoft.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Specific use case to fetch popular news
 */
class GetPopularNewsUseCase(private val newsRepository: NewsRepository) {

    /**
     * Fetches popular news
     *
     * @param country code of the country to get headlines
     * @param category category  to get headlines
     * @param pageSize page size per request
     * @param page page number
     */
    suspend fun execute(
        country: String,
        category: String,
        pageSize: Int,
        page: Int
    ): Flow<Result<PopularNews, ErrorEntity>> {
        return newsRepository.getPopularNews(
            country = country,
            category = category,
            pageSize = pageSize,
            page = page
        )
    }
}