package com.coursecampus.athleteconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.coursecampus.athleteconnect.data.model.Achievement
import com.coursecampus.athleteconnect.data.model.Athlete
import com.coursecampus.athleteconnect.data.model.Rarity
import com.coursecampus.athleteconnect.ui.theme.PrimaryBlue

@Composable
fun ProfileScreenNew(
    navController: NavController = rememberNavController()
) {
    val currentUser = remember {
        Athlete(
            id = "current_user",
            name = "Ramesh Yadav",
            age = 36,
            sport = "Basketball",
            location = "Delhi, India",
            profilePicture = "profile_ramesh", // resource name
            coverPhoto = "",
            height = "5'9\"",
            weight = "72 kg",
            personalBests = mapOf(
                "Yo-Yo Test" to "18.5",
                "Beep Test" to "Level 15",
                "Bench Press" to "110 kg"
            ),
            testResults = emptyList(),
            achievements = listOf(
                "National Basketball Champion",
                "MVP Delhi League",
                "Padma Shri"
            ),
            videos = emptyList(),
            verified = true,
            following = false,
            bio = "Indian basketball player, passionate about fitness and performance. Leading by example on and off the court."
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            // Blue header with edit button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                // Edit button
                IconButton(
                    onClick = { /* Edit profile */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit, 
                        contentDescription = "Edit Profile",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            // Profile card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-40).dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile photo from resources
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Photo",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = currentUser.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${currentUser.sport} • ${currentUser.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentUser.bio,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(label = "Height", value = currentUser.height)
                        Divider(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        )
                        StatItem(label = "Weight", value = currentUser.weight)
                        Divider(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        )
                        StatItem(label = "Age", value = currentUser.age.toString())
                    }
                }
            }
        }
        
        item {
            // Achievements section
            Text(
                text = "Achievements",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            
            // Achievement icons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AchievementIcon(
                    icon = Icons.Default.Speed,
                    label = "Speed Demon"
                )
                AchievementIcon(
                    icon = Icons.Default.Height,
                    label = "Jump Master"
                )
                AchievementIcon(
                    icon = Icons.Default.FitnessCenter,
                    label = "Strength Champion"
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        item {
            // Test Results section
            Text(
                text = "Test Results",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            
            // Test result items
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TestResultItem(
                        name = "40 Yard Dash",
                        value = "4.6s",
                        percentile = "85%"
                    )
                    
                    Divider(color = Color.LightGray)
                    
                    TestResultItem(
                        name = "Vertical Jump",
                        value = "36\"",
                        percentile = "72%"
                    )
                    
                    Divider(color = Color.LightGray)
                    
                    TestResultItem(
                        name = "Bench Press",
                        value = "225 lbs",
                        percentile = "68%"
                    )
                }
            }
            
            // Bottom spacer for navigation
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
private fun AchievementIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = PrimaryBlue,
                modifier = Modifier.size(28.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.DarkGray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TestResultItem(
    name: String,
    value: String,
    percentile: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* View test details */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Blue circle indicator
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(PrimaryBlue, CircleShape)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Test info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        
        // Percentile and chevron
        Text(
            text = percentile,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = PrimaryBlue
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "View details",
            tint = Color.Gray
        )
    }
}
