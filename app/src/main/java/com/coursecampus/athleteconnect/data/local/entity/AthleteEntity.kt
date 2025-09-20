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