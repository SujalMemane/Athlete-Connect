package com.coursecampus.athleteconnect.ui.screens

import androidx.compose.foundation.background
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
import com.coursecampus.athleteconnect.data.model.*
import com.coursecampus.athleteconnect.ui.components.*
import com.coursecampus.athleteconnect.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen() {
    val conversations = remember {
        listOf(
            Conversation(
                id = "1",
                participants = listOf("current_user", "alex_johnson"),
                lastMessage = Message(
                    id = "1",
                    senderId = "alex_johnson",
                    senderName = "Alex Johnson",
                    content = "Hey! Great performance at the track meet yesterday!",
                    timestamp = "2 hours ago",
                    read = false,
                    type = MessageType.TEXT
                ),
                unreadCount = 2,
                lastActivity = "2 hours ago"
            ),
            Conversation(
                id = "2",
                participants = listOf("current_user", "sarah_williams"),
                lastMessage = Message(
                    id = "2",
                    senderId = "sarah_williams",
                    senderName = "Sarah Williams",
                    content = "Thanks for the training tips!",
                    timestamp = "1 day ago",
                    read = true,
                    type = MessageType.TEXT
                ),
                unreadCount = 0,
                lastActivity = "1 day ago"
            ),
            Conversation(
                id = "3",
                participants = listOf("current_user", "mike_davis"),
                lastMessage = Message(
                    id = "3",
                    senderId = "mike_davis",
                    senderName = "Mike Davis",
                    content = "Are you going to the basketball camp next week?",
                    timestamp = "3 days ago",
                    read = true,
                    type = MessageType.TEXT
                ),
                unreadCount = 0,
                lastActivity = "3 days ago"
            ),
            Conversation(
                id = "4",
                participants = listOf("current_user", "coach_martinez"),
                lastMessage = Message(
                    id = "4",
                    senderId = "coach_martinez",
                    senderName = "Coach Martinez",
                    content = "Your test results look great! Keep up the good work.",
                    timestamp = "1 week ago",
                    read = true,
                    type = MessageType.TEXT
                ),
                unreadCount = 0,
                lastActivity = "1 week ago"
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Messages",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                IconButton(onClick = { /* New message */ }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "New Message",
                        tint = FitnessPrimary
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(conversations) { conversation ->
            ConversationCard(conversation = conversation)
        }
    }
}

@Composable
fun ConversationCard(
    conversation: Conversation,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage?.senderName ?: "Unknown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = conversation.lastActivity,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage?.content ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (conversation.unreadCount > 0) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        },
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (conversation.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = FitnessPrimary,
                                    shape = CircleShape
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = conversation.unreadCount.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    conversationId: String,
    onBackClick: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    
    val messages = remember {
        listOf(
            Message(
                id = "1",
                senderId = "alex_johnson",
                senderName = "Alex Johnson",
                content = "Hey! Great performance at the track meet yesterday!",
                timestamp = "2:30 PM",
                read = true,
                type = MessageType.TEXT
            ),
            Message(
                id = "2",
                senderId = "current_user",
                senderName = "You",
                content = "Thanks! Your 40-yard dash time was incredible too!",
                timestamp = "2:32 PM",
                read = true,
                type = MessageType.TEXT
            ),
            Message(
                id = "3",
                senderId = "alex_johnson",
                senderName = "Alex Johnson",
                content = "We should train together sometime. I know some great drills.",
                timestamp = "2:35 PM",
                read = false,
                type = MessageType.TEXT
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Alex Johnson",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }
        
        // Message Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                shape = RoundedCornerShape(24.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            FloatingActionButton(
                onClick = { /* Send message */ },
                modifier = Modifier.size(48.dp),
                containerColor = FitnessPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    modifier: Modifier = Modifier
) {
    val isOwnMessage = message.senderId == "current_user"
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwnMessage) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        if (!isOwnMessage) {
            // Profile picture for received messages
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Column(
            horizontalAlignment = if (isOwnMessage) {
                Alignment.End
            } else {
                Alignment.Start
            }
        ) {
            if (!isOwnMessage) {
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isOwnMessage) {
                        FitnessPrimary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isOwnMessage) 16.dp else 4.dp,
                    bottomEnd = if (isOwnMessage) 4.dp else 16.dp
                )
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isOwnMessage) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
            
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

