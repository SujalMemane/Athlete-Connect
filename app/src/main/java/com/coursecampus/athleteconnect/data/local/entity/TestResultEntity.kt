package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey
    val id: String,
    val testName: String,
    val score: Double,
    val unit: String,
    val date: String,
    val percentile: Int,
    val category: String,
    val athleteId: String = "",
    val notes: String = "",
    val isPersonalBest: Boolean = false
)
