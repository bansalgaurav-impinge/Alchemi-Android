package com.app.alchemi.models
data class CountryListModel(
        val code: Int,
        val data: Data,
        val message: String,
        val valid: Boolean
    )
data class Data (
        val country_list: List<Country>
)
data class Country(
        val country_code: String,
        val country_name: String,
        val id: Int,
        val status: Boolean
)
