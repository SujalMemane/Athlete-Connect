package com.coursecampus.athleteconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
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
fun OpportunitiesScreen() {
    var selectedType by remember { mutableStateOf("All") }
    var selectedSport by remember { mutableStateOf("All") }
    
    val types = listOf("All", "Scholarship", "Trial", "Camp", "Internship", "Job")
    val sports = listOf("All", "Basketball", "Football", "Soccer", "Track", "Swimming")
    
    val opportunities = remember {
        listOf(
            Opportunity(
                id = "1",
                title = "NCAA Division I Basketball Scholarship",
                organization = "University of California",
                type = OpportunityType.SCHOLARSHIP,
                sport = "Basketball",
                location = "Los Angeles, CA",
                deadline = "2024-03-15",
                description = "Full athletic scholarship for exceptional basketball players with strong academic records.",
                requirements = listOf(
                    "Minimum 3.0 GPA",
                    "SAT score 1200+",
                    "Basketball experience 5+ years",
                    "Video highlights required"
                ),
                applied = false,
                salary = "Full Tuition + Room & Board",
                duration = "4 years"
            ),
            Opportunity(
                id = "2",
                title = "Professional Football Tryout",
                organization = "Los Angeles Rams",
                type = OpportunityType.TRIAL,
                sport = "Football",
                location = "Los Angeles, CA",
                deadline = "2024-02-28",
                description = "Open tryout for undrafted free agents and college graduates.",
                requirements = listOf(
                    "College football experience",
                    "40-yard dash under 4.6s",
                    "Medical clearance",
                    "Background check"
                ),
                applied = true,
                duration = "1 day"
            ),
            Opportunity(
                id = "3",
                title = "Elite Track & Field Camp",
                organization = "Olympic Training Center",
                type = OpportunityType.CAMP,
                sport = "Track",
                location = "Colorado Springs, CO",
                deadline = "2024-04-01",
                description = "Intensive training camp with Olympic coaches and athletes.",
                requirements = listOf(
                    "National qualifying times",
                    "Coach recommendation",
                    "Medical insurance",
                    "Training log"
                ),
                applied = false,
                duration = "2 weeks"
            ),
            Opportunity(
                id = "4",
                title = "Sports Marketing Internship",
                organization = "Nike",
                type = OpportunityType.INTERNSHIP,
                sport = "All Sports",
                location = "Portland, OR",
                deadline = "2024-03-30",
                description = "Summer internship in sports marketing and athlete relations.",
                requirements = listOf(
                    "Marketing or Business major",
                    "GPA 3.5+",
                    "Social media experience",
                    "Portfolio required"
                ),
                applied = false,
                salary = "$25/hour",
                duration = "10 weeks"
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Opportunities",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                IconButton(onClick = { /* Search */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = FitnessPrimary
                    )
                }
            }
        }

        item {
            // Filters
            Column {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(types) { type ->
                        FilterChip(
                            onClick = { selectedType = type },
                            label = { Text(type) },
                            selected = selectedType == type,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FitnessPrimary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sports) { sport ->
                        FilterChip(
                            onClick = { selectedSport = sport },
                            label = { Text(sport) },
                            selected = selectedSport == sport,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FitnessSecondary,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }
                }
            }
        }

        item {
            // Opportunities List
            Column {
                Text(
                    text = "Available Opportunities",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    opportunities.forEach { opportunity ->
                        OpportunityCard(opportunity = opportunity)
                    }
                }
            }
        }
    }
}

@Composable
fun OpportunityCard(
    opportunity: Opportunity,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Type Badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = getTypeColor(opportunity.type).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = opportunity.type.name,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = getTypeColor(opportunity.type)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = opportunity.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = opportunity.organization,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                if (opportunity.applied) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Applied",
                        tint = FitnessSuccess,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Sport",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = opportunity.sport,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Column {
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = opportunity.location,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Column {
                    Text(
                        text = "Deadline",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = opportunity.deadline,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = opportunity.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            
            if (opportunity.salary.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Compensation: ${opportunity.salary}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = FitnessSuccess
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action Button
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (opportunity.applied) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        FitnessPrimary
                    }
                )
            ) {
                Icon(
                    imageVector = if (opportunity.applied) Icons.Default.Check else Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (opportunity.applied) "Applied" else "Apply Now"
                )
            }
        }
    }
}

@Composable
private fun getTypeColor(type: OpportunityType): Color {
    return when (type) {
        OpportunityType.SCHOLARSHIP -> FitnessPrimary
        OpportunityType.TRIAL -> FitnessSecondary
        OpportunityType.CAMP -> FitnessInfo
        OpportunityType.INTERNSHIP -> FitnessWarning
        OpportunityType.JOB -> FitnessSuccess
        OpportunityType.COMPETITION -> FitnessError
    }
}

