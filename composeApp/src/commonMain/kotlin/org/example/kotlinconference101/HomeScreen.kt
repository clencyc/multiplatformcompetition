package org.example.kotlinconference101

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinconference101.composeapp.generated.resources.Res
import kotlinconference101.composeapp.generated.resources.myprofile
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onNavigateToProjects: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showProfileImage by remember { mutableStateOf(false) }
    var showName by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }
    var showHighlights by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showProfileImage = true
    }

    LaunchedEffect(showProfileImage) {
        if (showProfileImage) {
            kotlinx.coroutines.delay(300)
            showName = true
        }
    }

    LaunchedEffect(showName) {
        if (showName) {
            kotlinx.coroutines.delay(300)
            showTagline = true
        }
    }

    LaunchedEffect(showTagline) {
        if (showTagline) {
            kotlinx.coroutines.delay(300)
            showButtons = true
        }
    }

    LaunchedEffect(showButtons) {
        if (showButtons) {
            kotlinx.coroutines.delay(300)
            showHighlights = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Spacer for top spacing
        Spacer(modifier = Modifier.height(32.dp))

        // Profile Image with fade-in animation
        AnimatedVisibility(
            visible = showProfileImage,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -100 })
        ) {
            Surface(
                modifier = Modifier
                    .size(160.dp)
                    .shadow(elevation = 16.dp, shape = CircleShape),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Image(
                    painter = painterResource(Res.drawable.myprofile),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }

        // Name with fade-in animation
        AnimatedVisibility(
            visible = showName,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 30 })
        ) {
            Text(
                text = "Clency Christine",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }

        // Tagline with fade-in animation
        AnimatedVisibility(
            visible = showTagline,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 30 })
        ) {
            Text(
                text = "Mobile & Backend Developer",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }

        // Quick Description
        AnimatedVisibility(
            visible = showTagline,
            enter = fadeIn()
        ) {
            Text(
                text = "Designing and building scalable, robust backend systems | Expert in mobile development with a focus on backend architecture | Actively learning modern design systems",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Action Buttons with fade-in animation
        AnimatedVisibility(
            visible = showButtons,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onNavigateToProjects,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = "Projects",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                    Text("View Projects")
                }

                OutlinedButton(
                    onClick = onNavigateToAbout,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "About",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                    Text("Learn More")
                }
            }
        }

        // Highlights with staggered animation
        AnimatedVisibility(
            visible = showHighlights,
            enter = fadeIn()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HighlightCard(
                    icon = Icons.Default.Code,
                    title = "Backend & Mobile Mastery",
                    description = "Building scalable backend systems and cross-platform mobile applications with expertise in Kotlin Multiplatform and modern server architecture"
                )

                HighlightCard(
                    icon = Icons.Default.Description,
                    title = "Scalable System Design",
                    description = "Designing robust backend architectures with clean code principles, SOLID design patterns, and production-ready best practices"
                )

                HighlightCard(
                    icon = Icons.Default.Info,
                    title = "Design Systems & UX",
                    description = "Mastering Material Design 3 and modern design systems for beautiful, functional mobile experiences backed by solid architecture"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun HighlightCard(
    icon: ImageVector = Icons.Default.Code,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
