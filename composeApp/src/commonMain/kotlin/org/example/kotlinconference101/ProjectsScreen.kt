package org.example.kotlinconference101

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private fun projectMatches(project: Project, filter: FilterType): Boolean = when (filter) {
    FilterType.ALL -> true
    FilterType.KOTLIN -> project.technologies.any { it.equals("Kotlin", ignoreCase = true) }
    FilterType.PYTHON -> project.technologies.any { it.equals("Python", ignoreCase = true) }
    FilterType.AI_AGENTS -> project.technologies.any {
        it.contains("Machine Learning", ignoreCase = true) || it.contains("NLP", ignoreCase = true)
    } || project.title.contains("AI", ignoreCase = true) || project.description.contains("AI-powered", ignoreCase = true)
}

private fun statusColor(status: QuestStatus): Color = when (status) {
    QuestStatus.CLEARED -> ArcadeColors.Cyan
    QuestStatus.IN_PROGRESS -> ArcadeColors.Amber
    QuestStatus.BOSS_CLEARED -> ArcadeColors.Pink
}

/** Boss toasts fire once per session; survives leaving and re-entering the screen. */
private val bossToastShownIds = mutableSetOf<String>()

@Composable
fun ProjectsScreen(
    modifier: Modifier = Modifier,
    filter: FilterType = FilterType.ALL,
    onClearFilter: () -> Unit = {}
) {
    var selectedProject by remember { mutableStateOf<Project?>(null) }

    val selected = selectedProject
    if (selected != null) {
        QuestDetailScreen(
            project = selected,
            onBack = { selectedProject = null },
            modifier = modifier
        )
    } else {
        QuestLogScreen(
            filter = filter,
            onClearFilter = onClearFilter,
            onQuestSelected = { selectedProject = it },
            modifier = modifier
        )
    }
}

// ----------------------------------------------------------------------------
// Quest log list
// ----------------------------------------------------------------------------

@Composable
private fun QuestLogScreen(
    filter: FilterType,
    onClearFilter: () -> Unit,
    onQuestSelected: (Project) -> Unit,
    modifier: Modifier = Modifier
) {
    val projects = PortfolioDataProvider.projects
    val filteredProjects = remember(filter) { projects.filter { projectMatches(it, filter) } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Arcade.colors.Ink)
    ) {
        QuestLogHeader(
            filter = filter,
            onClearFilter = onClearFilter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Arcade.spacing.xl)
        )

        if (filteredProjects.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Text(
                    text = QuestLogCopy.EMPTY_FILTER,
                    style = Arcade.type.body,
                    color = Arcade.colors.TextMuted,
                    modifier = Modifier
                        .padding(Arcade.spacing.xl)
                        .align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(
                    start = Arcade.spacing.lg,
                    end = Arcade.spacing.lg,
                    bottom = Arcade.spacing.xl
                ),
                verticalArrangement = Arrangement.spacedBy(Arcade.spacing.lg)
            ) {
                itemsIndexed(filteredProjects, key = { _, p -> p.id }) { index, project ->
                    QuestCard(
                        project = project,
                        accent = Arcade.colors.accents[index % Arcade.colors.accents.size],
                        staggerIndex = index,
                        onClick = { onQuestSelected(project) }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestLogHeader(
    filter: FilterType,
    onClearFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val earnedXp = PortfolioDataProvider.earnedQuestXp
    val totalXp = PortfolioDataProvider.totalQuestXp
    val level = PortfolioDataProvider.playerLevel

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Arcade.spacing.sm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = QuestLogCopy.TITLE,
                style = Arcade.type.displayM,
                color = Arcade.colors.Ember
            )
            // Player level pill
            Box(
                modifier = Modifier
                    .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.pill))
                    .neonBorder(Arcade.colors.Amber, cornerRadius = Arcade.radii.pill, alpha = 0.7f)
                    .padding(horizontal = Arcade.spacing.md, vertical = 6.dp)
            ) {
                Text(
                    text = "${QuestLogCopy.LEVEL_PREFIX} $level",
                    style = Arcade.type.arcadeCaption,
                    color = Arcade.colors.Amber
                )
            }
        }

        Text(
            text = QuestLogCopy.SUBTITLE,
            style = Arcade.type.bodySmall,
            color = Arcade.colors.TextMuted
        )

        // Overall XP from cleared quests
        SegmentedBar(
            progress = if (totalXp > 0) earnedXp.toFloat() / totalXp else 0f,
            modifier = Modifier.fillMaxWidth(),
            segments = 24,
            color = Arcade.colors.Amber
        )
        Text(
            text = "$earnedXp / $totalXp ${QuestLogCopy.XP_SUFFIX}",
            style = Arcade.type.label,
            color = Arcade.colors.TextFaint
        )

        if (filter != FilterType.ALL) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.sm)
            ) {
                Box(
                    modifier = Modifier
                        .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.chip))
                        .neonBorder(Arcade.colors.Amber, cornerRadius = Arcade.radii.chip, alpha = 0.6f)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${QuestLogCopy.FILTER_PREFIX} ${filter.label.uppercase()}",
                        style = Arcade.type.label,
                        color = Arcade.colors.Amber
                    )
                }
                Text(
                    text = QuestLogCopy.CLEAR_FILTER,
                    style = Arcade.type.label,
                    color = Arcade.colors.Ember,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClearFilter() }
                )
            }
        }
    }
}

