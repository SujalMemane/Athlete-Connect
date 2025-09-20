package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.coursecampus.athleteconnect.data.local.converter.StringListConverter

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
    val videoUrl: String = "",
    val imageUrl: String = "",
    val difficulty: String
)
