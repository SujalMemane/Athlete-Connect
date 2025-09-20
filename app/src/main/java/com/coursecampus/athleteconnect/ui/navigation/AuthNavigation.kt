package com.coursecampus.athleteconnect.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coursecampus.athleteconnect.ui.screens.auth.*

sealed class AuthScreen(val route: String) {
    object Splash : AuthScreen("splash")
    object SignIn : AuthScreen("signin")
    object Registration : AuthScreen("registration")
    object LoginSignUp : AuthScreen("login_signup")
    object ChoosePath : AuthScreen("choose_path")
    object AboutYourself : AuthScreen("about_yourself")
    object FitnessGoals : AuthScreen("fitness_goals")
    object Achievements : AuthScreen("achievements")
}

@Composable
fun AuthNavigation(
    navController: NavHostController,
    onAuthComplete: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AuthScreen.Splash.route
    ) {
        composable(AuthScreen.Splash.route) {
            SplashScreenNew(
                onNavigateToAuth = {
                    navController.navigate(AuthScreen.SignIn.route) {
                        popUpTo(AuthScreen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(AuthScreen.SignIn.route) {
            SignInScreenNew(
                onNavigateToRegistration = {
                    navController.navigate(AuthScreen.Registration.route)
                },
                onNavigateToForgotPassword = {
                    // Handle forgot password
                },
                onSignIn = { email, password ->
                    // Navigate to Choose Path
                    navController.navigate(AuthScreen.ChoosePath.route)
                },
                onSocialSignIn = { provider ->
                    // Navigate to Choose Path
                    navController.navigate(AuthScreen.ChoosePath.route)
                }
            )
        }
        
        composable(AuthScreen.Registration.route) {
            RegistrationScreenNew(
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                onRegister = { email, password, confirmPassword ->
                    // Handle registration logic
                    onAuthComplete()
                },
                onSocialSignIn = { provider ->
                    // Handle social sign in logic
                    onAuthComplete()
                }
            )
        }
        
        
        composable(AuthScreen.ChoosePath.route) {
            ChoosePathScreenNew(
                onAthleteSelected = {
                    navController.navigate(AuthScreen.AboutYourself.route)
                },
                onCoachSelected = {
                    onAuthComplete()
                }
            )
        }
        
        composable(AuthScreen.AboutYourself.route) {
            AboutYourselfScreen(
                onContinue = {
                    navController.navigate(AuthScreen.FitnessGoals.route)
                }
            )
        }
        
        composable(AuthScreen.FitnessGoals.route) {
            FitnessGoalsScreenNew(
                onContinue = {
                    navController.navigate(AuthScreen.Achievements.route)
                }
            )
        }
        
        composable(AuthScreen.Achievements.route) {
            AchievementsScreen(
                onComplete = {
                    onAuthComplete()
                }
            )
        }
    }
}
