package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

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
