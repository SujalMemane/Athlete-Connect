package com.coursecampus.athleteconnect.data.model

data class TestResult(
    val id: String,
    val testName: String,
    val score: Double,
    val unit: String,
    val date: String,
    val percentile: Int,
    val category: String,
    val athleteId: String = "",
    val notes: String = "",
    val isPersonalBest: Boolean = false,
    val videoUrl: String = ""
)
