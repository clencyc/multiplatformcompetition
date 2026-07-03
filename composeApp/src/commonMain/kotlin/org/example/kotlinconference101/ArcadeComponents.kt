package org.example.kotlinconference101

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

// ============================================================================
// Signature arcade components — Phase 2 implementations.
// ============================================================================

/**
 * Dashed-arc gauge: progress drawn as discrete neon segments around a ring,
 * remaining segments as dim track, [content] (usually a pixel-font number)
 * centered inside.
 */

@Composable
fun SegmentedProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    segments: Int = 32,
    color: Color = ArcadeColors.Ember,
    trackColor: Color = ArcadeColors.SurfaceRaised,
    strokeWidth: Dp = 10.dp,
    glow: Boolean = true,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val lit = (progress.coerceIn(0f, 1f) * segments).roundToInt()
    Box(
        modifier = modifier.then(if (glow) Modifier.neonGlow(color, alpha = 0.3f) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.matchParentSize()) {
            val stroke = strokeWidth.toPx()
            val inset = stroke / 2f
            val arcSize = Size(size.width - stroke, size.height - stroke)
            val sweepPerSegment = 360f / segments
            val gap = sweepPerSegment * 0.35f
            for (i in 0 until segments) {
                drawArc(
                    color = if (i < lit) color else trackColor,
                    startAngle = -90f + i * sweepPerSegment + gap / 2f,
                    sweepAngle = sweepPerSegment - gap,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = arcSize,
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                )
            }
        }
        content()
    }
}

/**
 * Horizontal cousin of [SegmentedProgressRing]: progress as a row of small
 * rounded rects, lit segments in [color], the rest as dim track.
 */
@Composable
fun SegmentedBar(
    progress: Float,
    modifier: Modifier = Modifier,
    segments: Int = 20,
    color: Color = ArcadeColors.Ember,
    trackColor: Color = ArcadeColors.SurfaceRaised,
    height: Dp = 8.dp,
) {
    val lit = (progress.coerceIn(0f, 1f) * segments).roundToInt()
    Canvas(modifier.height(height)) {
        val gap = size.height * 0.5f
        val segWidth = (size.width - gap * (segments - 1)) / segments
        val corner = CornerRadius(size.height * 0.3f)
        for (i in 0 until segments) {
            drawRoundRect(
                color = if (i < lit) color else trackColor,
                topLeft = Offset(i * (segWidth + gap), 0f),
                size = Size(segWidth, size.height),
                cornerRadius = corner,
            )
        }
    }
}

/**
 * Sticker-style text: white fill over a thick black outline (layered
 * stroke + fill draw), like a die-cut sticker on the dark background.
 */
@Composable
fun StickerText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    fillColor: Color = ArcadeColors.StickerFill,
    outlineColor: Color = ArcadeColors.StickerOutline,
    outlineWidth: Dp = 4.dp,
) {
    val outlinePx = with(LocalDensity.current) { outlineWidth.toPx() }
    Box(modifier) {
        Text(
            text = text,
            style = style.copy(
                color = outlineColor,
                drawStyle = Stroke(width = outlinePx * 2, cap = StrokeCap.Round),
            ),
        )
        Text(text = text, style = style.copy(color = fillColor))
    }
}

/**
 * Dark card with a thin neon border and matching edge glow in the accent
 * color; the standard container for stats, badges and game tiles.
 */
@Composable
fun NeonBadgeCard(
    accent: Color,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = ArcadeRadii.card,
    glow: Boolean = true,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .then(if (glow) Modifier.neonEdgeGlow(accent, cornerRadius = cornerRadius, alpha = 0.25f) else Modifier)
            .background(ArcadeColors.Surface, RoundedCornerShape(cornerRadius))
            .neonBorder(accent, cornerRadius = cornerRadius, alpha = 0.7f),
        content = content,
    )
}

/**
 * Horizontal run of day markers for a streak: filled neon dots/flames for
 * hit days, dim hollow markers for missed ones.
 */
@Composable
fun StreakRow(
    days: List<Boolean>,
    modifier: Modifier = Modifier,
    accent: Color = ArcadeColors.Amber,
    dotSize: Dp = 8.dp,
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(dotSize / 2)) {
        days.forEach { hit ->
            Box(
                Modifier
                    .size(dotSize)
                    .then(
                        if (hit) Modifier
                            .neonGlow(accent, radius = dotSize / 2, alpha = 0.35f)
                            .background(accent, CircleShape)
                        else Modifier.neonBorder(
                            ArcadeColors.Hairline,
                            cornerRadius = dotSize,
                            alpha = 1f,
                        )
                    )
            )
        }
    }
}

/**
 * Round tappable chip showing a single emoji on a raised dark surface with
 * an accent glow ring.
 */
@Composable
fun EmojiIconButton(
    emoji: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = ArcadeColors.Ember,
    size: Dp = 56.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .neonGlow(accent, radius = size / 4, alpha = 0.4f)
            .background(ArcadeColors.SurfaceRaised, CircleShape)
            .neonBorder(accent, cornerRadius = size, alpha = 0.8f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = emoji, style = TextStyle(fontSize = with(LocalDensity.current) { (size / 2.4f).toSp() }))
    }
}
