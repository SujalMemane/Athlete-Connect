package com.coursecampus.athleteconnect.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coursecampus.athleteconnect.R
import com.coursecampus.athleteconnect.ui.theme.FitnessPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToAuth: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 2 seconds
        onNavigateToAuth()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FitnessPrimary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Athlete Connect Logo",
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App Name
            Text(
                text = "Athlete Connect",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Tagline
            Text(
                text = "Connect • Compete • Excel",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}
