package com.coursecampus.athleteconnect.data.remote

import com.coursecampus.athleteconnect.data.remote.model.AnalyzeRequestDto
import com.coursecampus.athleteconnect.data.remote.model.AnalyzeResponseDto
import com.coursecampus.athleteconnect.data.remote.model.StartAssessmentRequestDto
import com.coursecampus.athleteconnect.data.remote.model.StartAssessmentResponseDto
import com.coursecampus.athleteconnect.data.remote.model.StopAssessmentRequestDto
import com.coursecampus.athleteconnect.data.remote.model.StopAssessmentResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AssessmentApiService {

    @POST("assessment/start")
    suspend fun startAssessment(
        @Body body: StartAssessmentRequestDto
    ): StartAssessmentResponseDto

    @POST("assessment/analyze")
    suspend fun analyzeResult(
        @Body body: AnalyzeRequestDto
    ): AnalyzeResponseDto

    @POST("assessment/stop")
    suspend fun stopAssessment(
        @Body body: StopAssessmentRequestDto
    ): StopAssessmentResponseDto
}


