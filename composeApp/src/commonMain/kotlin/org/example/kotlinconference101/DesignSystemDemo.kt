package org.example.kotlinconference101

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Phase 1 gallery: renders every design token — palette, type ramp, glow
 * effects, borders, radii. Dev-only; not reachable from app navigation.
 */
@Composable
fun DesignSystemDemo() {
    ArcadeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Arcade.colors.Ink)
                .verticalScroll(rememberScrollState())
                .padding(Arcade.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Arcade.spacing.xl),
        ) {
            DemoSection("COLORS") {
                Row(horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md)) {
                    Swatch("EMBER", Arcade.colors.Ember)
                    Swatch("PINK", Arcade.colors.Pink)
                    Swatch("PURPLE", Arcade.colors.Purple)
                    Swatch("AMBER", Arcade.colors.Amber)
                    Swatch("CYAN", Arcade.colors.Cyan)
                }
                Spacer(Modifier.height(Arcade.spacing.md))
                Row(horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md)) {
                    Swatch("INK", Arcade.colors.Ink, bordered = true)
                    Swatch("SURFACE", Arcade.colors.Surface, bordered = true)
                    Swatch("RAISED", Arcade.colors.SurfaceRaised, bordered = true)
                    Swatch("TEXT", Arcade.colors.Text)
                    Swatch("MUTED", Arcade.colors.TextMuted)
                }
            }

            DemoSection("TYPE — PIXEL DISPLAY (JERSEY 25)") {
                Text("2,048", style = Arcade.type.displayXL, color = Arcade.colors.Ember)
                Text("86%", style = Arcade.type.displayL, color = Arcade.colors.Cyan)
                Text("LEVEL 12", style = Arcade.type.displayM, color = Arcade.colors.Amber)
            }

            DemoSection("TYPE — ARCADE CAPTION (SILKSCREEN)") {
                Text("HI-SCORE 9999", style = Arcade.type.arcadeCaption, color = Arcade.colors.Pink)
                Text(
                    "INSERT COIN",
                    style = Arcade.type.arcadeCaption.copy(fontWeight = FontWeight.Bold),
                    color = Arcade.colors.Ember,
                )
            }

            DemoSection("TYPE — BODY (SPACE GROTESK)") {
                Text("Headline: Beat your streak", style = Arcade.type.headline)
                Text("Title: Weekly progress", style = Arcade.type.title)
                Text(
                    "Body: The quick brown fox jumps over the lazy dog, then " +
                        "grabs a power-up and clears the level.",
                    style = Arcade.type.body,
                )
                Text("Body small: last synced 2 min ago", style = Arcade.type.bodySmall)
                Text("MICRO LABEL · UPPERCASE · SPACED", style = Arcade.type.label)
            }

            DemoSection("EFFECT — NEON GLOW (RADIAL HALO)") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Arcade.spacing.xl),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    GlowDot(Arcade.colors.Ember)
                    GlowDot(Arcade.colors.Pink)
                    GlowDot(Arcade.colors.Purple)
                    GlowDot(Arcade.colors.Amber)
                    GlowDot(Arcade.colors.Cyan)
                }
            }

            DemoSection("EFFECT — NEON CARDS (EDGE GLOW + BORDER)") {
                NeonDemoCard(Arcade.colors.Ember, "DAILY GOAL", "420")
                Spacer(Modifier.height(Arcade.spacing.lg))
                NeonDemoCard(Arcade.colors.Pink, "STREAK", "17")
                Spacer(Modifier.height(Arcade.spacing.lg))
                NeonDemoCard(Arcade.colors.Cyan, "XP EARNED", "1,250")
            }

            DemoSection("COMPONENTS — RING / STICKER / STREAK / EMOJI") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Arcade.spacing.lg),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SegmentedProgressRing(
                        progress = 0.7f,
                        modifier = Modifier.size(96.dp),
                        color = Arcade.colors.Ember,
                    ) {
                        Text("70", style = Arcade.type.displayM, color = Arcade.colors.Ember)
                    }
                    EmojiIconButton(emoji = "🎙", onClick = {}, accent = Arcade.colors.Cyan)
                }
                StickerText("LEVEL UP!", style = Arcade.type.displayM)
                Spacer(Modifier.height(Arcade.spacing.md))
                StreakRow(days = listOf(true, true, true, false, true, false, true), dotSize = 12.dp)
            }

            DemoSection("RADII") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadiusChip("chip 12", Arcade.radii.chip)
                    RadiusChip("badge 16", Arcade.radii.badge)
                    RadiusChip("card 24", Arcade.radii.card)
                    RadiusChip("pill", Arcade.radii.pill)
                }
            }

            DemoSection("SPACING") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    SpacingBar("xs", Arcade.spacing.xs)
                    SpacingBar("sm", Arcade.spacing.sm)
                    SpacingBar("md", Arcade.spacing.md)
                    SpacingBar("lg", Arcade.spacing.lg)
                    SpacingBar("xl", Arcade.spacing.xl)
                    SpacingBar("xxl", Arcade.spacing.xxl)
                }
            }
        }
    }
}

@Composable
private fun DemoSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(title, style = Arcade.type.label)
        Spacer(Modifier.height(Arcade.spacing.md))
        content()
    }
}

@Composable
private fun Swatch(name: String, color: Color, bordered: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(52.dp)
                .then(
                    if (bordered) Modifier.neonBorder(
                        Arcade.colors.Hairline,
                        cornerRadius = Arcade.radii.badge,
                        alpha = 1f,
                    ) else Modifier
                )
                .background(color, RoundedCornerShape(Arcade.radii.badge))
        )
        Spacer(Modifier.height(Arcade.spacing.xs))
        Text(name, style = Arcade.type.label)
    }
}

@Composable
private fun GlowDot(color: Color) {
    Box(
        Modifier
            .size(44.dp)
            .neonGlow(color, radius = 26.dp)
            .background(color, CircleShape)
    )
}

@Composable
private fun NeonDemoCard(accent: Color, label: String, value: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .neonEdgeGlow(accent)
            .background(Arcade.colors.Surface, RoundedCornerShape(Arcade.radii.card))
            .neonBorder(accent)
            .padding(Arcade.spacing.lg)
    ) {
        Column {
            Text(label, style = Arcade.type.label, color = accent)
            Text(value, style = Arcade.type.displayM, color = accent)
        }
    }
}

@Composable
private fun RadiusChip(label: String, radius: Dp) {
    Box(
        Modifier
            .neonBorder(Arcade.colors.Purple, cornerRadius = radius)
            .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(radius))
            .padding(horizontal = Arcade.spacing.lg, vertical = Arcade.spacing.md)
    ) {
        Text(label, style = Arcade.type.bodySmall, color = Arcade.colors.Text)
    }
}

@Composable
private fun SpacingBar(label: String, value: Dp) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .width(value)
                .height(value * 2)
                .background(Arcade.colors.EmberDim, RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.height(Arcade.spacing.xs))
        Text(label, style = Arcade.type.label)
    }
}
