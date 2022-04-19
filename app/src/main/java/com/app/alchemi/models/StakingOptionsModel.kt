package com.app.alchemi.models

data class StakingOptionsModel(
    val code: Int,
    val `data`: StakingOptions,
    val message: String,
    val valid: Boolean
)
data class StakingOptions(
        val options: List<Option>
)
data class Option(
        val code: String,
        val id: Int,
        val name: String,
        val description: String,
        val interest: String

)