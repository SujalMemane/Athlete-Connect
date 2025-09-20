package com.coursecampus.athleteconnect.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coursecampus.athleteconnect.ui.theme.*

data class FitnessGoal(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessGoalsScreen(
    onContinue: () -> Unit
) {
    var primaryGoal by remember { mutableStateOf<FitnessGoal?>(null) }
    var secondaryGoals by remember { mutableStateOf(setOf<FitnessGoal>()) }

    val fitnessGoals = listOf(
        FitnessGoal("Strength", Icons.Default.FitnessCenter, FitnessSuccess),
        FitnessGoal("Endurance", Icons.Default.DirectionsRun, PrimaryBlue),
        FitnessGoal("Flexibility", Icons.Default.Straighten, FitnessWarning),
        FitnessGoal("Speed", Icons.Default.Speed, FitnessGold),
        FitnessGoal("Agility", Icons.Default.SwapHoriz, FitnessInfo),
        FitnessGoal("Balance", Icons.Default.Balance, FitnessError)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Progress Bar
        LinearProgressIndicator(
            progress = 0.66f,
            modifier = Modifier.fillMaxWidth(),
            color = PrimaryBlue,
            trackColor = Color.LightGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "Your Fitness Goals",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Primary Goal Section
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Primary Fitness Goal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(fitnessGoals) { goal ->
                    GoalCard(
                        goal = goal,
                        isSelected = primaryGoal == goal,
                        onClick = { 
                            primaryGoal = if (primaryGoal == goal) null else goal
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Secondary Goals Section
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Secondary Fitness Goals",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(fitnessGoals) { goal ->
                    GoalCard(
                        goal = goal,
                        isSelected = secondaryGoals.contains(goal),
                        onClick = { 
                            secondaryGoals = if (secondaryGoals.contains(goal)) {
                                secondaryGoals - goal
                            } else {
                                secondaryGoals + goal
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Continue Button
        Button(
            onClick = onContinue,
            enabled = primaryGoal != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Continue",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun GoalCard(
    goal: FitnessGoal,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) goal.color.copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.Brush.linearGradient(listOf(goal.color, goal.color))
        ) else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = goal.color.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = goal.icon,
                    contentDescription = null,
                    tint = goal.color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = goal.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) goal.color else Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}
