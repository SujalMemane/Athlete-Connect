package com.coursecampus.athleteconnect.data.model

import java.util.Date

data class Message(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val messageType: MessageType = MessageType.TEXT,
    val senderName: String = "",
    val mediaUrl: String = "",
    val replyTo: String? = null
)

enum class MessageType {
    TEXT, IMAGE, VIDEO, FILE
}

data class Conversation(
    val id: String,
    val participantIds: List<String>,
    val participants: List<String>,
    val lastMessage: Message?,
    val unreadCount: Int = 0,
    val isActive: Boolean = true,
    val lastActivity: String = ""
)
