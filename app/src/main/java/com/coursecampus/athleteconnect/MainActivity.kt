package com.coursecampus.athleteconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.coursecampus.athleteconnect.ui.navigation.AuthNavigation
import com.coursecampus.athleteconnect.ui.navigation.FitnessLabNavigation
import com.coursecampus.athleteconnect.ui.theme.AthleteConnectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AthleteConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    var isAuthenticated by remember { mutableStateOf(false) } // Set to false to show auth screens
    val navController = rememberNavController()
    
    if (isAuthenticated) {
        FitnessLabNavigation()
    } else {
        AuthNavigation(
            navController = navController,
            onAuthComplete = { isAuthenticated = true }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FitnessLabPreview() {
    AthleteConnectTheme {
        FitnessLabNavigation()
    }
}