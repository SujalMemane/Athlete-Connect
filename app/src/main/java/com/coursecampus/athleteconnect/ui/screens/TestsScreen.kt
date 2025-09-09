package com.coursecampus.athleteconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.coursecampus.athleteconnect.data.model.*
import com.coursecampus.athleteconnect.ui.components.*
import com.coursecampus.athleteconnect.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestsScreen() {
    val fitnessTests = remember {
        listOf(
            FitnessTest(
                id = "1",
                name = "40 Yard Dash",
                description = "Measure your sprint speed over 40 yards",
                category = "Speed",
                instructions = listOf(
                    "Stand at the starting line",
                    "Sprint as fast as possible for 40 yards",
                    "Record your time"
                ),
                duration = 10,
                difficulty = Difficulty.INTERMEDIATE
            ),
            FitnessTest(
                id = "2",
                name = "Vertical Jump",
                description = "Test your explosive leg power",
                category = "Power",
                instructions = listOf(
                    "Stand with feet shoulder-width apart",
                    "Jump as high as possible",
                    "Reach for the highest point"
                ),
                duration = 5,
                difficulty = Difficulty.BEGINNER
            ),
            FitnessTest(
                id = "3",
                name = "Bench Press",
                description = "Measure your upper body strength",
                category = "Strength",
                instructions = listOf(
                    "Lie on bench with feet flat",
                    "Lower bar to chest",
                    "Press up explosively"
                ),
                duration = 15,
                difficulty = Difficulty.ADVANCED
            ),
            FitnessTest(
                id = "4",
                name = "Agility Test",
                description = "Test your quickness and change of direction",
                category = "Agility",
                instructions = listOf(
                    "Set up cones in T-shape",
                    "Run through the course",
                    "Record your time"
                ),
                duration = 20,
                difficulty = Difficulty.INTERMEDIATE
            )
        )
    }

    val recentResults = remember {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fitness Tests",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                IconButton(onClick = { /* Camera test */ }) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Start Camera Test",
                        tint = FitnessPrimary
                    )
                }
            }
        }

        item {
            // Available Tests
            Column {
                Text(
                    text = "Available Tests",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(fitnessTests) { test ->
                        TestCard(test = test)
                    }
                }
            }
        }

        item {
            // Recent Results
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Results",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { /* View all results */ }) {
                        Text("View All")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    recentResults.forEach { result ->
                        TestResultCard(testResult = result)
                    }
                }
            }
        }
    }
}

@Composable
fun TestCard(
    test: FitnessTest,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Category Badge
            Box(
                modifier = Modifier
                    .background(
                        color = getCategoryColor(test.category).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = test.category,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = getCategoryColor(test.category)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = test.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = test.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${test.duration}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Button(
                    onClick = onClick,
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FitnessPrimary
                    )
                ) {
                    Text(
                        text = "Start",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun getCategoryColor(category: String): Color {
    return when (category) {
        "Speed" -> FitnessPrimary
        "Power" -> FitnessSecondary
        "Strength" -> FitnessError
        "Agility" -> FitnessInfo
        else -> MaterialTheme.colorScheme.primary
    }
}

