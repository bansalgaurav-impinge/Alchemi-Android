package com.app.alchemi.models

data class AllNewsModel(
    val code: Int,
    val `data`: NewsData,
    val message: String,
    val valid: Boolean
)

data class NewsData(
        val top_news: List<TopNews>,
)