package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val conversationId: String,
    val senderId: String,
    val receiverId: String,
    val senderName: String,
    val content: String,
    val timestamp: String,
    val read: Boolean,
    val type: String,
    val mediaUrl: String = "",
    val replyTo: String? = null
)
