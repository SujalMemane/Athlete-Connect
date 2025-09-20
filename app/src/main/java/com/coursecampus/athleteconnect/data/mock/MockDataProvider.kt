package com.coursecampus.athleteconnect.data.mock

import com.coursecampus.athleteconnect.data.local.entity.AthleteEntity
import com.coursecampus.athleteconnect.data.local.entity.FitnessTestEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockDataProvider @Inject constructor() {

    fun createMockAthlete(email: String): AthleteEntity {
        val username = email.substringBefore("@")
        return AthleteEntity(
            id = email,
            name = username.replaceFirstChar { it.uppercase() } + " Athlete",
            age = 20,
            sport = "Track",
            location = "San Francisco, CA",
            profilePicture = "",
            coverPhoto = "",
            height = "180 cm",
            weight = "75 kg",
            personalBests = mapOf("40yd" to "4.8s"),
            achievements = listOf("Regional finalist"),
            videos = emptyList(),
            verified = true,
            following = false,
            bio = "Determined multi-sport athlete",
            email = email,
            phone = "",
            socialMedia = mapOf("instagram" to "@$username"),
            joinDate = "2024-01-01",
            lastActive = "2025-01-01"
        )
    }

    fun getMockFitnessTests(): List<FitnessTestEntity> = listOf(
        FitnessTestEntity(
            id = "sprint_40",
            name = "40-Yard Sprint",
            description = "Measure straight-line acceleration",
            category = "SPEED",
            instructions = listOf(
                "Warm up with dynamic stretches",
                "Start behind the line",
                "Sprint 40 yards as fast as possible"
            ),
            duration = 5,
            videoUrl = "",
            imageUrl = "",
            difficulty = "INTERMEDIATE"
        ),
        FitnessTestEntity(
            id = "vertical_jump",
            name = "Vertical Jump",
            description = "Assess lower-body power",
            category = "POWER",
            instructions = listOf("Stand next to wall", "Jump and mark reach"),
            duration = 3,
            videoUrl = "",
            imageUrl = "",
            difficulty = "ADVANCED"
        )
    )
}


