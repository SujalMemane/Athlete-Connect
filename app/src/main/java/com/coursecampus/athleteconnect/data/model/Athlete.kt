package com.coursecampus.athleteconnect.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Athlete(
    val id: String,
    val name: String,
    val age: Int,
    val sport: String,
    val location: String,
    val profilePicture: String,
    val coverPhoto: String,
    val height: String,
    val weight: String,
    val personalBests: @RawValue Map<String, String>,
    val testResults: @RawValue List<com.coursecampus.athleteconnect.data.model.TestResult>,
    val achievements: List<String>,
    val videos: List<String>,
    val verified: Boolean,
    val following: Boolean,
    val bio: String = "",
    val email: String = "",
    val phone: String = "",
    val socialMedia: @RawValue Map<String, String> = emptyMap(),
    val joinDate: String = "",
    val lastActive: String = ""
) : Parcelable