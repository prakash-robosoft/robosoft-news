package com.robosoft.news.presentation.common

import com.robosoft.news.common.ErrorEntity
import com.robosoft.news.domain.model.PopularNews

/**
 * Class that manages possible states of fetching the news
 */
sealed class PopularNewsFetchState {
    object Init : PopularNewsFetchState()
    data class IsLoading(val isLoading: Boolean) : PopularNewsFetchState()
    data class Success(val popularNews: PopularNews) : PopularNewsFetchState()
    data class Error(val error: ErrorEntity) : PopularNewsFetchState()
}
