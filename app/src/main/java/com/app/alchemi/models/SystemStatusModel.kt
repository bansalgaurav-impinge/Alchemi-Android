package com.app.alchemi.models

data class SystemStatusModel(
    val code: Int,
    val `data`: SystemStatus,
    val message: String,
    val valid: Boolean
)
data class SystemStatus(
        val system_response: Boolean
)