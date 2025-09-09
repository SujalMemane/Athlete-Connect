package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fitness_tests")
data class FitnessTestEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val instructions: String, // JSON string of instructions
    val duration: Int,
    val difficulty: String,
    val icon: String = "",
    val isAvailable: Boolean = true
)
