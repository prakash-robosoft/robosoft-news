package com.robosoft.news.domain.model

import com.robosoft.news.data.remote.dto.Article

data class PopularNews(
    val articles: List<Article>,
    val status: String,
    val totalResults: String
)