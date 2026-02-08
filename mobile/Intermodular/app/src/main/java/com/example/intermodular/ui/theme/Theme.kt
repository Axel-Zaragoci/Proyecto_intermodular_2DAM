package com.example.intermodular.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,

    secondary = BlueAccent,
    onSecondary = Color.White,

    background = LightBackground,
    onBackground = LightTextPrimary,

    surface = LightSurface,
    onSurface = LightTextPrimary,

    surfaceVariant = LightSurfaceAlt,
    onSurfaceVariant = LightTextSecondary,

    error = Error,
    onError = Color.White,

    outline = LightBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,

    secondary = BlueAccent,
    onSecondary = Color.White,

    background = DarkBackground,
    onBackground = DarkTextPrimary,

    surface = DarkSurface,
    onSurface = DarkTextPrimary,

    surfaceVariant = DarkSurfaceAlt,
    onSurfaceVariant = DarkTextSecondary,

    error = Error,
    onError = Color.White,

    outline = DarkBorder
)

@Composable
fun AppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        content = content
    )
}