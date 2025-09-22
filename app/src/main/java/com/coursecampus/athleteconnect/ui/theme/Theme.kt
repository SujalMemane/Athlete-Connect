package com.coursecampus.athleteconnect.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = FitnessPrimaryDark,
    onPrimary = FitnessOnPrimaryDark,
    primaryContainer = FitnessPrimaryVariantDark,
    onPrimaryContainer = FitnessOnPrimaryDark,
    secondary = FitnessSecondaryDark,
    onSecondary = FitnessOnSecondaryDark,
    secondaryContainer = FitnessSecondaryVariantDark,
    onSecondaryContainer = FitnessOnSecondaryDark,
    tertiary = FitnessInfo,
    onTertiary = FitnessOnPrimaryDark,
    background = FitnessBackgroundDark,
    onBackground = FitnessOnBackgroundDark,
    surface = FitnessSurfaceDark,
    onSurface = FitnessOnSurfaceDark,
    surfaceVariant = FitnessSurfaceVariantDark,
    onSurfaceVariant = FitnessOnSurfaceDark,
    error = FitnessError,
    onError = FitnessOnPrimaryDark,
    errorContainer = FitnessError,
    onErrorContainer = FitnessOnPrimaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = FitnessPrimary,
    onPrimary = FitnessOnPrimary,
    primaryContainer = FitnessPrimaryVariant,
    onPrimaryContainer = FitnessOnPrimary,
    secondary = FitnessSecondary,
    onSecondary = FitnessOnSecondary,
    secondaryContainer = FitnessSecondaryVariant,
    onSecondaryContainer = FitnessOnSecondary,
    tertiary = FitnessInfo,
    onTertiary = FitnessOnPrimary,
    background = FitnessBackground,
    onBackground = FitnessOnBackground,
    surface = FitnessSurface,
    onSurface = FitnessOnSurface,
    surfaceVariant = FitnessSurfaceVariant,
    onSurfaceVariant = FitnessOnSurface,
    error = FitnessError,
    onError = FitnessOnPrimary,
    errorContainer = FitnessError,
    onErrorContainer = FitnessOnPrimary
)

@Composable
fun AthleteConnectTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FitnessTypography,
        content = content
    )
}