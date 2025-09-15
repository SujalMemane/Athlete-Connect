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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.coursecampus.athleteconnect.data.model.LeaderboardEntry
import com.coursecampus.athleteconnect.ui.components.LeaderboardCard
import com.coursecampus.athleteconnect.ui.theme.FitnessBronze
import com.coursecampus.athleteconnect.ui.theme.FitnessGold
import com.coursecampus.athleteconnect.ui.theme.FitnessPrimary
import com.coursecampus.athleteconnect.ui.theme.FitnessSecondary
import com.coursecampus.athleteconnect.ui.theme.FitnessSilver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen() {
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedTest by remember { mutableStateOf("All") }

    val filters = listOf("All", "Speed", "Power", "Strength", "Agility")
    val tests = listOf("All", "40 Yard Dash", "Vertical Jump", "Bench Press", "Agility Test")

    val leaderboardEntries = remember {
        listOf(
            LeaderboardEntry(
                athleteId = "1",
                athleteName = "Alex Johnson",
                profilePicture = "",
                rank = 1,
                score = 95.2,
                testName = "40 Yard Dash",
                sport = "Football",
                location = "California",
                verified = true
            ),
            LeaderboardEntry(
                athleteId = "2",
                athleteName = "Sarah Williams",
                profilePicture = "",
                rank = 2,
                score = 92.8,
                testName = "40 Yard Dash",
                sport = "Basketball",
                location = "Texas",
                verified = true
            ),
            LeaderboardEntry(
                athleteId = "3",
                athleteName = "Mike Davis",
                profilePicture = "",
                rank = 3,
                score = 89.5,
                testName = "40 Yard Dash",
                sport = "Football",
                location = "Florida",
                verified = false
            ),
            LeaderboardEntry(
                athleteId = "4",
                athleteName = "Emma Brown",
                profilePicture = "",
                rank = 4,
                score = 87.3,
                testName = "40 Yard Dash",
                sport = "Soccer",
                location = "New York",
                verified = true
            ),
            LeaderboardEntry(
                athleteId = "5",
                athleteName = "David Wilson",
                profilePicture = "",
                rank = 5,
                score = 85.1,
                testName = "40 Yard Dash",
                sport = "Track",
                location = "Oregon",
                verified = false
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Text(
                text = "Leaderboard",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            // Top 3 Podium
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Top Performers",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Podium
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // 2nd Place
                        if (leaderboardEntries.size > 1) {
                            PodiumItem(
                                athlete = leaderboardEntries[1],
                                height = 80.dp,
                                color = FitnessSilver
                            )
                        }

                        // 1st Place
                        PodiumItem(
                            athlete = leaderboardEntries[0],
                            height = 100.dp,
                            color = FitnessGold
                        )

                        // 3rd Place
                        if (leaderboardEntries.size > 2) {
                            PodiumItem(
                                athlete = leaderboardEntries[2],
                                height = 60.dp,
                                color = FitnessBronze
                            )
                        }
                    }
                }
            }
        }

        item {
            // Filters
            Column {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        FilterChip(
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            selected = selectedFilter == filter,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FitnessPrimary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tests) { test ->
                        FilterChip(
                            onClick = { selectedTest = test },
                            label = { Text(test) },
                            selected = selectedTest == test,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FitnessSecondary,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }
                }
            }
        }

        item {
            // Full Leaderboard
            Column {
                Text(
                    text = "Full Rankings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    leaderboardEntries.forEach { entry ->
                        LeaderboardCard(
                            rank = entry.rank,
                            athleteName = entry.athleteName,
                            score = "${entry.score}%",
                            testName = entry.testName
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PodiumItem(
    athlete: LeaderboardEntry,
    height: androidx.compose.ui.unit.Dp,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Athlete Info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = athlete.athleteName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${athlete.score}%",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Podium
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(height)
                .background(
                    color = color,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#${athlete.rank}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

