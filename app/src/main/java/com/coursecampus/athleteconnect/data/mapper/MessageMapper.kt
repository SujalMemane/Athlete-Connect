package com.coursecampus.athleteconnect.data.mapper

import com.coursecampus.athleteconnect.data.local.entity.ConversationEntity
import com.coursecampus.athleteconnect.data.local.entity.MessageEntity
import com.coursecampus.athleteconnect.data.model.Conversation
import com.coursecampus.athleteconnect.data.model.Message
import com.coursecampus.athleteconnect.data.model.MessageType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun MessageEntity.toDomain(): Message {
    return Message(
        id = id,
        senderId = senderId,
        receiverId = receiverId,
        senderName = senderName,
        content = content,
        timestamp = timestamp,
        isRead = read,
        messageType = MessageType.valueOf(type),
        mediaUrl = mediaUrl,
        replyTo = replyTo
    )
}

fun Message.toEntity(conversationId: String): MessageEntity {
    return MessageEntity(
        id = id,
        conversationId = conversationId,
        senderId = senderId,
        receiverId = receiverId,
        senderName = senderName,
        content = content,
        timestamp = timestamp,
        read = isRead,
        type = messageType.name,
        mediaUrl = mediaUrl,
        replyTo = replyTo
    )
}

fun ConversationEntity.toDomain(): Conversation {
    val participants = try {
        val gson = Gson()
        val listType = object : TypeToken<List<String>>() {}.type
        gson.fromJson<List<String>>(participants, listType) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
    
    return Conversation(
        id = id,
        participantIds = participants,
        participants = participants,
        lastMessage = null, // Will be populated separately
        unreadCount = unreadCount,
        lastActivity = lastActivity
    )
}

fun Conversation.toEntity(): ConversationEntity {
    val gson = Gson()
    val participantsJson = gson.toJson(participants)
    
    return ConversationEntity(
        id = id,
        participantIds = participantsJson,
        participants = participantsJson,
        lastMessageId = lastMessage?.id,
        unreadCount = unreadCount,
        lastActivity = lastActivity
    )
}

