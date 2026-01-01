package org.example.kotlinconference101

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.surfaceDim
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Color definitions
val StoneBrown80 = Color(0xFFCEBAA8) // Firewood brown
val StoneBrown40 = Color(0xFF593C39) // Brown Stone
val StoneBrownAccent = Color(0xFF8B6F47) // Earthy accent
val SurfaceLight = Color(0xFFFAF8F6) // Off-white for light theme
val SurfaceDark = Color(0xFF1A1514) // Very dark brown for dark theme

private val DarkColorScheme = darkColorScheme(
    primary = StoneBrown80,
    onPrimary = StoneBrown40,
    primaryContainer = StoneBrown40,
    onPrimaryContainer = StoneBrown80,
    secondary = StoneBrownAccent,
    onSecondary = Color.White,
    secondaryContainer = StoneBrown80,
    onSecondaryContainer = StoneBrown40,
    tertiary = Color(0xFF9BA4A8),
    onTertiary = StoneBrown40,
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = SurfaceDark,
    onBackground = StoneBrown80,
    surface = Color(0xFF2C2420),
    onSurface = StoneBrown80,
    surfaceVariant = Color(0xFF49454E),
    onSurfaceVariant = StoneBrown80,
)

private val LightColorScheme = lightColorScheme(
    primary = StoneBrown40,
    onPrimary = Color.White,
    primaryContainer = StoneBrown80,
    onPrimaryContainer = StoneBrown40,
    secondary = StoneBrownAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DFD7),
    onSecondaryContainer = StoneBrown40,
    tertiary = Color(0xFF7F848E),
    onTertiary = Color.White,
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = SurfaceLight,
    onBackground = StoneBrown40,
    surface = Color.White,
    onSurface = StoneBrown40,
    surfaceVariant = Color(0xFFE8DFD7),
    onSurfaceVariant = StoneBrown40,
)

private val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}