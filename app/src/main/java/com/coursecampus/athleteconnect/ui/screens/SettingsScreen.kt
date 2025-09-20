package com.coursecampus.athleteconnect.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coursecampus.athleteconnect.ui.theme.*

data class SettingsItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val settingsItems = remember {
        listOf(
            SettingsItem(
                title = "Account Settings",
                subtitle = "Manage your profile and preferences",
                icon = Icons.Default.Person,
                onClick = { /* Navigate to account settings */ }
            ),
            SettingsItem(
                title = "Notifications",
                subtitle = "Configure notification preferences",
                icon = Icons.Default.Notifications,
                onClick = { /* Navigate to notifications */ }
            ),
            SettingsItem(
                title = "Privacy & Security",
                subtitle = "Control your data and privacy",
                icon = Icons.Default.Security,
                onClick = { /* Navigate to privacy */ }
            ),
            SettingsItem(
                title = "Test Preferences",
                subtitle = "Customize your test experience",
                icon = Icons.Default.Settings,
                onClick = { /* Navigate to test preferences */ }
            ),
            SettingsItem(
                title = "Help & Support",
                subtitle = "Get help and contact support",
                icon = Icons.Default.Help,
                onClick = { /* Navigate to help */ }
            ),
            SettingsItem(
                title = "About",
                subtitle = "App version and information",
                icon = Icons.Default.Info,
                onClick = { /* Navigate to about */ }
            ),
            SettingsItem(
                title = "Logout",
                subtitle = "Sign out of your account",
                icon = Icons.Default.ExitToApp,
                onClick = { /* Handle logout */ }
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
                    text = "Settings",
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(settingsItems) { item ->
                SettingsItemCard(item = item)
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp)) // Bottom padding
            }
        }
    }
}

@Composable
fun SettingsItemCard(
    item: SettingsItem
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FitnessSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = item.onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (item.title == "Logout") FitnessError.copy(alpha = 0.1f) 
                               else PrimaryBlue.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (item.title == "Logout") FitnessError else PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (item.title == "Logout") FitnessError else FitnessOnSurface
                )
                Text(
                    text = item.subtitle,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
            
            // Arrow
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
