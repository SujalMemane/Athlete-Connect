package com.coursecampus.athleteconnect.domain.repository

import com.coursecampus.athleteconnect.data.model.Conversation
import com.coursecampus.athleteconnect.data.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getAllConversations(): Flow<List<Conversation>>
    suspend fun getConversationById(id: String): Conversation?
    fun getConversationsByUser(userId: String): Flow<List<Conversation>>
    fun getMessagesByConversation(conversationId: String): Flow<List<Message>>
    suspend fun getMessageById(id: String): Message?
    fun getUnreadMessages(): Flow<List<Message>>
    suspend fun insertMessage(message: Message)
    suspend fun insertConversation(conversation: Conversation)
    suspend fun updateMessage(message: Message)
    suspend fun updateConversation(conversation: Conversation)
    suspend fun deleteMessage(message: Message)
    suspend fun deleteConversation(conversation: Conversation)
    suspend fun markMessagesAsRead(conversationId: String)
    suspend fun sendMessage(message: Message)
}

