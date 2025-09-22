package com.coursecampus.athleteconnect.ui.screens.auth

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.coursecampus.athleteconnect.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AboutYourselfScreen(
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

    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var primarySport by remember { mutableStateOf("") }
    var customSport by remember { mutableStateOf("") }
    var expandedGender by remember { mutableStateOf(false) }
    var expandedSport by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var requestGalleryAfterPermission by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female", "Other")
    val sportOptions = listOf("Football", "Basketball", "Tennis", "Swimming", "Running", "Weightlifting", "Other")

    val galleryPermissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    LaunchedEffect(galleryPermissionState.status.isGranted, requestGalleryAfterPermission) {
        if (galleryPermissionState.status.isGranted && requestGalleryAfterPermission) {
            galleryLauncher.launch("image/*")
            requestGalleryAfterPermission = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
            item {
                LinearProgressIndicator(
                    progress = 0.33f,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                Text(
                    text = "About Yourself",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
            item {
                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        focusedLabelColor = PrimaryBlue
                    ),
                    singleLine = true
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
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

                    // location Field
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("location", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            focusedLabelColor = PrimaryBlue
                        ),
                        singleLine = true
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
                                        if (option != "Other") customSport = ""
                                    }
                                )
                            }
                        }
                    }
                    // Show custom sport input if 'Other' is selected
                    if (primarySport == "Other") {
                        OutlinedTextField(
                            value = customSport,
                            onValueChange = { customSport = it },
                            label = { Text("Please specify your sport") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                focusedLabelColor = PrimaryBlue
                            )
                        )
                    }

                    // Upload Profile Picture
                    OutlinedButton(
                        onClick = {
                            if (galleryPermissionState.status.isGranted) {
                                galleryLauncher.launch("image/*")
                            } else {
                                requestGalleryAfterPermission = true
                                galleryPermissionState.launchPermissionRequest()
                            }
                        },
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
                    // Show selected image
                    selectedImageUri?.let { uri ->
                        Spacer(modifier = Modifier.height(12.dp))
                        AsyncImage(
                            model = uri,
                            contentDescription = "Selected Profile Picture",
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.CenterHorizontally)
                                .background(Color.White, shape = RoundedCornerShape(16.dp))
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                // Continue Button
                Button(
                    onClick = onContinue,
                    enabled = name.isNotEmpty() && gender.isNotEmpty() && dateOfBirth.isNotEmpty() && height.isNotEmpty() && weight.isNotEmpty() && ((primarySport.isNotEmpty() && primarySport != "Other") || (primarySport == "Other" && customSport.isNotEmpty())),
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
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
