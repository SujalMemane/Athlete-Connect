package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long, // Date as timestamp
    val isRead: Boolean = false,
    val messageType: String = "TEXT" // MessageType as string
)
