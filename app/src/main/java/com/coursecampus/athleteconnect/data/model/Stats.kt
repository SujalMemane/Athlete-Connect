package com.coursecampus.athleteconnect.data.model

data class Stats(
    val totalAthletes: Int,
    val testsCompleted: Int,
    val opportunitiesPosted: Int,
    val messagesSent: Int,
    val averageScore: Double,
    val topSport: String,
    val activeUsers: Int
)
