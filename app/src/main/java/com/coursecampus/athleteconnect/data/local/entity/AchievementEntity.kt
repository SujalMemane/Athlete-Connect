package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

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
