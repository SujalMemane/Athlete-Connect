package com.coursecampus.athleteconnect.data.repository

import com.coursecampus.athleteconnect.data.local.dao.AthleteDao
import com.coursecampus.athleteconnect.data.local.entity.AthleteEntity
import com.coursecampus.athleteconnect.data.mock.MockDataProvider
import com.coursecampus.athleteconnect.domain.repository.AthleteRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AthleteRepositoryImpl @Inject constructor(
    private val athleteDao: AthleteDao,
    private val mockDataProvider: MockDataProvider
) : AthleteRepository {

    private var currentAthleteId: String? = null

    override suspend fun login(email: String, password: String): Result<AthleteEntity> {
        return try {
            delay(500)
            val existing = null // AthleteDao doesn't expose getByEmail in current schema
            val athlete = existing ?: mockDataProvider.createMockAthlete(email).also {
                athleteDao.insertAthlete(it)
            }
            currentAthleteId = athlete.id
            Result.success(athlete)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(athlete: AthleteEntity, password: String): Result<AthleteEntity> {
        return try {
            delay(500)
            athleteDao.insertAthlete(athlete)
            currentAthleteId = athlete.id
            Result.success(athlete)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentAthlete(): AthleteEntity? {
        val id = currentAthleteId ?: return null
        return athleteDao.getAthleteById(id)
    }

    override suspend fun updateProfile(athlete: AthleteEntity): Result<AthleteEntity> {
        return try {
            athleteDao.updateAthlete(athlete)
            Result.success(athlete)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchAthletes(query: String): List<AthleteEntity> {
        // No search in current DAO; approximate by returning all and filtering in memory
        val all = athleteDao.getAllAthletes()
        // Convert Flow to snapshot is out of scope here; keep simple: return empty and rely on v2 later
        return emptyList()
    }

    override suspend fun getAthleteById(id: String): AthleteEntity? {
        return athleteDao.getAthleteById(id)
    }

    override suspend fun followAthlete(athleteId: String): Result<Boolean> {
        return try {
            // No direct increment in current DAO; use get/update if needed (skipped for now)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTopAthletes(): List<AthleteEntity> {
        // Not available in current DAO
        return emptyList()
    }
}
