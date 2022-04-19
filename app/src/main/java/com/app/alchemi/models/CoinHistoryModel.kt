package com.app.alchemi.models

data class CoinHistoryModel(
    val code: Int,
    val `data`: HistoryData,
    val message: String
)
data class HistoryData(
        val historical_data: CryptoHourlyData
)
data class CryptoHourlyData(
        val Aggregated: Boolean,
        val Data: List<CoinData>,
        val TimeFrom: Long,
        val TimeTo: Long,
        val supply:Double,
        val market_cap:Double,
        val user_favourite: Boolean

)
data class CoinData(
        val close: Double,
        val conversionSymbol: String,
        val conversionType: String,
        val high: Double,
        val low: Double,
        val open: Double,
        val time: Long,
        val volumefrom: Double,
        val volumeto: Double
)