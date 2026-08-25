package com.example.minewiki.ui.chat

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: String = "",
    val modelUsed: String? = null
)
