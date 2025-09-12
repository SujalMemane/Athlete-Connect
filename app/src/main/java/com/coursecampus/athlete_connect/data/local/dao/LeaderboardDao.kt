package com.coursecampus.athleteconnect.data.local.dao

import androidx.room.*
import com.coursecampus.athleteconnect.data.local.entity.LeaderboardEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaderboardDao {
    @Query("SELECT * FROM leaderboard_entries ORDER BY rank ASC")
    fun getAllLeaderboardEntries(): Flow<List<LeaderboardEntryEntity>>

    @Query("SELECT * FROM leaderboard_entries WHERE testName = :testName ORDER BY rank ASC")
    fun getLeaderboardByTest(testName: String): Flow<List<LeaderboardEntryEntity>>

    @Query("SELECT * FROM leaderboard_entries WHERE sport = :sport ORDER BY rank ASC")
    fun getLeaderboardBySport(sport: String): Flow<List<LeaderboardEntryEntity>>

    @Query("SELECT * FROM leaderboard_entries WHERE location = :location ORDER BY rank ASC")
    fun getLeaderboardByLocation(location: String): Flow<List<LeaderboardEntryEntity>>

    @Query("SELECT * FROM leaderboard_entries WHERE testName = :testName AND sport = :sport ORDER BY rank ASC")
    fun getLeaderboardByTestAndSport(testName: String, sport: String): Flow<List<LeaderboardEntryEntity>>

    @Query("SELECT * FROM leaderboard_entries WHERE rank <= :topN ORDER BY rank ASC")
    fun getTopNLeaderboard(topN: Int): Flow<List<LeaderboardEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardEntry(entry: LeaderboardEntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardEntries(entries: List<LeaderboardEntryEntity>)

    @Update
    suspend fun updateLeaderboardEntry(entry: LeaderboardEntryEntity)

    @Delete
    suspend fun deleteLeaderboardEntry(entry: LeaderboardEntryEntity)

    @Query("DELETE FROM leaderboard_entries")
    suspend fun deleteAllLeaderboardEntries()
}

