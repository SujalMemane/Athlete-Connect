package com.coursecampus.athleteconnect.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Athlete(
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
    val testResults: List<TestResult>,
    val achievements: List<String>,
    val videos: List<String>,
    val verified: Boolean,
    val following: Boolean,
    val bio: String = "",
    val email: String = "",
    val phone: String = "",
    val socialMedia: Map<String, String> = emptyMap(),
    val joinDate: String = "",
    val lastActive: String = ""
) : Parcelable

@Parcelize
data class TestResult(
    val id: String,
    val testName: String,
    val score: Double,
    val unit: String,
    val date: String,
    val percentile: Int,
    val category: String = "",
    val videoUrl: String = "",
    val notes: String = ""
) : Parcelable

@Parcelize
data class Opportunity(
    val id: String,
    val title: String,
    val organization: String,
    val type: OpportunityType,
    val sport: String,
    val location: String,
    val deadline: String,
    val description: String,
    val requirements: List<String>,
    val applied: Boolean,
    val imageUrl: String = "",
    val contactEmail: String = "",
    val website: String = "",
    val salary: String = "",
    val duration: String = "",
    val benefits: List<String> = emptyList()
) : Parcelable

enum class OpportunityType {
    SCHOLARSHIP,
    TRIAL,
    CAMP,
    INTERNSHIP,
    JOB,
    COMPETITION
}

@Parcelize
data class Message(
    val id: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: String,
    val read: Boolean,
    val type: MessageType = MessageType.TEXT,
    val mediaUrl: String = "",
    val replyTo: String? = null
) : Parcelable

enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    FILE
}

@Parcelize
data class Conversation(
    val id: String,
    val participants: List<String>,
    val lastMessage: Message?,
    val unreadCount: Int,
    val lastActivity: String
) : Parcelable

@Parcelize
data class FitnessTest(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val instructions: List<String>,
    val duration: Int, // in seconds
    val videoUrl: String = "",
    val imageUrl: String = "",
    val difficulty: Difficulty = Difficulty.INTERMEDIATE
) : Parcelable

enum class Difficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}

@Parcelize
data class LeaderboardEntry(
    val athleteId: String,
    val athleteName: String,
    val profilePicture: String,
    val rank: Int,
    val score: Double,
    val testName: String,
    val sport: String,
    val location: String,
    val verified: Boolean
) : Parcelable

@Parcelize
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconUrl: String,
    val category: String,
    val unlockedDate: String,
    val rarity: Rarity = Rarity.COMMON
) : Parcelable

enum class Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY
}

@Parcelize
data class Stats(
    val totalAthletes: Int,
    val testsCompleted: Int,
    val opportunitiesPosted: Int,
    val messagesSent: Int,
    val averageScore: Double,
    val topSport: String,
    val activeUsers: Int
) : Parcelable

@Parcelize
data class User(
    val id: String,
    val email: String,
    val name: String,
    val profilePicture: String,
    val isVerified: Boolean,
    val preferences: UserPreferences,
    val subscription: Subscription = Subscription.FREE
) : Parcelable

@Parcelize
data class UserPreferences(
    val notifications: Boolean = true,
    val darkMode: Boolean = false,
    val language: String = "en",
    val units: String = "metric",
    val privacy: PrivacySettings = PrivacySettings()
) : Parcelable

@Parcelize
data class PrivacySettings(
    val profileVisibility: String = "public",
    val showLocation: Boolean = true,
    val showContactInfo: Boolean = false,
    val allowMessages: Boolean = true
) : Parcelable

enum class Subscription {
    FREE,
    PREMIUM,
    PRO
}

