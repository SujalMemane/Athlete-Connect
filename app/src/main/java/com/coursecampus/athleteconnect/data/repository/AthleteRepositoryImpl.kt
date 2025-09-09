package com.coursecampus.athleteconnect.data.repository

import com.coursecampus.athleteconnect.data.local.dao.AthleteDao
import com.coursecampus.athleteconnect.data.mapper.toDomain
import com.coursecampus.athleteconnect.data.mapper.toEntity
import com.coursecampus.athleteconnect.data.model.Athlete
import com.coursecampus.athleteconnect.domain.repository.AthleteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AthleteRepositoryImpl @Inject constructor(
    private val athleteDao: AthleteDao
) : AthleteRepository {

    override fun getAllAthletes(): Flow<List<Athlete>> {
        return athleteDao.getAllAthletes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAthleteById(id: String): Athlete? {
        return athleteDao.getAthleteById(id)?.toDomain()
    }

    override fun getAthletesBySport(sport: String): Flow<List<Athlete>> {
        return athleteDao.getAthletesBySport(sport).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAthletesByLocation(location: String): Flow<List<Athlete>> {
        return athleteDao.getAthletesByLocation(location).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFollowingAthletes(): Flow<List<Athlete>> {
        return athleteDao.getFollowingAthletes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getVerifiedAthletes(): Flow<List<Athlete>> {
        return athleteDao.getVerifiedAthletes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertAthlete(athlete: Athlete) {
        athleteDao.insertAthlete(athlete.toEntity())
    }

    override suspend fun updateAthlete(athlete: Athlete) {
        athleteDao.updateAthlete(athlete.toEntity())
    }

    override suspend fun deleteAthlete(athlete: Athlete) {
        athleteDao.deleteAthlete(athlete.toEntity())
    }

    override suspend fun updateFollowingStatus(id: String, following: Boolean) {
        athleteDao.updateFollowingStatus(id, following)
    }

    override suspend fun refreshAthletes() {
        // In a real app, this would fetch from API and update local database
        // For now, we'll just use local data
    }
}

