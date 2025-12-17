package org.example.kotlinconference101

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val StoneBrown80 = Color(0xFFCEBAA8) // Firewood brown
val StoneBrown40 = Color(0xFF593C39) // Brown Stone

private val DarkColorScheme = darkColorScheme(
    primary = StoneBrown80,       // Light color pops on dark screens
    onPrimary = StoneBrown40,     // Dark text on light button
    primaryContainer = StoneBrown40,
    onPrimaryContainer = StoneBrown80,
)

private val LightColorScheme = lightColorScheme(
    primary = StoneBrown40,       // Dark color is main branding on light screens
    onPrimary = Color.White,
    primaryContainer = StoneBrown80,
    onPrimaryContainer = StoneBrown40,
)