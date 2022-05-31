package com.robosoft.news.data.remote.mapper

import com.robosoft.news.data.remote.dto.NewsDto
import com.robosoft.news.domain.model.PopularNews
import com.robosoft.news.domain.model.TopNews

/**
 * Maps from News response DTO to Top headline news
 */
fun NewsDto.toTopNews(): TopNews? {
    return this.articles.firstOrNull()?.let { article ->
        TopNews(
            author = article.author,
            content = article.content,
            description = article.description,
            publishedAt = article.publishedAt,
            source = article.source,
            title = article.title,
            url = article.url,
            urlToImage = article.urlToImage
        )
    }
}

/**
 * Maps from News response DTO to popular news
 */
fun NewsDto.toPopularNews(): PopularNews {
    return PopularNews(
        articles = this.articles,
        status = this.status,
        totalResults = this.totalResults
    )
}
