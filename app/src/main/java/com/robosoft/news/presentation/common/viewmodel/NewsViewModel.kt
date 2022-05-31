package com.robosoft.news.presentation.common.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robosoft.news.common.Result
import com.robosoft.news.domain.usecase.getpopularnews.GetPopularNewsUseCase
import com.robosoft.news.domain.usecase.gettopnews.GetTopNewsUseCase
import com.robosoft.news.presentation.common.PopularNewsFetchState
import com.robosoft.news.presentation.common.TopNewsFetchState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * A Shared view model to manage the news states
 */
class NewsViewModel(
    private val getTopNewsUseCase: GetTopNewsUseCase,
    private val getPopularNewsUseCase: GetPopularNewsUseCase,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val COUNTRY = "us"
        const val CATEGORY = "sports"
        const val PAGE_SIZE = 10
    }

    private val _topNewsFetchState = MutableStateFlow<TopNewsFetchState>(TopNewsFetchState.Init)
    val topNewsFetchState: StateFlow<TopNewsFetchState> get() = _topNewsFetchState

    private val _popularNewsFetchState =
        MutableStateFlow<PopularNewsFetchState>(PopularNewsFetchState.Init)
    val popularNewsFetchState: StateFlow<PopularNewsFetchState> get() = _popularNewsFetchState


    /**
     * Fetches top news
     *
     * @param country code of the country to get headlines
     */
    fun fetchTopNews(country: String) {
        viewModelScope.launch {
            getTopNewsUseCase.execute(country = country).onStart {
                _topNewsFetchState.value = TopNewsFetchState.IsLoading(true)
            }.catch { exception ->
                _topNewsFetchState.value = TopNewsFetchState.IsLoading(false)
            }.collect { result ->
                _topNewsFetchState.value = TopNewsFetchState.IsLoading(false)
                when (result) {
                    is Result.Success -> _topNewsFetchState.value =
                        TopNewsFetchState.Success(result.data)
                    is Result.Error -> _topNewsFetchState.value =
                        TopNewsFetchState.Error(result.error)
                }
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
    fun fetchPopularNews(country: String, category: String, pageSize: Int, page: Int) {

        viewModelScope.launch {
            getPopularNewsUseCase.execute(
                country = country,
                category = category,
                pageSize = pageSize,
                page = page
            ).onStart {
                _popularNewsFetchState.value = PopularNewsFetchState.IsLoading(true)
            }.catch { exception ->
                _popularNewsFetchState.value = PopularNewsFetchState.IsLoading(false)
            }.collect { result ->
                _popularNewsFetchState.value = PopularNewsFetchState.IsLoading(false)
                when (result) {
                    is Result.Success -> _popularNewsFetchState.value =
                        PopularNewsFetchState.Success(result.data)
                    is Result.Error -> _popularNewsFetchState.value =
                        PopularNewsFetchState.Error(result.error)
                }
            }
        }
    }
}
