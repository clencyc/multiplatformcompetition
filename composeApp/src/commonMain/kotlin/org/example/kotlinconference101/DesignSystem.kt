package org.example.kotlinconference101

import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinconference101.composeapp.generated.resources.Res
import kotlinconference101.composeapp.generated.resources.jersey25_regular
import kotlinconference101.composeapp.generated.resources.silkscreen_bold
import kotlinconference101.composeapp.generated.resources.silkscreen_regular
import kotlinconference101.composeapp.generated.resources.space_grotesk_bold
import kotlinconference101.composeapp.generated.resources.space_grotesk_medium
import kotlinconference101.composeapp.generated.resources.space_grotesk_regular
import org.jetbrains.compose.resources.Font

// ============================================================================
// Arcade design system — dark retro-arcade / pixel-neon
// Near-black ground, neon ember-orange primary (sampled from the Africa's
// Talking reference set), candy accents, pixel display type.
// ============================================================================

object ArcadeColors {
    // Ground — warm neutral near-black (sampled ~#101010) so the ember glows
    val Ink = Color(0xFF0F0D0B)
    val Surface = Color(0xFF1A1613)        // dark card
    val SurfaceRaised = Color(0xFF272119)  // elevated card / chip
    val Hairline = Color(0xFF3A322A)       // non-neon dividers, quiet borders

    // Primary neon — Africa's Talking banner orange (#FF640D) tuned for dark bg
    val Ember = Color(0xFFFF6B1A)
    val EmberDim = Color(0xFF87370A)       // pressed / track state
    val OnEmber = Color(0xFF0F0D0B)        // ink on ember fills

    // Accents — sampled: coins/amber #FFA900, screen magenta #DE50CC,
    // brain purple #E95DEE, light-trail cyan #0390EA, crimson-pink #EA003D
    val Pink = Color(0xFFFF3D94)
    val Purple = Color(0xFFDD62EA)
    val Amber = Color(0xFFFFAD1F)
    val Cyan = Color(0xFF35B6FF)

    // Text
    val Text = Color(0xFFF7F2EA)
    val TextMuted = Color(0xFFA99C8C)
    val TextFaint = Color(0xFF66594C)

    // Sticker treatment
    val StickerFill = Color(0xFFFFFFFF)
    val StickerOutline = Color(0xFF000000)

    val accents: List<Color> get() = listOf(Ember, Pink, Purple, Amber, Cyan)
}

object ArcadeSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
}

object ArcadeRadii {
    val chip = 12.dp
    val badge = 16.dp
    val card = 24.dp
    val hero = 32.dp
    val pill = 999.dp
}

// ----------------------------------------------------------------------------
// Typography — pixel display (Jersey 25) for big numbers, Silkscreen for tiny
// arcade captions, Space Grotesk for everything readable.
// ----------------------------------------------------------------------------

@Composable
fun pixelDisplayFamily(): FontFamily = FontFamily(Font(Res.font.jersey25_regular))

@Composable
fun pixelArcadeFamily(): FontFamily = FontFamily(
    Font(Res.font.silkscreen_regular),
    Font(Res.font.silkscreen_bold, FontWeight.Bold),
)

@Composable
fun bodyFamily(): FontFamily = FontFamily(
    Font(Res.font.space_grotesk_regular),
    Font(Res.font.space_grotesk_medium, FontWeight.Medium),
    Font(Res.font.space_grotesk_bold, FontWeight.Bold),
)

@Immutable
data class ArcadeTypography(
    /** Hero numbers inside gauges — e.g. "2,048" */
    val displayXL: TextStyle,
    val displayL: TextStyle,
    val displayM: TextStyle,
    /** Small chunky-pixel caption — e.g. "HI-SCORE" */
    val arcadeCaption: TextStyle,
    val headline: TextStyle,
    val title: TextStyle,
    val body: TextStyle,
    val bodySmall: TextStyle,
    /** Uppercase, letter-spaced micro label — caller supplies uppercase text */
    val label: TextStyle,
)

@Composable
fun arcadeTypography(): ArcadeTypography {
    val pixel = pixelDisplayFamily()
    val arcade = pixelArcadeFamily()
    val grotesk = bodyFamily()
    return ArcadeTypography(
        displayXL = TextStyle(fontFamily = pixel, fontSize = 76.sp, lineHeight = 76.sp, color = ArcadeColors.Text),
        displayL = TextStyle(fontFamily = pixel, fontSize = 52.sp, lineHeight = 52.sp, color = ArcadeColors.Text),
        displayM = TextStyle(fontFamily = pixel, fontSize = 34.sp, lineHeight = 36.sp, color = ArcadeColors.Text),
        arcadeCaption = TextStyle(fontFamily = arcade, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 1.sp, color = ArcadeColors.Text),
        headline = TextStyle(fontFamily = grotesk, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 30.sp, color = ArcadeColors.Text),
        title = TextStyle(fontFamily = grotesk, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 24.sp, color = ArcadeColors.Text),
        body = TextStyle(fontFamily = grotesk, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp, color = ArcadeColors.Text),
        bodySmall = TextStyle(fontFamily = grotesk, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp, color = ArcadeColors.TextMuted),
        label = TextStyle(fontFamily = grotesk, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 14.sp, letterSpacing = 1.6.sp, color = ArcadeColors.TextMuted),
    )
}

