package com.coursecampus.athleteconnect.data.model

data class LeaderboardEntry(
    val athleteId: String,
    val athleteName: String,
    val profilePicture: String,
    val rank: Int,
    val score: Double,
    val testName: String,
    val sport: String,
    val location: String,
    val verified: Boolean
)
