package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val unlockedDate: String? = null,
    val athleteId: String
)
