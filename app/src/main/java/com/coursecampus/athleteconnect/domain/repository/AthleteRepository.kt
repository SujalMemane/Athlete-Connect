package com.coursecampus.athleteconnect.domain.repository

import com.coursecampus.athleteconnect.data.local.entity.AthleteEntity

interface AthleteRepository {
    suspend fun login(email: String, password: String): Result<AthleteEntity>
    suspend fun register(athlete: AthleteEntity, password: String): Result<AthleteEntity>
    suspend fun getCurrentAthlete(): AthleteEntity?
    suspend fun updateProfile(athlete: AthleteEntity): Result<AthleteEntity>
    suspend fun searchAthletes(query: String): List<AthleteEntity>
    suspend fun getAthleteById(id: String): AthleteEntity?
    suspend fun followAthlete(athleteId: String): Result<Boolean>
    suspend fun getTopAthletes(): List<AthleteEntity>
}
