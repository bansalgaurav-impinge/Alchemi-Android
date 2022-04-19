package com.app.alchemi.models

data class GetContactSupportModel(
    val code: Int,
    val `data`: MessageData,
    val message: String,
    val valid: Boolean
)
data class MessageData(
    val messages: List<Message>
)
data class Message(
    val message: String,
    val replied_at: String,
    val reply: String,
    val send_at: String
)