val LocalArcadeTypography = staticCompositionLocalOf<ArcadeTypography?> { null }

/** Token entry point: `Arcade.colors.Ember`, `Arcade.type.displayXL`, … */
object Arcade {
    val colors = ArcadeColors
    val spacing = ArcadeSpacing
    val radii = ArcadeRadii
    val type: ArcadeTypography
        @Composable get() = LocalArcadeTypography.current
            ?: error("Arcade.type used outside ArcadeTheme")
}

// ----------------------------------------------------------------------------
// Effects — cross-platform draws, no Android elevation involved
// ----------------------------------------------------------------------------

/**
 * Soft colored halo behind an element (radial gradient fading to transparent).
 * Draws beyond the element's bounds, so leave breathing room around it.
 * Best under circular or compact elements: gauges, buttons, emoji chips.
 */
fun Modifier.neonGlow(
    color: Color,
    radius: Dp = 28.dp,
    alpha: Float = 0.5f,
): Modifier = drawBehind {
    val halo = maxOf(size.width, size.height) / 2f + radius.toPx()
    drawCircle(
        brush = Brush.radialGradient(
            0f to color.copy(alpha = alpha),
            1f to color.copy(alpha = 0f),
            center = center,
            radius = halo,
        ),
        radius = halo,
        center = center,
    )
}

/**
 * Blur-like glow hugging a rounded-rect edge — for cards. Simulated with
 * concentric strokes of falling alpha (works on every Compose target).
 */
fun Modifier.neonEdgeGlow(
    color: Color,
    cornerRadius: Dp = ArcadeRadii.card,
    spread: Dp = 14.dp,
    alpha: Float = 0.45f,
    steps: Int = 12,
): Modifier = drawBehind {
    val cr = cornerRadius.toPx()
    val sp = spread.toPx()
    val ringWidth = sp / steps + 1f
    for (i in 1..steps) {
        val t = i / steps.toFloat()
        val inflate = sp * t
        drawRoundRect(
            color = color.copy(alpha = alpha * (1f - t) * (1f - t)),
            topLeft = Offset(-inflate, -inflate),
            size = Size(size.width + inflate * 2, size.height + inflate * 2),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cr + inflate),
            style = Stroke(width = ringWidth),
        )
    }
}

/** Thin neon outline for cards and chips. */
fun Modifier.neonBorder(
    color: Color,
    cornerRadius: Dp = ArcadeRadii.card,
    width: Dp = 1.dp,
    alpha: Float = 0.8f,
): Modifier = border(width, color.copy(alpha = alpha), RoundedCornerShape(cornerRadius))

// ----------------------------------------------------------------------------
// Theme
// ----------------------------------------------------------------------------

/**
 * Arcade theme: always dark. Provides [Arcade.type] and maps the arcade
 * palette onto Material3 so stock components don't clash.
 */
@Composable
fun ArcadeTheme(content: @Composable () -> Unit) {
    val type = arcadeTypography()
    val scheme = darkColorScheme(
        primary = ArcadeColors.Ember,
        onPrimary = ArcadeColors.OnEmber,
        primaryContainer = ArcadeColors.EmberDim,
        onPrimaryContainer = ArcadeColors.Ember,
        secondary = ArcadeColors.Cyan,
        onSecondary = ArcadeColors.Ink,
        tertiary = ArcadeColors.Pink,
        onTertiary = ArcadeColors.Ink,
        background = ArcadeColors.Ink,
        onBackground = ArcadeColors.Text,
        surface = ArcadeColors.Surface,
        onSurface = ArcadeColors.Text,
        surfaceVariant = ArcadeColors.SurfaceRaised,
        onSurfaceVariant = ArcadeColors.TextMuted,
        outline = ArcadeColors.Hairline,
        error = ArcadeColors.Pink,
        onError = ArcadeColors.Ink,
    )
    val materialType = MaterialTheme.typography.copy(
        headlineLarge = type.headline,
        titleLarge = type.title,
        bodyLarge = type.body,
        bodyMedium = type.bodySmall,
        labelLarge = type.label,
    )
    CompositionLocalProvider(LocalArcadeTypography provides type) {
        MaterialTheme(colorScheme = scheme, typography = materialType, content = content)
    }
}
