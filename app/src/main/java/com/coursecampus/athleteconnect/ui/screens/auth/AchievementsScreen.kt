package com.coursecampus.athleteconnect.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coursecampus.athleteconnect.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    onComplete: () -> Unit
) {
    var currentFitnessLevel by remember { mutableStateOf("") }
    var trainingFrequency by remember { mutableStateOf("") }
    var trainingDuration by remember { mutableStateOf("") }
    var medicalConditions by remember { mutableStateOf("") }
    var expandedLevel by remember { mutableStateOf(false) }
    var expandedFrequency by remember { mutableStateOf(false) }
    var expandedDuration by remember { mutableStateOf(false) }

    val fitnessLevels = listOf("Beginner", "Intermediate", "Advanced", "Professional")
    val frequencies = listOf("1-2 times", "3-4 times", "5-6 times", "Daily")
    val durations = listOf("15-30 minutes", "30-45 minutes", "45-60 minutes", "60+ minutes")

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
            progress = 1.0f,
            modifier = Modifier.fillMaxWidth(),
            color = PrimaryBlue,
            trackColor = Color.LightGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "What do you want to achieve?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Form Fields
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current Fitness Level Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedLevel,
                onExpandedChange = { expandedLevel = !expandedLevel }
            ) {
                OutlinedTextField(
                    value = currentFitnessLevel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Your current fitness level") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLevel) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        focusedLabelColor = PrimaryBlue
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedLevel,
                    onDismissRequest = { expandedLevel = false }
                ) {
                    fitnessLevels.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                currentFitnessLevel = option
                                expandedLevel = false
                            }
                        )
                    }
                }
            }

            // Training Frequency Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedFrequency,
                onExpandedChange = { expandedFrequency = !expandedFrequency }
            ) {
                OutlinedTextField(
                    value = trainingFrequency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("How many times a week do you want to train?") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFrequency) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        focusedLabelColor = PrimaryBlue
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedFrequency,
                    onDismissRequest = { expandedFrequency = false }
                ) {
                    frequencies.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                trainingFrequency = option
                                expandedFrequency = false
                            }
                        )
                    }
                }
            }

            // Training Duration Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedDuration,
                onExpandedChange = { expandedDuration = !expandedDuration }
            ) {
                OutlinedTextField(
                    value = trainingDuration,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("What is your preferred training duration?") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDuration) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        focusedLabelColor = PrimaryBlue
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedDuration,
                    onDismissRequest = { expandedDuration = false }
                ) {
                    durations.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                trainingDuration = option
                                expandedDuration = false
                            }
                        )
                    }
                }
            }

            // Medical Conditions
            OutlinedTextField(
                value = medicalConditions,
                onValueChange = { medicalConditions = it },
                label = { Text("Do you have any injuries or medical conditions?") },
                placeholder = { Text("Optional - Describe any relevant conditions") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Go to Home Button
        Button(
            onClick = onComplete,
            enabled = currentFitnessLevel.isNotEmpty() && trainingFrequency.isNotEmpty() && trainingDuration.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Go to Home",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
