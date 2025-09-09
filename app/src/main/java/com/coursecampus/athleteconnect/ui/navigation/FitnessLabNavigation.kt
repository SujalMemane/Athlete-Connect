package com.coursecampus.athleteconnect.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coursecampus.athleteconnect.data.model.*
import com.coursecampus.athleteconnect.ui.camera.CameraScreen
import com.coursecampus.athleteconnect.ui.screens.*

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Tests : Screen("tests", "Tests", Icons.Default.FitnessCenter)
    object Leaderboard : Screen("leaderboard", "Leaderboard", Icons.Default.EmojiEvents)
    object Opportunities : Screen("opportunities", "Opportunities", Icons.Default.Work)
    object Messages : Screen("messages", "Messages", Icons.Default.Message)
    object Camera : Screen("camera/{testId}", "Camera", Icons.Default.Camera)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessLabNavigation() {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.Dashboard,
        Screen.Profile,
        Screen.Tests,
        Screen.Leaderboard,
        Screen.Opportunities,
        Screen.Messages
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Tests.route) {
                TestsScreenNew()
            }
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen()
            }
            composable(Screen.Opportunities.route) {
                OpportunitiesScreen()
            }
            composable(Screen.Messages.route) {
                MessagesScreen()
            }
            composable(Screen.Camera.route) { backStackEntry ->
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
                        // Save result and navigate back
                        // In a real app, you'd save this to the repository
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

