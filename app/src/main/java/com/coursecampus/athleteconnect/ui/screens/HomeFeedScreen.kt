package com.coursecampus.athleteconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coursecampus.athleteconnect.ui.theme.*

data class FeedPost(
    val id: String,
    val userName: String,
    val userAvatar: String,
    val postImage: String,
    val postText: String,
    val likes: Int,
    val comments: Int,
    val timeAgo: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeedScreen() {
    val feedPosts = remember {
        listOf(
            FeedPost(
                id = "1",
                userName = "Ravi Yadav",
                userAvatar = "RY",
                postImage = "stadium",
                postText = "Just completed my best 5K run! 🏃‍♂️ Broke my personal record by 30 seconds. Training for the marathon is paying off!",
                likes = 42,
                comments = 8,
                timeAgo = "2 hours ago"
            ),
            FeedPost(
                id = "2",
                userName = "Saniya Mirza",
                userAvatar = "SM",
                postImage = "gym",
                postText = "New personal record in deadlifts today! 💪 Hit 225 lbs for 5 reps. Hard work is showing results!",
                likes = 67,
                comments = 12,
                timeAgo = "4 hours ago"
            ),
            FeedPost(
                id = "3",
                userName = "Coach Thompson",
                userAvatar = "CT",
                postImage = "training",
                postText = "Just posted a new training program for sprinters! Check it out in the Training section. This 8-week program will boost your speed by 15%.",
                likes = 89,
                comments = 24,
                timeAgo = "6 hours ago"
            )
        )
    }
    
    val categories = listOf("All", "Running", "Strength", "Flexibility", "Team Sports", "Nutrition")
    var selectedCategory by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Top Bar with Gradient
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp,
            color = Color.White
        ) {
            Column {
                TopAppBar(
                    title = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(PrimaryBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "AC",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Athlete Connect",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Search */ }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Black
                            )
                        }
                        IconButton(onClick = { /* Notifications */ }) {
                            BadgedBox(
                                badge = {
                                    Badge {
                                        Text("3")
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.Black
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                
                // Category Tabs
                ScrollableTabRow(
                    selectedTabIndex = categories.indexOf(selectedCategory),
                    edgePadding = 16.dp,
                    containerColor = Color.White,
                    contentColor = PrimaryBlue,
                    divider = {}
                ) {
                    categories.forEachIndexed { index, category ->
                        Tab(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            text = { 
                                Text(
                                    text = category,
                                    fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal
                                ) 
                            },
                            selectedContentColor = PrimaryBlue,
                            unselectedContentColor = Color.Gray
                        )
                    }
                }
            }
        }
        
        // Welcome Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = PrimaryBlue
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Welcome back, Alex!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your fitness score is up by 12% this week!",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { /* View Stats */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "View Stats",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }
        }

        // Feed Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Latest Activity",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(feedPosts) { post ->
                FeedPostCard(post = post)
            }
            
            // Bottom padding for navigation bar
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun FeedPostCard(post: FeedPost) {
    var isLiked by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { /* View post details */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // User Info with More Options
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar with border
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, CircleShape)
                            .border(2.dp, PrimaryBlue, CircleShape)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(
                                when (post.userAvatar) {
                                    "RY" -> PrimaryBlue
                                    "SM" -> Color(0xFF8E44AD) // Purple
                                    else -> Color(0xFF16A085) // Teal
                                },
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.userAvatar,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = post.userName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = post.timeAgo,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                
                // More options
                IconButton(onClick = { /* Show options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Post Text with better typography
            Text(
                text = post.postText,
                fontSize = 15.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Post Image Placeholder with better visuals
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            when (post.postImage) {
                                "stadium" -> Color(0xFFE3F2FD) // Light Blue
                                "gym" -> Color(0xFFF3E5F5) // Light Purple
                                else -> Color(0xFFE8F5E9) // Light Green
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = when (post.postImage) {
                                "stadium" -> Icons.Default.DirectionsRun
                                "gym" -> Icons.Default.FitnessCenter
                                else -> Icons.Default.School
                            },
                            contentDescription = post.postImage,
                            tint = when (post.postImage) {
                                "stadium" -> Color(0xFF1976D2) // Blue
                                "gym" -> Color(0xFF9C27B0) // Purple
                                else -> Color(0xFF388E3C) // Green
                            },
                            modifier = Modifier.size(64.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = when (post.postImage) {
                                "stadium" -> "Running Workout"
                                "gym" -> "Strength Training"
                                else -> "Training Program"
                            },
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action Buttons with animations
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isLiked = !isLiked }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${if (isLiked) post.likes + 1 else post.likes}",
                        fontSize = 14.sp,
                        color = if (isLiked) Color.Red else Color.Gray,
                        fontWeight = if (isLiked) FontWeight.Bold else FontWeight.Normal
                    )
                }
                
                // Comment button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* Show comments */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Comment,
                        contentDescription = "Comment",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.comments}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                // Share button with pill background
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEEEEEE))
                        .clickable { /* Share post */ }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Share",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
