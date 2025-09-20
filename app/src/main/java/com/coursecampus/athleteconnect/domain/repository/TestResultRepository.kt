package com.coursecampus.athleteconnect.domain.repository

import com.coursecampus.athleteconnect.data.model.TestResult
import kotlinx.coroutines.flow.Flow

interface TestResultRepository {
    fun getAllTestResults(): Flow<List<TestResult>>
    suspend fun getTestResultById(id: String): TestResult?
    suspend fun saveTestResult(testResult: TestResult)
    suspend fun deleteTestResult(testResult: TestResult)
    fun getTestResultsByCategory(category: String): Flow<List<TestResult>>
    fun getPersonalBests(athleteId: String): Flow<List<TestResult>>
}