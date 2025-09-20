package com.coursecampus.athleteconnect.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val category: String = "General",
    val rarity: Rarity = Rarity.COMMON,
    val isUnlocked: Boolean = false,
    val unlockedDate: String? = null,
    val athleteId: String
) : Parcelable

enum class Rarity {
    COMMON, RARE, EPIC, LEGENDARY
}
