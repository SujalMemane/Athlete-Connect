package com.coursecampus.athleteconnect.data.remote.model

data class AnalyzeRequestDto(
    val testId: String,
    val testName: String,
    val category: String,
    val score: Double,
    val unit: String,
    val durationMs: Long,
    val reps: Int,
    val date: String
)

data class AnalyzeResponseDto(
    val percentile: Int?,
    val feedback: String?,
    val recommendations: List<String>?,
    val talentScore: Double?,
    val olympicReadiness: String?
)

data class StartAssessmentRequestDto(
    val testId: String,
    val testName: String,
    val category: String,
    val cameraIndex: Int? = 0
)

data class StartAssessmentResponseDto(
    val sessionId: String,
    val message: String?
)

data class StopAssessmentRequestDto(
    val sessionId: String
)

data class StopAssessmentResponseDto(
    val message: String?
)


