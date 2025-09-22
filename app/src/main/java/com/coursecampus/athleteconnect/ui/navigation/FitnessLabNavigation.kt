package com.coursecampus.athleteconnect.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coursecampus.athleteconnect.data.model.Difficulty
import com.coursecampus.athleteconnect.data.model.FitnessTest
import com.coursecampus.athleteconnect.ui.camera.CameraScreen
import com.coursecampus.athleteconnect.ui.screens.DashboardScreen
import com.coursecampus.athleteconnect.ui.screens.HomeFeedScreenNew
import com.coursecampus.athleteconnect.ui.screens.LeaderboardScreen
import com.coursecampus.athleteconnect.ui.screens.LiveAIAnalysisScreen
import com.coursecampus.athleteconnect.ui.screens.MessagesScreen
import com.coursecampus.athleteconnect.ui.screens.OpportunitiesScreen
import com.coursecampus.athleteconnect.ui.screens.ProfileScreenNew
import com.coursecampus.athleteconnect.ui.screens.SettingsScreen
import com.coursecampus.athleteconnect.ui.screens.TestDetailScreen
import com.coursecampus.athleteconnect.ui.screens.TestHistoryScreen
import com.coursecampus.athleteconnect.ui.screens.TestModeSelectScreen
import com.coursecampus.athleteconnect.ui.screens.TestsScreenNew
import com.coursecampus.athleteconnect.ui.screens.TestsViewModel
import com.coursecampus.athleteconnect.ui.theme.PrimaryBlue
import com.coursecampus.athleteconnect.ui.screens.auth.ForgotPasswordScreen

sealed class Screen(
    val route: String,
    val icon: ImageVector
) {
    object Home : Screen("home", Icons.Default.Home)
    object Opportunities : Screen("opportunities", Icons.Default.Work)
    object Tests : Screen("tests", Icons.Default.FitnessCenter)
    object Dashboard : Screen("dashboard", Icons.Default.Dashboard)
    object Messages : Screen("messages", Icons.Default.Message)
    object Profile : Screen("profile", Icons.Default.Person)
    object ForgotPassword : Screen("forgot_password", Icons.Default.Lock)

    // Detail screens
    object Camera : Screen("camera/{testId}", Icons.Default.Camera)
    object TestDetail : Screen("test_detail/{testId}", Icons.Default.FitnessCenter)
    object TestModeSelect : Screen("test_mode/{testId}", Icons.Default.Settings)
    object TestResults : Screen("test_results/{testId}", Icons.Default.Assessment)
    object OpportunityDetail :
        Screen("opportunity_detail/{opportunityId}", Icons.Default.Work)

    object ChatThread : Screen("chat/{conversationId}", Icons.Default.Message)
    object PlayerDetail : Screen("player_detail/{playerId}", Icons.Default.Person)
    object TestHistory : Screen("test_history", Icons.Default.History)
    object Leaderboard : Screen("leaderboard", Icons.Default.EmojiEvents)
    object Settings : Screen("settings", Icons.Default.Settings)
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
                        navController.navigate(
                            Screen.TestModeSelect.route.replace(
                                "{testId}",
                                testId
                            )
                        )
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
                        navController.navigate(
                            Screen.TestModeSelect.route.replace(
                                "{testId}",
                                testId
                            )
                        )
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

            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onNavigateToSignIn = { navController.popBackStack() },
                    onResetPassword = { email, otp, newPassword ->
                        // Handle password reset logic
                        navController.popBackStack()
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
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Removed label text below icon
    }
}
