package com.app.alchemi.models

data class AddPinModel(
    val code: Int,
    val `data`: JwtToken,
    val message: String,
    val valid: Boolean
)

data class JwtToken(
    val jwt_token: String,
    val user_id: String
)