// ----------------------------------------------------------------------------
// Quest card
// ----------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuestCard(
    project: Project,
    accent: Color,
    staggerIndex: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Staggered slide+fade the first time the card composes (≈ enters viewport)
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(staggerIndex * 70L)
        visible = true
    }

    // One-time boss toast per session
    var showToast by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (project.status == QuestStatus.BOSS_CLEARED && bossToastShownIds.add(project.id)) {
            delay(staggerIndex * 70L + 400L)
            showToast = true
            delay(1400L)
            showToast = false
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(250)) + slideInVertically(animationSpec = tween(250), initialOffsetY = { it / 4 })
    ) {
        Box {
            NeonBadgeCard(
                accent = accent,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClick() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(Arcade.spacing.md)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RankBadge(rank = project.rank)

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.xs),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = project.questType.label,
                                    style = Arcade.type.label,
                                    color = Arcade.colors.TextFaint
                                )
                                if (project.questType == QuestType.BOSS) {
                                    Text(text = QuestLogCopy.BOSS_TROPHY, style = Arcade.type.label)
                                }
                            }
                            Text(
                                text = project.title,
                                style = Arcade.type.title,
                                color = accent,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Text(
                            text = "+${project.xp} ${QuestLogCopy.XP_SUFFIX}",
                            style = Arcade.type.displayM.copy(fontSize = 22.sp, lineHeight = 24.sp),
                            color = Arcade.colors.Amber
                        )
                    }

                    Text(
                        text = project.description,
                        style = Arcade.type.bodySmall,
                        color = Arcade.colors.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusChip(status = project.status)
                        SegmentedBar(
                            progress = project.progress,
                            modifier = Modifier.weight(1f),
                            segments = 16,
                            color = statusColor(project.status),
                            height = 6.dp
                        )
                    }

                    if (project.technologies.isNotEmpty()) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.sm),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            project.technologies.forEachIndexed { i, tech ->
                                LootChip(
                                    text = tech,
                                    accent = Arcade.colors.accents[i % Arcade.colors.accents.size]
                                )
                            }
                        }
                    }
                }
            }

            // "YOU DID IT!" sticker toast for freshly-seen boss clears
            AnimatedVisibility(
                visible = showToast,
                enter = scaleIn(tween(200), initialScale = 0.4f) + fadeIn(tween(200)),
                exit = fadeOut(tween(350)),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = Arcade.spacing.sm, end = Arcade.spacing.lg)
            ) {
                StickerText(
                    text = QuestLogCopy.BOSS_TOAST,
                    style = Arcade.type.displayM.copy(fontSize = 28.sp, lineHeight = 30.sp),
                    outlineWidth = 5.dp,
                    modifier = Modifier.rotate(-8f)
                )
            }
        }
    }
}

@Composable
private fun RankBadge(rank: QuestRank, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.chip))
            .neonBorder(Arcade.colors.Amber, cornerRadius = Arcade.radii.chip, alpha = 0.6f),
        contentAlignment = Alignment.Center
    ) {
        StickerText(
            text = rank.label,
            style = Arcade.type.displayM.copy(fontSize = 26.sp, lineHeight = 28.sp),
            fillColor = Arcade.colors.Amber,
            outlineWidth = 3.dp
        )
    }
}

@Composable
private fun StatusChip(status: QuestStatus, modifier: Modifier = Modifier) {
    val color = statusColor(status)
    Box(
        modifier = modifier
            .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.chip / 2))
            .neonBorder(color, cornerRadius = Arcade.radii.chip / 2, alpha = 0.6f)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.label,
            style = Arcade.type.label,
            color = color
        )
    }
}

@Composable
private fun LootChip(
    text: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.chip / 2))
            .neonBorder(accent, cornerRadius = Arcade.radii.chip / 2, alpha = 0.45f)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            style = Arcade.type.label,
            color = Arcade.colors.TextMuted
        )
    }
}

