package com.robosoft.news.domain.usecase.gettopnews

import com.robosoft.news.common.ErrorEntity
import com.robosoft.news.common.Result
import com.robosoft.news.domain.model.TopNews
import com.robosoft.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Top headlines use case
 */
class GetTopNewsUseCase(private val newsRepository: NewsRepository) {

    /**
     * Fetches top headlines news
     *
     * @param country code of the country to get headlines
     */
    suspend fun execute(country: String): Flow<Result<TopNews, ErrorEntity>> {
        return newsRepository.getTopNews(country = country)
    }
}
