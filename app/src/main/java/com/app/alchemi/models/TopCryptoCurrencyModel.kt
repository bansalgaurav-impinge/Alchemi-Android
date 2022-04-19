package com.app.alchemi.models

data class TopCryptoCurrencyModel(
    val code: Int,
    val `data`: TopCryptoCurrencyData,
    val message: String
)
data class TopCryptoCurrencyData(
        val mktcapfull: List<TopGainer>
)