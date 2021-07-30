package com.app.alchemi.models

data class UserDetailModel(
    val code: Int,
    val data: userData,
    val message: String
)
data class userData(
        val email: String,
        val firstname: String,
        val gender: Any,
        val id: String,
        val lastname: String,
        val phone: String,
        val pin: String,
        val selfie: String,
        val user_id: String,
        val isSsnVerified: Boolean,
        val pin_status: Boolean,
        val notification_status: Boolean,
        val anti_phishing_code:Any,
        val username: String
)