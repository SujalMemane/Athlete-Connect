package com.coursecampus.athleteconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coursecampus.athleteconnect.ui.theme.*

data class TestHistoryItem(
    val id: String,
    val testName: String,
    val date: String,
    val score: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestHistoryScreen(
    onBackClick: () -> Unit
) {
    val testHistory = remember {
        listOf(
            TestHistoryItem(
                id = "1",
                testName = "Vertical Jump",
                date = "2024-01-15",
                score = "95",
                icon = Icons.Default.FitnessCenter,
                color = PrimaryBlue
            ),
            TestHistoryItem(
                id = "2",
                testName = "Push-up Test",
                date = "2024-01-14",
                score = "88",
                icon = Icons.Default.DirectionsRun,
                color = FitnessSuccess
            ),
            TestHistoryItem(
                id = "3",
                testName = "Sprint Test",
                date = "2024-01-13",
                score = "92",
                icon = Icons.Default.Speed,
                color = FitnessWarning
            ),
            TestHistoryItem(
                id = "4",
                testName = "Flexibility Test",
                date = "2024-01-12",
                score = "85",
                icon = Icons.Default.Straighten,
                color = FitnessGold
            ),
            TestHistoryItem(
                id = "5",
                testName = "Balance Test",
                date = "2024-01-11",
                score = "90",
                icon = Icons.Default.Balance,
                color = FitnessInfo
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        TopAppBar(
            title = { 
                Text(
                    text = "Test History",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        // Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(testHistory) { test ->
                TestHistoryCard(
                    test = test,
                    onClick = { /* Navigate to test details */ }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp)) // Bottom padding
            }
        }
    }
}

@Composable
fun TestHistoryCard(
    test: TestHistoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FitnessSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Test Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = test.color.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = test.icon,
                    contentDescription = test.testName,
                    tint = test.color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Test Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = test.testName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = FitnessOnSurface
                )
                Text(
                    text = test.date,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
            
            // Score
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = test.score,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = test.color
                )
                Text(
                    text = "Score",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }
    }
}
