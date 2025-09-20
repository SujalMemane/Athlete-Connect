package com.coursecampus.athleteconnect.data.local.dao

import androidx.room.*
import com.coursecampus.athleteconnect.data.local.entity.OpportunityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OpportunityDao {
    @Query("SELECT * FROM opportunities")
    fun getAllOpportunities(): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities WHERE id = :id")
    suspend fun getOpportunityById(id: String): OpportunityEntity?

    @Query("SELECT * FROM opportunities WHERE type = :type")
    fun getOpportunitiesByType(type: String): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities WHERE sport = :sport")
    fun getOpportunitiesBySport(sport: String): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities WHERE location = :location")
    fun getOpportunitiesByLocation(location: String): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities WHERE applied = 1")
    fun getAppliedOpportunities(): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities WHERE applied = 0")
    fun getAvailableOpportunities(): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities WHERE deadline >= :currentDate ORDER BY deadline ASC")
    fun getUpcomingOpportunities(currentDate: String): Flow<List<OpportunityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunity(opportunity: OpportunityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunities(opportunities: List<OpportunityEntity>)

    @Update
    suspend fun updateOpportunity(opportunity: OpportunityEntity)

    @Delete
    suspend fun deleteOpportunity(opportunity: OpportunityEntity)

    @Query("UPDATE opportunities SET applied = :applied WHERE id = :id")
    suspend fun updateApplicationStatus(id: String, applied: Boolean)
}

