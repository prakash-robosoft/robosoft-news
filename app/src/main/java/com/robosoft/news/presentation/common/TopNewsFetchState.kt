package com.robosoft.news.presentation.common

import com.robosoft.news.common.ErrorEntity
import com.robosoft.news.domain.model.TopNews

/**
 * Class that manages possible states of fetching the news
 */
sealed class TopNewsFetchState {
    object Init : TopNewsFetchState()
    data class IsLoading(val isLoading: Boolean) : TopNewsFetchState()
    data class Success(val topNews: TopNews) : TopNewsFetchState()
    data class Error(val error: ErrorEntity) : TopNewsFetchState()
}