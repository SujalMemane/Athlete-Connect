package com.coursecampus.athleteconnect.data.mapper

import com.coursecampus.athleteconnect.data.local.entity.TestResultEntity
import com.coursecampus.athleteconnect.data.model.TestResult

fun TestResultEntity.toDomain(): TestResult {
    return TestResult(
        id = id,
        testName = testName,
        score = score,
        unit = unit,
        date = date,
        percentile = percentile,
        category = category,
        videoUrl = videoUrl,
        notes = notes
    )
}

fun TestResult.toEntity(): TestResultEntity {
    return TestResultEntity(
        id = id,
        athleteId = "", // Will be set when saving
        testName = testName,
        score = score,
        unit = unit,
        date = date,
        percentile = percentile,
        category = category,
        videoUrl = videoUrl,
        notes = notes
    )
}

