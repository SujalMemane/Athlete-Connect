package com.coursecampus.athleteconnect.data.local.dao

import androidx.room.*
import com.coursecampus.athleteconnect.data.local.entity.AthleteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AthleteDao {
    @Query("SELECT * FROM athletes")
    fun getAllAthletes(): Flow<List<AthleteEntity>>

    @Query("SELECT * FROM athletes WHERE id = :id")
    suspend fun getAthleteById(id: String): AthleteEntity?

    @Query("SELECT * FROM athletes WHERE sport = :sport")
    fun getAthletesBySport(sport: String): Flow<List<AthleteEntity>>

    @Query("SELECT * FROM athletes WHERE location = :location")
    fun getAthletesByLocation(location: String): Flow<List<AthleteEntity>>

    @Query("SELECT * FROM athletes WHERE following = 1")
    fun getFollowingAthletes(): Flow<List<AthleteEntity>>

    @Query("SELECT * FROM athletes WHERE verified = 1")
    fun getVerifiedAthletes(): Flow<List<AthleteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athlete: AthleteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthletes(athletes: List<AthleteEntity>)

    @Update
    suspend fun updateAthlete(athlete: AthleteEntity)

    @Delete
    suspend fun deleteAthlete(athlete: AthleteEntity)

    @Query("DELETE FROM athletes")
    suspend fun deleteAllAthletes()

    @Query("UPDATE athletes SET following = :following WHERE id = :id")
    suspend fun updateFollowingStatus(id: String, following: Boolean)
}

