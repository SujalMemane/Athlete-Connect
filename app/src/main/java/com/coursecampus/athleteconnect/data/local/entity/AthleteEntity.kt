package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.coursecampus.athleteconnect.data.local.converter.MapConverter
import com.coursecampus.athleteconnect.data.local.converter.StringListConverter

@Entity(tableName = "athletes")
@TypeConverters(MapConverter::class, StringListConverter::class)
data class AthleteEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val age: Int,
    val sport: String,
    val location: String,
    val profilePicture: String,
    val coverPhoto: String,
    val height: String,
    val weight: String,
    val personalBests: Map<String, String>,
    val achievements: List<String>,
    val videos: List<String>,
    val verified: Boolean,
    val following: Boolean,
    val bio: String,
    val email: String,
    val phone: String,
    val socialMedia: Map<String, String>,
    val joinDate: String,
    val lastActive: String
)

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey
    val id: String,
    val athleteId: String,
    val testName: String,
    val score: Double,
    val unit: String,
    val date: String,
    val percentile: Int,
    val category: String,
    val videoUrl: String,
    val notes: String
)

@Entity(tableName = "opportunities")
@TypeConverters(StringListConverter::class)
data class OpportunityEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val organization: String,
    val type: String,
    val sport: String,
    val location: String,
    val deadline: String,
    val description: String,
    val requirements: List<String>,
    val applied: Boolean,
    val imageUrl: String,
    val contactEmail: String,
    val website: String,
    val salary: String,
    val duration: String,
    val benefits: List<String>
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: String,
    val read: Boolean,
    val type: String,
    val mediaUrl: String,
    val replyTo: String?
)

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val participants: String, // JSON string of participant IDs
    val lastMessageId: String?,
    val unreadCount: Int,
    val lastActivity: String
)

@Entity(tableName = "fitness_tests")
@TypeConverters(StringListConverter::class)
data class FitnessTestEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val instructions: List<String>,
    val duration: Int,
    val videoUrl: String,
    val imageUrl: String,
    val difficulty: String
)

@Entity(tableName = "leaderboard_entries")
data class LeaderboardEntryEntity(
    @PrimaryKey
    val id: String,
    val athleteId: String,
    val athleteName: String,
    val profilePicture: String,
    val rank: Int,
    val score: Double,
    val testName: String,
    val sport: String,
    val location: String,
    val verified: Boolean
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val athleteId: String,
    val title: String,
    val description: String,
    val iconUrl: String,
    val category: String,
    val unlockedDate: String,
    val rarity: String
)

