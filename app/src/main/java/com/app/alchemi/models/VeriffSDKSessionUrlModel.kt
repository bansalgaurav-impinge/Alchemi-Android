package com.app.alchemi.models

data class VeriffSDKSessionUrlModel(
    val code: Int,
    val data: SessionData,
    val message: String
)
data class SessionData(
    val host: String,
    val id: String,
    val sessionToken: String,
    val status: String,
    val url: String,
)