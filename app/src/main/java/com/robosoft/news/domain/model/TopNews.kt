package com.robosoft.news.domain.model

import com.robosoft.news.data.remote.dto.Source

data class TopNews(
    val author: String?,
    val content: Any?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)
