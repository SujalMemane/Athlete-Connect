package com.coursecampus.athleteconnect.data.model

enum class Difficulty {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}

data class FitnessTest(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val instructions: List<String>,
    val duration: Int, // in minutes
    val difficulty: Difficulty,
    val icon: String = "",
    val isAvailable: Boolean = true
)
