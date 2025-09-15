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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.coursecampus.athleteconnect.data.model.LeaderboardEntry
import com.coursecampus.athleteconnect.data.model.Stats
import com.coursecampus.athleteconnect.data.model.TestResult
import com.coursecampus.athleteconnect.ui.components.LeaderboardCard
import com.coursecampus.athleteconnect.ui.components.StatsCard
import com.coursecampus.athleteconnect.ui.components.TestResultCard
import com.coursecampus.athleteconnect.ui.theme.GradientEnd
import com.coursecampus.athleteconnect.ui.theme.GradientStart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val stats = remember {
        Stats(
            totalAthletes = 1250,
            testsCompleted = 3450,
            opportunitiesPosted = 89,
            messagesSent = 567,
            averageScore = 78.5,
            topSport = "Basketball",
            activeUsers = 892
        )
    }

    val recentTests = remember {
        listOf(
            TestResult(
                id = "1",
                testName = "40 Yard Dash",
                score = 4.8,
                unit = "seconds",
                date = "2024-01-15",
                percentile = 85,
                category = "Speed"
            ),
            TestResult(
                id = "2",
                testName = "Vertical Jump",
                score = 32.5,
                unit = "inches",
                date = "2024-01-14",
                percentile = 72,
                category = "Power"
            ),
            TestResult(
                id = "3",
                testName = "Bench Press",
                score = 185.0,
                unit = "lbs",
                date = "2024-01-13",
                percentile = 68,
                category = "Strength"
            )
        )
    }

    val topPerformers = remember {
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
                testName = "Vertical Jump",
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
                testName = "Bench Press",
                sport = "Football",
                location = "Florida",
                verified = false
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            // Welcome Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(GradientStart, GradientEnd)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Welcome to Athlete Connect",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ready to take your performance to the next level?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        item {
            // Stats Grid
            Column {
                Text(
                    text = "Platform Statistics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        StatsCard(
                            title = "Total Athletes",
                            value = stats.totalAthletes.toString(),
                            icon = Icons.Default.People,
                            modifier = Modifier.width(160.dp)
                        )
                    }
                    item {
                        StatsCard(
                            title = "Tests Completed",
                            value = stats.testsCompleted.toString(),
                            icon = Icons.Default.FitnessCenter,
                            modifier = Modifier.width(160.dp)
                        )
                    }
                    item {
                        StatsCard(
                            title = "Opportunities",
                            value = stats.opportunitiesPosted.toString(),
                            icon = Icons.Default.Work,
                            modifier = Modifier.width(160.dp)
                        )
                    }
                }
            }
        }

        item {
            // Recent Test Results
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Test Results",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { /* Navigate to tests */ }) {
                        Text("View All")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    recentTests.forEach { testResult ->
                        TestResultCard(testResult = testResult)
                    }
                }
            }
        }

        item {
            // Top Performers
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Top Performers",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { /* Navigate to leaderboard */ }) {
                        Text("View All")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    topPerformers.forEach { performer ->
                        LeaderboardCard(
                            rank = performer.rank,
                            athleteName = performer.athleteName,
                            score = "${performer.score}%",
                            testName = performer.testName
                        )
                    }
                }
            }
        }
    }
}
