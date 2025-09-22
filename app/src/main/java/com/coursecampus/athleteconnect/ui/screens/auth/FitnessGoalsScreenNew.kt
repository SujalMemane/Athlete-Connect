package com.coursecampus.athleteconnect.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coursecampus.athleteconnect.R
import com.coursecampus.athleteconnect.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController

data class FitnessGoalNew(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessGoalsScreenNew(
    onContinue: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFFF8F9FA)
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = true
        )
    }

    var primaryGoal by remember { mutableStateOf<FitnessGoalNew?>(null) }
    var secondaryGoals by remember { mutableStateOf(setOf<FitnessGoalNew>()) }

    val fitnessGoals = listOf(
        FitnessGoalNew("Strength", Icons.Default.FitnessCenter, Color(0xFF4CAF50), "Build muscle and power"),
        FitnessGoalNew("Endurance", Icons.Default.DirectionsRun, PrimaryBlue, "Improve cardiovascular fitness"),
        FitnessGoalNew("Flexibility", Icons.Default.SelfImprovement, Color(0xFFFF9800), "Enhance mobility and range"),
        FitnessGoalNew("Speed", Icons.Default.Speed, Color(0xFFE91E63), "Increase pace and agility"),
        FitnessGoalNew("Balance", Icons.Default.Balance, Color(0xFF9C27B0), "Improve stability and coordination"),
        FitnessGoalNew("Recovery", Icons.Default.Spa, Color(0xFF00BCD4), "Focus on rest and rehabilitation")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == 1) 12.dp else 8.dp)
                                .background(
                                    color = if (index <= 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                    shape = CircleShape
                                )
                        )
                        if (index < 2) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = "AthleteConnect Logo",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Text(
                    text = "Your Fitness Goals",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
            item {
                Spacer(modifier = Modifier.height(6.dp))
            }
            item {
                Text(
                    text = "Select your primary goal and any secondary goals",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                // Goals selection card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Primary Goal Section
                        Text(
                            text = "Primary Fitness Goal",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        if (primaryGoal != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = primaryGoal!!.description,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(fitnessGoals) { goal ->
                                GoalCardNew(
                                    goal = goal,
                                    isSelected = primaryGoal == goal,
                                    isPrimary = true,
                                    onClick = {
                                        primaryGoal = if (primaryGoal == goal) null else goal
                                        // Remove from secondary if selected as primary
                                        if (primaryGoal == goal) {
                                            secondaryGoals = secondaryGoals - goal
                                        }
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        // Divider
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(20.dp))
                        // Secondary Goals Section
                        Text(
                            text = "Secondary Fitness Goals",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Select up to 2 additional goals (optional)",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(fitnessGoals.filter { it != primaryGoal }) { goal ->
                                GoalCardNew(
                                    goal = goal,
                                    isSelected = secondaryGoals.contains(goal),
                                    isPrimary = false,
                                    onClick = {
                                        if (secondaryGoals.contains(goal)) {
                                            secondaryGoals = secondaryGoals - goal
                                        } else if (secondaryGoals.size < 2) {
                                            secondaryGoals = secondaryGoals + goal
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                // Progress summary
                if (primaryGoal != null || secondaryGoals.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.05f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "Your Goals Summary:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryBlue
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            primaryGoal?.let { goal ->
                                Text(
                                    text = "• Primary: ${goal.name}",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                            secondaryGoals.forEach { goal ->
                                Text(
                                    text = "• Secondary: ${goal.name}",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        // Continue Button - Always visible at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = onContinue,
                enabled = primaryGoal != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (primaryGoal != null) PrimaryBlue else Color.Gray.copy(alpha = 0.5f),
                    disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (primaryGoal != null) "Continue" else "Select a primary goal to continue",
                    fontSize = if (primaryGoal != null) 18.sp else 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun GoalCardNew(
    goal: FitnessGoalNew,
    isSelected: Boolean,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) goal.color.copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                width = 2.dp,
                color = goal.color
            )
        } else {
            androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (isSelected) goal.color else goal.color.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = goal.icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else goal.color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = goal.name,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) goal.color else Color.Black,
                textAlign = TextAlign.Center
            )

            if (isSelected && isPrimary) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = goal.color,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "PRIMARY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
