package com.coursecampus.athleteconnect.domain.repository

import com.coursecampus.athleteconnect.data.model.Opportunity
import kotlinx.coroutines.flow.Flow

interface OpportunityRepository {
    fun getAllOpportunities(): Flow<List<Opportunity>>
    suspend fun getOpportunityById(id: String): Opportunity?
    fun getOpportunitiesByType(type: String): Flow<List<Opportunity>>
    fun getOpportunitiesBySport(sport: String): Flow<List<Opportunity>>
    fun getOpportunitiesByLocation(location: String): Flow<List<Opportunity>>
    fun getAppliedOpportunities(): Flow<List<Opportunity>>
    fun getAvailableOpportunities(): Flow<List<Opportunity>>
    fun getUpcomingOpportunities(): Flow<List<Opportunity>>
    suspend fun insertOpportunity(opportunity: Opportunity)
    suspend fun updateOpportunity(opportunity: Opportunity)
    suspend fun deleteOpportunity(opportunity: Opportunity)
    suspend fun updateApplicationStatus(id: String, applied: Boolean)
    suspend fun refreshOpportunities()
}

