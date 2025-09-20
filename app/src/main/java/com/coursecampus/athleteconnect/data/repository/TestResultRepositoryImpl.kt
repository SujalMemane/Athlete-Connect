package com.coursecampus.athleteconnect.data.repository

import com.coursecampus.athleteconnect.data.model.TestResult
import com.coursecampus.athleteconnect.domain.repository.TestResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestResultRepositoryImpl @Inject constructor(
    // private val testResultDao: TestResultDao
) : TestResultRepository {
    
    private val mockResults = mutableListOf<TestResult>()
    
    override fun getAllTestResults(): Flow<List<TestResult>> {
        return flowOf(mockResults)
    }
    
    override suspend fun getTestResultById(id: String): TestResult? {
        return mockResults.find { it.id == id }
    }
    
    override fun getTestResultsByCategory(category: String): Flow<List<TestResult>> {
        return flowOf(mockResults.filter { it.category == category })
    }
    
    override suspend fun saveTestResult(testResult: TestResult) {
        mockResults.add(0, testResult) // Add to beginning
    }
    
    override suspend fun deleteTestResult(testResult: TestResult) {
        mockResults.removeAll { it.id == testResult.id }
    }
    
    override fun getPersonalBests(athleteId: String): Flow<List<TestResult>> {
        val personalBests = mutableMapOf<String, TestResult>()
        
        mockResults.filter { it.athleteId == athleteId }.forEach { result ->
            val existing = personalBests[result.testName]
            if (existing == null || isBetterResult(result, existing)) {
                personalBests[result.testName] = result
            }
        }
        
        return flowOf(personalBests.values.toList())
    }
    
    private fun isBetterResult(new: TestResult, existing: TestResult): Boolean {
        return when (new.category) {
            "Speed" -> new.score < existing.score // Lower time is better
            "Power", "Strength" -> new.score > existing.score // Higher reps/score is better
            "Core" -> new.score > existing.score // Higher time is better
            else -> new.score > existing.score
        }
    }
}
