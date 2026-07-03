package org.example.kotlinconference101

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinconference101.composeapp.generated.resources.Res
import kotlinconference101.composeapp.generated.resources.myprofile
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onNavigateToProjects: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Staggered reveal: each stage lights up 300ms after the previous one.
    var stage by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (stage < 4) {
            stage++
            delay(300)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Arcade.colors.Ink)
            .padding(horizontal = Arcade.spacing.lg, vertical = Arcade.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Arcade.spacing.xl)
    ) {
        Spacer(modifier = Modifier.height(Arcade.spacing.xxl))

        // Profile image in a neon ember ring
        AnimatedVisibility(
            visible = stage >= 1,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -100 })
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .neonGlow(Arcade.colors.Ember, radius = 36.dp, alpha = 0.4f)
                    .neonBorder(Arcade.colors.Ember, cornerRadius = 160.dp, width = 2.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.myprofile),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(CircleShape)
                )
            }
        }

        // Support links, shown right below the profile image
        AnimatedVisibility(
            visible = stage >= 1,
            enter = fadeIn()
        ) {
            val contact = PortfolioDataProvider.contact
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md),
                verticalArrangement = Arrangement.spacedBy(Arcade.spacing.sm),
                modifier = Modifier.padding(horizontal = Arcade.spacing.lg)
            ) {
                if (contact.calCom.isNotEmpty()) {
                    SupportLink(emoji = "📅", label = "Book a Call", url = contact.calCom, accent = Arcade.colors.Cyan)
                }
                if (contact.buyMeACoffee.isNotEmpty()) {
                    SupportLink(emoji = "☕", label = "Buy Me a Coffee", url = contact.buyMeACoffee, accent = Arcade.colors.Amber)
                }
                if (contact.githubSponsors.isNotEmpty()) {
                    SupportLink(emoji = "💜", label = "GitHub Sponsors", url = contact.githubSponsors, accent = Arcade.colors.Purple)
                }
            }
        }

        // Player tag + name
        AnimatedVisibility(
            visible = stage >= 2,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 30 })
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "PLAYER 1",
                    style = Arcade.type.arcadeCaption,
                    color = Arcade.colors.Ember
                )
                Spacer(modifier = Modifier.height(Arcade.spacing.sm))
                StickerText(
                    text = "Clency Christine",
                    style = Arcade.type.displayL,
                    outlineWidth = 3.dp
                )
            }
        }

        // Tagline + quick description
        AnimatedVisibility(
            visible = stage >= 3,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 30 })
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Arcade.spacing.md)
            ) {
                Text(
                    text = "MOBILE + BACKEND DEV",
                    style = Arcade.type.arcadeCaption,
                    color = Arcade.colors.Cyan,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "OPEN SOURCE ENTHUSIAST",
                    style = Arcade.type.arcadeCaption,
                    color = Arcade.colors.Purple,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Designing and building scalable, robust backend systems | Expert in mobile development with a focus on backend architecture | Actively learning modern design systems",
                    style = Arcade.type.body,
                    color = Arcade.colors.TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = Arcade.spacing.lg)
                )
            }
        }

        // Action buttons
        AnimatedVisibility(
            visible = stage >= 4,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Arcade.spacing.lg),
                horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onNavigateToProjects,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Arcade.colors.Ember,
                        contentColor = Arcade.colors.OnEmber
                    ),
                    shape = RoundedCornerShape(Arcade.radii.chip)
                ) {
                    Text("PRESS START", style = Arcade.type.arcadeCaption, color = Arcade.colors.OnEmber)
                }

                OutlinedButton(
                    onClick = onNavigateToAbout,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    border = BorderStroke(1.dp, Arcade.colors.Ember),
                    shape = RoundedCornerShape(Arcade.radii.chip)
                ) {
                    Text("PLAYER INFO", style = Arcade.type.arcadeCaption, color = Arcade.colors.Ember)
                }
            }
        }

        // Highlights
        AnimatedVisibility(
            visible = stage >= 4,
            enter = fadeIn()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Arcade.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(Arcade.spacing.lg)
            ) {
                HighlightCard(
                    emoji = "🕹️",
                    accent = Arcade.colors.Ember,
                    title = "Backend & Mobile Mastery",
                    description = "Building scalable backend systems and cross-platform mobile applications with expertise in Kotlin Multiplatform and modern server architecture"
                )

                HighlightCard(
                    emoji = "🏗️",
                    accent = Arcade.colors.Cyan,
                    title = "Scalable System Design",
                    description = "Designing robust backend architectures with clean code principles, SOLID design patterns, and production-ready best practices"
                )

                HighlightCard(
                    emoji = "✨",
                    accent = Arcade.colors.Purple,
                    title = "Design Systems & UX",
                    description = "Mastering Material Design 3 and modern design systems for beautiful, functional mobile experiences backed by solid architecture"
                )
            }
        }

        Spacer(modifier = Modifier.height(Arcade.spacing.xxl))
    }
}

@Composable
private fun SupportLink(
    emoji: String,
    label: String,
    url: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    Row(
        modifier = modifier
            .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.pill))
            .neonBorder(accent, cornerRadius = Arcade.radii.pill, alpha = 0.5f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { uriHandler.openUri(url) }
            .padding(horizontal = Arcade.spacing.lg, vertical = Arcade.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, style = Arcade.type.bodySmall)
        Text(
            text = label,
            style = Arcade.type.bodySmall,
            color = Arcade.colors.Text,
            maxLines = 1
        )
    }
}

@Composable
private fun HighlightCard(
    emoji: String,
    accent: Color,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    NeonBadgeCard(
        accent = accent,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Arcade.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.lg),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.chip))
                    .neonBorder(accent, cornerRadius = Arcade.radii.chip, alpha = 0.4f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, style = Arcade.type.title)
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Arcade.spacing.xs)
            ) {
                Text(
                    text = title,
                    style = Arcade.type.title,
                    color = accent
                )
                Text(
                    text = description,
                    style = Arcade.type.bodySmall,
                    color = Arcade.colors.TextMuted
                )
            }
        }
    }
}
