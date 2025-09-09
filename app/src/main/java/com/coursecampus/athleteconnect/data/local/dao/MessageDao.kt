package com.coursecampus.athleteconnect.data.local.dao

import androidx.room.*
import com.coursecampus.athleteconnect.data.local.entity.ConversationEntity
import com.coursecampus.athleteconnect.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesByConversation(conversationId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun getMessageById(id: String): MessageEntity?

    @Query("SELECT * FROM messages WHERE senderId = :senderId")
    fun getMessagesBySender(senderId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE read = 0")
    fun getUnreadMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Delete
    suspend fun deleteMessage(message: MessageEntity)

    @Query("UPDATE messages SET read = 1 WHERE conversationId = :conversationId")
    suspend fun markMessagesAsRead(conversationId: String)

    @Query("DELETE FROM messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesByConversation(conversationId: String)
}

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations ORDER BY lastActivity DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :id")
    suspend fun getConversationById(id: String): ConversationEntity?

    @Query("SELECT * FROM conversations WHERE participants LIKE '%' || :userId || '%'")
    fun getConversationsByUser(userId: String): Flow<List<ConversationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)

    @Update
    suspend fun updateConversation(conversation: ConversationEntity)

    @Delete
    suspend fun deleteConversation(conversation: ConversationEntity)

    @Query("UPDATE conversations SET unreadCount = :count WHERE id = :id")
    suspend fun updateUnreadCount(id: String, count: Int)
}

