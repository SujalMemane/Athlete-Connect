package com.coursecampus.athleteconnect.data.model

import java.util.Date

data class Message(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Date,
    val isRead: Boolean = false,
    val messageType: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT, IMAGE, VIDEO, FILE
}

data class Conversation(
    val id: String,
    val participantIds: List<String>,
    val lastMessage: Message?,
    val unreadCount: Int = 0,
    val isActive: Boolean = true
)
