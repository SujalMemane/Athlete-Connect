package com.coursecampus.athleteconnect.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coursecampus.athleteconnect.data.model.*
import com.coursecampus.athleteconnect.ui.camera.CameraScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.coursecampus.athleteconnect.ui.screens.TestsViewModel
import com.coursecampus.athleteconnect.ui.screens.*
import com.coursecampus.athleteconnect.ui.theme.PrimaryBlue

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Opportunities : Screen("opportunities", "Opportunities", Icons.Default.Work)
    object Tests : Screen("tests", "Tests", Icons.Default.FitnessCenter)
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Messages : Screen("messages", "Messages", Icons.Default.Message)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    
    // Detail screens
    object Camera : Screen("camera/{testId}", "Camera", Icons.Default.Camera)
    object TestDetail : Screen("test_detail/{testId}", "Test Detail", Icons.Default.FitnessCenter)
    object TestModeSelect : Screen("test_mode/{testId}", "Test Mode", Icons.Default.Settings)
    object TestResults : Screen("test_results/{testId}", "Test Results", Icons.Default.Assessment)
    object OpportunityDetail : Screen("opportunity_detail/{opportunityId}", "Opportunity Detail", Icons.Default.Work)
    object ChatThread : Screen("chat/{conversationId}", "Chat", Icons.Default.Message)
    object PlayerDetail : Screen("player_detail/{playerId}", "Player Detail", Icons.Default.Person)
    object TestHistory : Screen("test_history", "Test History", Icons.Default.History)
    object Leaderboard : Screen("leaderboard", "Leaderboard", Icons.Default.EmojiEvents)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessLabNavigation() {
    val navController = rememberNavController()
    // Include all screens shown in the bottom navigation (6 tabs)
    val screens = listOf(
        Screen.Home,
        Screen.Profile,
        Screen.Tests,
        Screen.Dashboard,
        Screen.Opportunities,
        Screen.Messages
    )

    Scaffold(
        bottomBar = {
            CustomBottomNavigation(
                screens = screens,
                navController = navController
            )
        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeFeedScreenNew()
            }
            composable(Screen.Opportunities.route) {
                OpportunitiesScreen()
            }
            composable(Screen.Tests.route) {
                TestsScreenNew(navController)
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
            composable(Screen.Messages.route) {
                MessagesScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreenNew(navController)
            }
            composable(Screen.TestDetail.route) { backStackEntry ->
                val testId = backStackEntry.arguments?.getString("testId") ?: "1"
                TestDetailScreen(
                    testId = testId,
                    onStartTest = {
                        navController.navigate(Screen.TestModeSelect.route.replace("{testId}", testId))
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
            
            composable(Screen.TestModeSelect.route) { backStackEntry ->
                val testId = backStackEntry.arguments?.getString("testId") ?: "1"
                TestModeSelectScreen(
                    testId = testId,
                    onLiveAnalysisSelected = {
                        navController.navigate(Screen.Camera.route.replace("{testId}", testId))
                    },
                    onUploadVideoSelected = {
                        // Handle video upload
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
            
            composable(Screen.TestResults.route) { backStackEntry ->
                val testId = backStackEntry.arguments?.getString("testId") ?: "1"
                LiveAIAnalysisScreen(
                    testId = testId,
                    onRetakeTest = {
                        navController.navigate(Screen.TestModeSelect.route.replace("{testId}", testId))
                    },
                    onSaveResults = {
                        navController.navigate(Screen.TestHistory.route)
                    },
                    onShareResults = {
                        // Handle share
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
            
            composable(Screen.TestHistory.route) {
                TestHistoryScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen()
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            
            composable(Screen.Camera.route) { backStackEntry ->
                val testsViewModel: TestsViewModel = hiltViewModel()
                val testId = backStackEntry.arguments?.getString("testId") ?: "1"
                // For now, we'll use a mock test. In a real app, you'd fetch this from the repository
                val mockTest = FitnessTest(
                    id = testId,
                    name = "Test $testId",
                    description = "Mock test for demonstration",
                    category = "Speed",
                    instructions = listOf("Follow the instructions"),
                    duration = 5,
                    difficulty = Difficulty.BEGINNER
                )
                CameraScreen(
                    fitnessTest = mockTest,
                    onBackClick = { navController.popBackStack() },
                    onTestComplete = { result ->
                        testsViewModel.saveTestResult(result)
                        navController.navigate(Screen.TestResults.route.replace("{testId}", testId))
                    }
                )
            }
            
            // Quick test route for FAB
            composable("test_mode/quick_test") {
                TestModeSelectScreen(
                    testId = "quick_test",
                    onLiveAnalysisSelected = {
                        navController.navigate("camera/quick_test")
                    },
                    onUploadVideoSelected = {
                        // Handle video upload
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomNavigation(
    screens: List<Screen>,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        // Bottom Navigation Bar - Exact match to second screenshot
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Home tab
                BottomNavItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    selected = currentRoute == Screen.Home.route,
                    onClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                
                // Profile tab
                BottomNavItem(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    selected = currentRoute == Screen.Profile.route,
                    onClick = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                
                // Test tab
                BottomNavItem(
                    icon = Icons.Default.Assignment,
                    label = "Test",
                    selected = currentRoute == Screen.Tests.route,
                    onClick = {
                        navController.navigate(Screen.Tests.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                
                // Empty space for FAB
                Box(modifier = Modifier.width(56.dp))
                
                // Dashboard tab
                BottomNavItem(
                    icon = Icons.Default.Dashboard,
                    label = "Dashboard",
                    selected = currentRoute == Screen.Dashboard.route,
                    onClick = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                
                // Opportunities tab
                BottomNavItem(
                    icon = Icons.Default.Work,
                    label = "Opportunities",
                    selected = currentRoute == Screen.Opportunities.route,
                    onClick = {
                        navController.navigate(Screen.Opportunities.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                
                // Message tab
                BottomNavItem(
                    icon = Icons.Default.Chat,
                    label = "Message",
                    selected = currentRoute == Screen.Messages.route,
                    onClick = {
                        navController.navigate(Screen.Messages.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
        
        // FAB in the center
        FloatingActionButton(
            onClick = { 
                navController.navigate("test_mode/quick_test")
            },
            modifier = Modifier
                .align(Alignment.Center)
                .size(56.dp),
            containerColor = PrimaryBlue,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Start Test",
                tint = Color.White
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) PrimaryBlue else Color.Gray
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
            .fillMaxHeight()
            .width(48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.height(2.dp))
        
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            style = MaterialTheme.typography.labelSmall
        )
        
        // No underline in the second image
    }
}

