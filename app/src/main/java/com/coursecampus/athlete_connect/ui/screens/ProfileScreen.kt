package com.coursecampus.athleteconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.coursecampus.athleteconnect.ui.theme.FitnessPrimary
import com.coursecampus.athleteconnect.ui.theme.GradientEnd
import com.coursecampus.athleteconnect.ui.theme.GradientStart
import com.coursecampus.athleteconnect.data.model.Achievement
import com.coursecampus.athleteconnect.data.model.Athlete
import com.coursecampus.athleteconnect.data.model.Rarity
import com.coursecampus.athleteconnect.ui.components.AchievementCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val currentUser = remember {
        Athlete(
            id = "current_user",
            name = "John Smith",
            age = 22,
            sport = "Basketball",
            location = "Los Angeles, CA",
            profilePicture = "https://example.com/profile.jpg",
            coverPhoto = "https://example.com/cover.jpg",
            height = "6'2\"",
            weight = "185 lbs",
            personalBests = mapOf(
                "40 Yard Dash" to "4.6s",
                "Vertical Jump" to "36\"",
                "Bench Press" to "225 lbs"
            ),
            testResults = emptyList(),
            achievements = listOf(
                "Speed Demon",
                "Jump Master",
                "Strength Champion"
            ),
            videos = listOf(
                "https://example.com/video1.mp4",
                "https://example.com/video2.mp4"
            ),
            verified = true,
            following = false,
            bio = "Passionate basketball player with 8 years of experience. Always looking to improve and compete at the highest level."
        )
    }

    val achievements = remember {
        listOf(
            Achievement(
                id = "1",
                title = "Speed Demon",
                description = "Complete 10 speed tests with 90%+ percentile",
                icon = "🏃‍♂️",
                category = "Speed",
                unlockedDate = "2024-01-10",
                rarity = Rarity.RARE,
                athleteId = "current_user"
            ),
            Achievement(
                id = "2",
                title = "Jump Master",
                description = "Achieve 35+ inch vertical jump",
                icon = "🏃‍♂️",
                category = "Power",
                unlockedDate = "2024-01-08",
                rarity = Rarity.EPIC,
                athleteId = "current_user"
            ),
            Achievement(
                id = "3",
                title = "Strength Champion",
                description = "Bench press 200+ lbs",
                icon = "🏃‍♂️",
                category = "Strength",
                unlockedDate = "2024-01-05",
                rarity = Rarity.COMMON,
                athleteId = "current_user"
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            // Profile Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                // Cover Photo
                AsyncImage(
                    model = currentUser.coverPhoto,
                    contentDescription = "Cover photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(GradientStart, GradientEnd)
                            )
                        ),
                    contentScale = ContentScale.Crop
                )

                // Profile Picture
                AsyncImage(
                    model = currentUser.profilePicture,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .align(Alignment.BottomCenter)
                        .offset(y = 60.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                )

                // Edit Button
                FloatingActionButton(
                    onClick = { /* Edit profile */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile"
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }

        item {
            // Profile Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentUser.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "${currentUser.sport} • ${currentUser.location}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                if (currentUser.bio.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentUser.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        label = "Age",
                        value = currentUser.age.toString()
                    )
                    StatItem(
                        label = "Height",
                        value = currentUser.height
                    )
                    StatItem(
                        label = "Weight",
                        value = currentUser.weight
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Personal Bests
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Personal Bests",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(currentUser.personalBests.entries.toList()) { (test, result) ->
                        Card(
                            modifier = Modifier.width(140.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = test,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = result,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FitnessPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Achievements
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Achievements",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    achievements.forEach { achievement ->
                        AchievementCard(
                            title = achievement.title,
                            description = achievement.description,
                            icon = when (achievement.category) {
                                "Speed" -> Icons.Default.Speed
                                "Power" -> Icons.Default.FitnessCenter
                                "Strength" -> Icons.Default.Sports
                                else -> Icons.Default.Star
                            },
                            isUnlocked = true
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

