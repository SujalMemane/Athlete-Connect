package com.coursecampus.athleteconnect.data.mapper

import com.coursecampus.athleteconnect.data.local.entity.AthleteEntity
import com.coursecampus.athleteconnect.data.model.Athlete

fun AthleteEntity.toDomain(): Athlete {
    return Athlete(
        id = id,
        name = name,
        age = age,
        sport = sport,
        location = location,
        profilePicture = profilePicture,
        coverPhoto = coverPhoto,
        height = height,
        weight = weight,
        personalBests = personalBests,
        testResults = emptyList(), // Will be populated separately
        achievements = achievements,
        videos = videos,
        verified = verified,
        following = following,
        bio = bio,
        email = email,
        phone = phone,
        socialMedia = socialMedia,
        joinDate = joinDate,
        lastActive = lastActive
    )
}

fun Athlete.toEntity(): AthleteEntity {
    return AthleteEntity(
        id = id,
        name = name,
        age = age,
        sport = sport,
        location = location,
        profilePicture = profilePicture,
        coverPhoto = coverPhoto,
        height = height,
        weight = weight,
        personalBests = personalBests,
        achievements = achievements,
        videos = videos,
        verified = verified,
        following = following,
        bio = bio,
        email = email,
        phone = phone,
        socialMedia = socialMedia,
        joinDate = joinDate,
        lastActive = lastActive
    )
}

