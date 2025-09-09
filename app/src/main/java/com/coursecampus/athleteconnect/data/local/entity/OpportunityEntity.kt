package com.coursecampus.athleteconnect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "opportunities")
data class OpportunityEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val type: String, // OpportunityType as string
    val sport: String,
    val location: String,
    val deadline: Long, // Date as timestamp
    val requirements: String, // JSON string of requirements
    val isApplied: Boolean = false,
    val organization: String = "",
    val contactEmail: String = ""
)
