package com.coursecampus.athleteconnect.data.local.dao

import androidx.room.*
import com.coursecampus.athleteconnect.data.local.entity.FitnessTestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessTestDao {
    @Query("SELECT * FROM fitness_tests")
    fun getAllFitnessTests(): Flow<List<FitnessTestEntity>>

    @Query("SELECT * FROM fitness_tests WHERE id = :id")
    suspend fun getFitnessTestById(id: String): FitnessTestEntity?

    @Query("SELECT * FROM fitness_tests WHERE category = :category")
    fun getFitnessTestsByCategory(category: String): Flow<List<FitnessTestEntity>>

    @Query("SELECT * FROM fitness_tests WHERE difficulty = :difficulty")
    fun getFitnessTestsByDifficulty(difficulty: String): Flow<List<FitnessTestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFitnessTest(fitnessTest: FitnessTestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFitnessTests(fitnessTests: List<FitnessTestEntity>)

    @Update
    suspend fun updateFitnessTest(fitnessTest: FitnessTestEntity)

    @Delete
    suspend fun deleteFitnessTest(fitnessTest: FitnessTestEntity)
}

