package com.coursecampus.athleteconnect.data.local.dao

import androidx.room.*
import com.coursecampus.athleteconnect.data.local.entity.TestResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TestResultDao {
    @Query("SELECT * FROM test_results")
    fun getAllTestResults(): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE athleteId = :athleteId")
    fun getTestResultsByAthlete(athleteId: String): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE testName = :testName")
    fun getTestResultsByTestName(testName: String): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE athleteId = :athleteId AND testName = :testName ORDER BY date DESC")
    fun getTestResultsByAthleteAndTest(athleteId: String, testName: String): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE id = :id")
    suspend fun getTestResultById(id: String): TestResultEntity?

    @Query("SELECT * FROM test_results ORDER BY score DESC LIMIT :limit")
    fun getTopTestResults(limit: Int): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE testName = :testName ORDER BY score DESC LIMIT :limit")
    fun getTopTestResultsByTest(testName: String, limit: Int): Flow<List<TestResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(testResult: TestResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResults(testResults: List<TestResultEntity>)

    @Update
    suspend fun updateTestResult(testResult: TestResultEntity)

    @Delete
    suspend fun deleteTestResult(testResult: TestResultEntity)

    @Query("DELETE FROM test_results WHERE athleteId = :athleteId")
    suspend fun deleteTestResultsByAthlete(athleteId: String)
}

