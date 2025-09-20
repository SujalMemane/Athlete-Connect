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
            name = "John Smith",
            age = 22,
            sport = "Basketball",
            location = "Los Angeles, CA",
            profilePicture = "",
            coverPhoto = "",
            height = "6'2\"",
            weight = "185 lbs",
            personalBests = mapOf(
                "40 Yard Dash" to "4.6s",
                "Vertical Jump" to "36\"",
                "Bench Press" to "225 lbs"
            ),
            testResults = emptyList(),
            achievements = listOf(
                "Speed Demon",
                "Jump Master",
                "Strength Champion"
            ),
            videos = emptyList(),
            verified = true,
            following = false,
            bio = "Passionate basketball player with 8 years of experience. Always looking to improve and compete at the highest level."
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        item {
            // Blue header with edit button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(PrimaryBlue)
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
                        tint = Color.White
                    )
                }
                
                // Eye icon in the center
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Profile Visibility",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            }

            // Profile card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-30).dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentUser.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${currentUser.sport} • ${currentUser.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = currentUser.bio,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
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
                            color = Color.LightGray
                        )
                        StatItem(label = "Weight", value = currentUser.weight)
                        Divider(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp),
                            color = Color.LightGray
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
                colors = CardDefaults.cardColors(containerColor = Color.White),
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
