package com.coursecampus.athleteconnect.domain.repository

import com.coursecampus.athleteconnect.data.model.TestResult
import kotlinx.coroutines.flow.Flow

interface TestResultRepository {
    fun getAllTestResults(): Flow<List<TestResult>>
    fun getTestResultsByAthlete(athleteId: String): Flow<List<TestResult>>
    fun getTestResultsByTestName(testName: String): Flow<List<TestResult>>
    fun getTestResultsByAthleteAndTest(athleteId: String, testName: String): Flow<List<TestResult>>
    suspend fun getTestResultById(id: String): TestResult?
    fun getTopTestResults(limit: Int): Flow<List<TestResult>>
    fun getTopTestResultsByTest(testName: String, limit: Int): Flow<List<TestResult>>
    suspend fun insertTestResult(testResult: TestResult)
    suspend fun updateTestResult(testResult: TestResult)
    suspend fun deleteTestResult(testResult: TestResult)
    suspend fun refreshTestResults()
}

