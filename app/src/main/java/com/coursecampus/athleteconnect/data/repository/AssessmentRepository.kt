package com.coursecampus.athleteconnect.data.repository

import com.coursecampus.athleteconnect.data.remote.AssessmentApiService
import com.coursecampus.athleteconnect.data.remote.model.AnalyzeRequestDto
import com.coursecampus.athleteconnect.data.remote.model.AnalyzeResponseDto
import com.coursecampus.athleteconnect.data.remote.model.StartAssessmentRequestDto
import com.coursecampus.athleteconnect.data.remote.model.StartAssessmentResponseDto
import com.coursecampus.athleteconnect.data.remote.model.StopAssessmentRequestDto
import com.coursecampus.athleteconnect.data.remote.model.StopAssessmentResponseDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssessmentRepository @Inject constructor(
    private val api: AssessmentApiService
) {
    suspend fun startAssessment(request: StartAssessmentRequestDto): Result<StartAssessmentResponseDto> {
        return try {
            Result.success(api.startAssessment(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun analyze(request: AnalyzeRequestDto): Result<AnalyzeResponseDto> {
        return try {
            val response = api.analyzeResult(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun stopAssessment(request: StopAssessmentRequestDto): Result<StopAssessmentResponseDto> {
        return try {
            Result.success(api.stopAssessment(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


