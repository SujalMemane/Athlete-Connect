package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.coursecampus.athleteconnect.data.local.converter.StringListConverter

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