// ----------------------------------------------------------------------------
// Mission briefing (quest detail)
// ----------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuestDetailScreen(
    project: Project,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = statusColor(project.status)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Arcade.colors.Ink)
            .verticalScroll(rememberScrollState())
            .padding(Arcade.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Arcade.spacing.lg)
    ) {
        Text(
            text = QuestLogCopy.BACK,
            style = Arcade.type.arcadeCaption,
            color = Arcade.colors.Ember,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onBack() }
        )

        Text(
            text = QuestLogCopy.BRIEFING,
            style = Arcade.type.label,
            color = Arcade.colors.TextFaint
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RankBadge(rank = project.rank)
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.questType.label,
                        style = Arcade.type.label,
                        color = Arcade.colors.TextFaint
                    )
                    if (project.questType == QuestType.BOSS) {
                        Text(text = QuestLogCopy.BOSS_TROPHY, style = Arcade.type.label)
                    }
                }
                Text(
                    text = project.title,
                    style = Arcade.type.displayM,
                    color = Arcade.colors.Text
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusChip(status = project.status)
            Text(
                text = "+${project.xp} ${QuestLogCopy.XP_SUFFIX}",
                style = Arcade.type.arcadeCaption,
                color = Arcade.colors.Amber
            )
        }

        SegmentedBar(
            progress = project.progress,
            modifier = Modifier.fillMaxWidth(),
            segments = 24,
            color = accent
        )

        // Objective — typewriter reveal, tap to skip
        NeonBadgeCard(accent = accent, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Arcade.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(Arcade.spacing.sm)
            ) {
                Text(
                    text = QuestLogCopy.OBJECTIVE,
                    style = Arcade.type.arcadeCaption,
                    color = accent
                )
                TypewriterText(
                    text = project.description,
                    style = Arcade.type.body,
                    color = Arcade.colors.Text
                )
            }
        }

        // Tactics
        if (project.features.isNotEmpty()) {
            NeonBadgeCard(accent = Arcade.colors.Purple, modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Arcade.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(Arcade.spacing.sm)
                ) {
                    Text(
                        text = QuestLogCopy.TACTICS,
                        style = Arcade.type.arcadeCaption,
                        color = Arcade.colors.Purple
                    )
                    project.features.forEach { feature ->
                        Text(
                            text = "${QuestLogCopy.TACTIC_BULLET} $feature",
                            style = Arcade.type.bodySmall,
                            color = Arcade.colors.TextMuted
                        )
                    }
                }
            }
        }

        // Loot acquired
        if (project.technologies.isNotEmpty()) {
            NeonBadgeCard(accent = Arcade.colors.Amber, modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Arcade.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(Arcade.spacing.sm)
                ) {
                    Text(
                        text = QuestLogCopy.LOOT,
                        style = Arcade.type.arcadeCaption,
                        color = Arcade.colors.Amber
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.sm),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        project.technologies.forEachIndexed { i, tech ->
                            LootChip(
                                text = tech,
                                accent = Arcade.colors.accents[i % Arcade.colors.accents.size]
                            )
                        }
                    }
                }
            }
        }

        // Links
        if (project.githubUrl.isNotEmpty() || project.liveUrl.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md)
            ) {
                if (project.githubUrl.isNotEmpty()) {
                    QuestLink(
                        emoji = QuestLogCopy.GITHUB_EMOJI,
                        label = QuestLogCopy.GITHUB_LINK,
                        url = project.githubUrl,
                        accent = accent,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (project.liveUrl.isNotEmpty()) {
                    QuestLink(
                        emoji = QuestLogCopy.LIVE_EMOJI,
                        label = QuestLogCopy.LIVE_LINK,
                        url = project.liveUrl,
                        accent = accent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Reveals [text] character by character over at most 1.4s; a tap anywhere on
 * the text skips straight to the full string.
 */
@Composable
private fun TypewriterText(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier
) {
    var visibleChars by remember(text) { mutableStateOf(0) }

    LaunchedEffect(text) {
        val durationMs = 1400L
        val tickMs = 30L
        var elapsed = 0L
        while (visibleChars < text.length) {
            delay(tickMs)
            elapsed += tickMs
            visibleChars = ((elapsed.toFloat() / durationMs) * text.length)
                .toInt()
                .coerceAtMost(text.length)
        }
    }

    Text(
        text = text.take(visibleChars),
        style = style,
        color = color,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { visibleChars = text.length }
    )
}

@Composable
private fun QuestLink(
    emoji: String,
    label: String,
    url: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    Row(
        modifier = modifier
            .height(44.dp)
            .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.chip))
            .neonBorder(accent, cornerRadius = Arcade.radii.chip, alpha = 0.6f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { uriHandler.openUri(url) },
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, style = Arcade.type.bodySmall, color = accent)
        Text(
            text = label,
            style = Arcade.type.label,
            color = accent
        )
    }
}
