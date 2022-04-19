package com.app.alchemi.models

data class HomeModel(
    val code: Int,
    val `data`: HomeData,
    val message: String
)
data class HomeData(
    val crypto_live_data: List<CryptoLiveData>,
    val top_gainers: List<TopGainer>,
    val top_news: List<TopNews>,
    val total_balance: Double,
    val coins: List<TopGainer>,
)