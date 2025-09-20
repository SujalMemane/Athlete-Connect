package com.coursecampus.athleteconnect.domain.repository

import com.coursecampus.athleteconnect.data.model.Athlete
import kotlinx.coroutines.flow.Flow

interface AthleteRepository {
    fun getAllAthletes(): Flow<List<Athlete>>
    suspend fun getAthleteById(id: String): Athlete?
    fun getAthletesBySport(sport: String): Flow<List<Athlete>>
    fun getAthletesByLocation(location: String): Flow<List<Athlete>>
    fun getFollowingAthletes(): Flow<List<Athlete>>
    fun getVerifiedAthletes(): Flow<List<Athlete>>
    suspend fun insertAthlete(athlete: Athlete)
    suspend fun updateAthlete(athlete: Athlete)
    suspend fun deleteAthlete(athlete: Athlete)
    suspend fun updateFollowingStatus(id: String, following: Boolean)
    suspend fun refreshAthletes()
}

