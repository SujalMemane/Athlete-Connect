package com.coursecampus.athleteconnect.data.model

import java.util.Date

data class Opportunity(
    val id: String,
    val title: String,
    val description: String,
    val type: OpportunityType,
    val sport: String,
    val location: String,
    val deadline: String,
    val requirements: List<String>,
    val applied: Boolean = false,
    val organization: String = "",
    val contactEmail: String = "",
    val salary: String = "",
    val duration: String = "",
    val imageUrl: String = "",
    val website: String = "",
    val benefits: List<String> = emptyList()
)

enum class OpportunityType {
    SCHOLARSHIP, TRIAL, CAMP, JOB, COMPETITION, INTERNSHIP
}
