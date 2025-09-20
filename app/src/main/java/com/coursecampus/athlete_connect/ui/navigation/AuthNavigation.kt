package com.coursecampus.athleteconnect.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coursecampus.athleteconnect.ui.screens.auth.ForgotPasswordScreen
import com.coursecampus.athleteconnect.ui.screens.auth.RegistrationScreen
import com.coursecampus.athleteconnect.ui.screens.auth.SignUpScreen
import com.coursecampus.athleteconnect.ui.screens.auth.SplashScreen

sealed class AuthScreen(val route: String) {
    object Splash : AuthScreen("splash")
    object SignIn : AuthScreen("signin")
    object Registration : AuthScreen("registration")
    object ForgotPassword : AuthScreen("forgot_password")
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
            SplashScreen(
                onNavigateToAuth = {
                    navController.navigate(AuthScreen.SignIn.route) {
                        popUpTo(AuthScreen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AuthScreen.SignIn.route) {
            SignUpScreen(
                onNavigateToRegistration = {
                    navController.navigate(AuthScreen.Registration.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(AuthScreen.ForgotPassword.route)
                },
                onSignIn = { email, password ->
                    // Handle sign in logic
                    onAuthComplete()
                },
                onSocialSignIn = { provider ->
                    // Handle social sign in logic
                    onAuthComplete()
                }
            )
        }

        composable(AuthScreen.Registration.route) {
            RegistrationScreen(
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

        composable(AuthScreen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                onResetPassword = { email, otp, newPassword ->
                    // Handle password reset logic
                    navController.popBackStack()
                }
            )
        }
    }
}
