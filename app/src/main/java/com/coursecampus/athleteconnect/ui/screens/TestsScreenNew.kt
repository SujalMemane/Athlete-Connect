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
fun TestsScreenNew(
    navController: NavController,
    viewModel: TestsViewModel = hiltViewModel()
) {
    val fitnessTests by viewModel.fitnessTests.collectAsState()
    val recentResults by viewModel.recentResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val analysisMessage by viewModel.analysisMessage.collectAsState()
    
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Speed", "Power", "Strength", "Core", "Agility")
    
    val filteredTests = if (selectedCategory == "All") {
        fitnessTests
    } else {
        fitnessTests.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp), // reduced horizontal padding
        verticalArrangement = Arrangement.spacedBy(12.dp) // reduced spacing
    ) {
        item {
            // Header
            Column(
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp) // reduced padding
            ) {
                Text(
                    text = "Fitness Tests",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Choose a test to assess your performance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)) // reduced padding
                }
                if (!analysisMessage.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp)) // reduced Spacer
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = analysisMessage ?: "",
                            modifier = Modifier.padding(8.dp), // reduced padding
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        item {
            // Category Filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        selected = selectedCategory == category,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FitnessPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        item {
            // Test Categories
            Text(
                text = "Available Tests",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(filteredTests) { test ->
            TestCard(
                test = test,
                onClick = {
                    // Navigate to test detail screen first
                    navController.navigate("test_detail/${test.id}")
                }
            )
        }

        if (recentResults.isNotEmpty()) {
            item {
                // Recent Results Section
                Text(
                    text = "Recent Results",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp) // reduced padding
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp) // reduced spacing
                ) {
                    recentResults.take(5).forEach { result ->
                        TestResultCard(
                            result = result,
                            onClick = {
                                // Show detailed result
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TestCard(
    test: FitnessTest,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp) // reduced padding
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = test.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp)) // reduced Spacer
                    Text(
                        text = test.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start Test",
                    tint = FitnessPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp)) // reduced Spacer

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Category Badge
                Surface(
                    color = FitnessPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = test.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), // reduced padding
                        style = MaterialTheme.typography.labelMedium,
                        color = FitnessPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Difficulty Badge
                Surface(
                    color = when (test.difficulty) {
                        Difficulty.BEGINNER -> Color.Green.copy(alpha = 0.1f)
                        Difficulty.INTERMEDIATE -> Color(0xFFFF9800).copy(alpha = 0.1f)
                        Difficulty.ADVANCED -> Color.Red.copy(alpha = 0.1f)
                        Difficulty.EXPERT -> Color(0xFF9C27B0).copy(alpha = 0.1f)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = test.difficulty.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), // reduced padding
                        style = MaterialTheme.typography.labelMedium,
                        color = when (test.difficulty) {
                            Difficulty.BEGINNER -> Color.Green
                            Difficulty.INTERMEDIATE -> Color(0xFFFF9800)
                            Difficulty.ADVANCED -> Color.Red
                            Difficulty.EXPERT -> Color(0xFF9C27B0)
                        },
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Duration
                Text(
                    text = "${test.duration} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun TestResultCard(
    result: TestResult,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = result.testName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${result.score} ${result.unit} • ${result.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${result.percentile}th percentile",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = FitnessPrimary
                )
                if (result.isPersonalBest) {
                    Surface(
                        color = Color(0xFFFFD700).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "PB",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
