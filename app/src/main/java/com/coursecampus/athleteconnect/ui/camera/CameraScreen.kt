package com.coursecampus.athleteconnect.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.coursecampus.athleteconnect.data.model.FitnessTest
import com.coursecampus.athleteconnect.data.model.TestResult
import com.coursecampus.athleteconnect.ui.theme.FitnessPrimary
import com.coursecampus.athleteconnect.ui.theme.FitnessError
import com.coursecampus.athleteconnect.ui.theme.FitnessSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    fitnessTest: FitnessTest,
    onBackClick: () -> Unit,
    onTestComplete: (TestResult) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    // Test execution state
    var isTestRunning by remember { mutableStateOf(false) }
    var testStartTime by remember { mutableStateOf(0L) }
    var testDuration by remember { mutableStateOf(0L) }
    var testScore by remember { mutableStateOf(0.0) }
    var testReps by remember { mutableStateOf(0) }
    var currentPhase by remember { mutableStateOf("Ready") }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }
    
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }
    
    // Test execution functions
    fun startTest() {
        isTestRunning = true
        testStartTime = System.currentTimeMillis()
        currentPhase = "Running"
        testReps = 0
        testScore = 0.0
    }
    
    fun stopTest() {
        isTestRunning = false
        testDuration = System.currentTimeMillis() - testStartTime
        currentPhase = "Completed"
        
        // Calculate score based on test type
        val score = when (fitnessTest.category) {
            "Speed" -> (testDuration / 1000.0) // Time in seconds
            "Power" -> testReps * 2.5 // Reps * multiplier
            "Strength" -> testReps * 1.0 // Direct rep count
            "Core" -> (testDuration / 1000.0) // Time in seconds
            else -> testReps * 1.0
        }
        
        testScore = score
        
        // Create test result
        val result = TestResult(
            id = System.currentTimeMillis().toString(),
            testName = fitnessTest.name,
            score = testScore,
            unit = when (fitnessTest.category) {
                "Speed" -> "seconds"
                "Power", "Strength" -> "reps"
                "Core" -> "seconds"
                else -> "score"
            },
            date = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
            percentile = (70..95).random(), // Mock percentile
            category = fitnessTest.category
        )
        
        onTestComplete(result)
    }
    
    fun resetTest() {
        isTestRunning = false
        testStartTime = 0L
        testDuration = 0L
        testScore = 0.0
        testReps = 0
        currentPhase = "Ready"
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = fitnessTest.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
        
        if (hasCameraPermission) {
            // Camera Preview
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    lifecycleOwner = lifecycleOwner
                )
                
                // Test Instructions Overlay
                Card(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Instructions:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        fitnessTest.instructions.forEach { instruction ->
                            Text(
                                text = "• $instruction",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }
                }
                
                // Recording Indicator
                if (false) { // Replace with actual recording state
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = FitnessError
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color.White, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "REC",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            
            // Test Controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Test Status
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentPhase,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        if (isTestRunning) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Time: ${(System.currentTimeMillis() - testStartTime) / 1000}s",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            Text(
                                text = "Reps: $testReps",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Control Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Reset Button
                    FloatingActionButton(
                        onClick = { resetTest() },
                        modifier = Modifier.size(56.dp),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset"
                        )
                    }
                    
                    // Start/Stop Button
                    FloatingActionButton(
                        onClick = { 
                            if (isTestRunning) {
                                stopTest()
                            } else {
                                startTest()
                            }
                        },
                        modifier = Modifier.size(80.dp),
                        containerColor = if (isTestRunning) FitnessError else FitnessPrimary,
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = if (isTestRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (isTestRunning) "Stop" else "Start",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    // Add Rep Button (for rep-based tests)
                    if (fitnessTest.category in listOf("Power", "Strength")) {
                        FloatingActionButton(
                            onClick = { 
                                if (isTestRunning) {
                                    testReps++
                                }
                            },
                            modifier = Modifier.size(56.dp),
                            containerColor = FitnessSecondary,
                            contentColor = Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Rep"
                            )
                        }
                    } else {
                        // Switch Camera Button for other tests
                        FloatingActionButton(
                            onClick = { /* Switch camera */ },
                            modifier = Modifier.size(56.dp),
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cameraswitch,
                                contentDescription = "Switch Camera"
                            )
                        }
                    }
                }
            }
        } else {
            // Permission Denied
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                        modifier = Modifier.size(64.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Camera Permission Required",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please grant camera permission to record fitness tests",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { launcher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FitnessPrimary
                        )
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(
    modifier: Modifier = Modifier,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner
) {
    AndroidView(
        factory = { context ->
            // This is a placeholder for the actual CameraX implementation
            // In a real app, you would use PreviewView from CameraX
            androidx.compose.ui.platform.ComposeView(context).apply {
                setContent {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera Preview",
                                modifier = Modifier.size(64.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Camera Preview",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                            Text(
                                text = "CameraX integration would go here",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    )
}

