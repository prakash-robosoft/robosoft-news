package com.robosoft.news.di

import androidx.lifecycle.SavedStateHandle
import com.robosoft.news.data.repository.NewsRepositoryImpl
import com.robosoft.news.domain.repository.NewsRepository
import com.robosoft.news.domain.usecase.getpopularnews.GetPopularNewsUseCase
import com.robosoft.news.domain.usecase.gettopnews.GetTopNewsUseCase
import com.robosoft.news.presentation.common.viewmodel.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * App module Koin dependencies
 */
val appModule = module {

    single<NewsRepository> { NewsRepositoryImpl(get()) }
    single { GetTopNewsUseCase(get()) }
    single { GetPopularNewsUseCase(get()) }
    single { SavedStateHandle() }
    // Shared ViewModel for News
    viewModel {
        NewsViewModel(get(), get(), get())
    }
}
