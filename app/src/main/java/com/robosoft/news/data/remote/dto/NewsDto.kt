package com.robosoft.news.data.remote.dto

data class NewsDto(
    val articles: List<Article>,
    val status: String,
    val totalResults: String
)
