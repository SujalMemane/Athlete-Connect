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
fun AboutYourselfScreen(
    onContinue: () -> Unit
) {
    var gender by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var primarySport by remember { mutableStateOf("") }
    var expandedGender by remember { mutableStateOf(false) }
    var expandedSport by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female", "Other")
    val sportOptions = listOf("Football", "Basketball", "Tennis", "Swimming", "Running", "Weightlifting")

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
            progress = 0.33f,
            modifier = Modifier.fillMaxWidth(),
            color = PrimaryBlue,
            trackColor = Color.LightGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "About Yourself",
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
            // Gender Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedGender,
                onExpandedChange = { expandedGender = !expandedGender }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
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
                    expanded = expandedGender,
                    onDismissRequest = { expandedGender = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                expandedGender = false
                            }
                        )
                    }
                }
            }

            // Date of Birth
            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = { Text("Date of Birth") },
                placeholder = { Text("DD/MM/YYYY") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue
                )
            )

            // Height
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height") },
                placeholder = { Text("e.g., 5'8\" or 173 cm") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue
                )
            )

            // Weight
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight") },
                placeholder = { Text("e.g., 70 kg or 154 lbs") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue
                )
            )

            // Primary Sport Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedSport,
                onExpandedChange = { expandedSport = !expandedSport }
            ) {
                OutlinedTextField(
                    value = primarySport,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Primary Sport") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSport) },
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
                    expanded = expandedSport,
                    onDismissRequest = { expandedSport = false }
                ) {
                    sportOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                primarySport = option
                                expandedSport = false
                            }
                        )
                    }
                }
            }

            // Upload Profile Picture
            OutlinedButton(
                onClick = { /* Handle photo upload */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PrimaryBlue
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload Profile Picture")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Continue Button
        Button(
            onClick = onContinue,
            enabled = gender.isNotEmpty() && dateOfBirth.isNotEmpty() && height.isNotEmpty() && weight.isNotEmpty() && primarySport.isNotEmpty(),
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
