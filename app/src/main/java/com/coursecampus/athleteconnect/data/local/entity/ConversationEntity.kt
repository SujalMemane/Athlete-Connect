package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val participantIds: String, // JSON string of participant IDs
    val lastMessageId: String?,
    val unreadCount: Int = 0,
    val isActive: Boolean = true
)
