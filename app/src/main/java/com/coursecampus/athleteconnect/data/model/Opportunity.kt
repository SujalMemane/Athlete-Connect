package com.coursecampus.athleteconnect.data.model

import java.util.Date

data class Opportunity(
    val id: String,
    val title: String,
    val description: String,
    val type: OpportunityType,
    val sport: String,
    val location: String,
    val deadline: Date,
    val requirements: List<String>,
    val isApplied: Boolean = false,
    val organization: String = "",
    val contactEmail: String = ""
)

enum class OpportunityType {
    SCHOLARSHIP, TRIAL, CAMP, JOB, COMPETITION
}
