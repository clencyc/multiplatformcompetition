package org.example.kotlinconference101

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val data = PortfolioDataProvider

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Arcade.colors.Ink)
            .verticalScroll(scrollState)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Arcade.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Arcade.spacing.sm)
        ) {
            Text(
                text = "PLAYER PROFILE",
                style = Arcade.type.arcadeCaption,
                color = Arcade.colors.Ember
            )
            Text(
                text = "Learn more about me and my journey",
                style = Arcade.type.body,
                color = Arcade.colors.TextMuted
            )
        }

        // Bio Section
        BioSection(bio = data.bio)

        // Fun Fact
        FunFactCard(fact = data.funFact)

        // Experience Section
        ExperienceSection(experiences = data.experiences)

        // Skills Section
        SkillsSection(skills = data.skills)

        // Contact Section
        ContactSection(contact = data.contact)

        // Resume Download Button
        Button(
            onClick = { /* Handle resume download */ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(Arcade.spacing.xl),
            colors = ButtonDefaults.buttonColors(
                containerColor = Arcade.colors.Ember,
                contentColor = Arcade.colors.OnEmber
            ),
            shape = RoundedCornerShape(Arcade.radii.chip)
        ) {
            Text(
                text = "DOWNLOAD RESUME",
                style = Arcade.type.arcadeCaption,
                color = Arcade.colors.OnEmber,
                modifier = Modifier.padding(horizontal = Arcade.spacing.lg)
            )
        }

        Spacer(modifier = Modifier.height(Arcade.spacing.xl))
    }
}

@Composable
private fun SectionTitle(text: String, color: Color = Arcade.colors.Ember) {
    Text(
        text = text,
        style = Arcade.type.arcadeCaption,
        color = color
    )
}

@Composable
private fun BioSection(
    bio: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Arcade.spacing.lg)
            .padding(bottom = Arcade.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Arcade.spacing.md)
    ) {
        SectionTitle("BIO")

        NeonBadgeCard(
            accent = Arcade.colors.Ember,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = bio,
                style = Arcade.type.body,
                color = Arcade.colors.TextMuted,
                modifier = Modifier.padding(Arcade.spacing.lg)
            )
        }
    }
}

@Composable
private fun FunFactCard(
    fact: String,
    modifier: Modifier = Modifier
) {
    NeonBadgeCard(
        accent = Arcade.colors.Amber,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Arcade.spacing.lg)
            .padding(bottom = Arcade.spacing.xl)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(Arcade.spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✨ FUN FACT",
                style = Arcade.type.arcadeCaption,
                color = Arcade.colors.Amber
            )
            Text(
                text = fact,
                style = Arcade.type.body,
                color = Arcade.colors.Text,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ExperienceSection(
    experiences: List<Experience>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Arcade.spacing.lg)
            .padding(bottom = Arcade.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Arcade.spacing.md)
    ) {
        SectionTitle("EXPERIENCE")

        experiences.forEachIndexed { index, experience ->
            ExperienceCard(
                experience = experience,
                accent = Arcade.colors.accents[index % Arcade.colors.accents.size]
            )
        }
    }
}

@Composable
private fun ExperienceCard(
    experience: Experience,
    accent: Color,
    modifier: Modifier = Modifier
) {
    NeonBadgeCard(
        accent = accent,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Arcade.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Arcade.spacing.sm)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = experience.title,
                        style = Arcade.type.title,
                        color = accent
                    )
                    Text(
                        text = experience.company,
                        style = Arcade.type.bodySmall,
                        color = Arcade.colors.TextMuted
                    )
                }

                if (experience.isCurrent) {
                    Box(
                        modifier = Modifier
                            .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.chip / 2))
                            .neonBorder(Arcade.colors.Cyan, cornerRadius = Arcade.radii.chip / 2, alpha = 0.6f)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "▶ NOW PLAYING",
                            style = Arcade.type.label,
                            color = Arcade.colors.Cyan
                        )
                    }
                }
            }

            Text(
                text = if (experience.endDate != null) {
                    "${experience.startDate} - ${experience.endDate}"
                } else {
                    "From ${experience.startDate}"
                },
                style = Arcade.type.label,
                color = Arcade.colors.TextFaint
            )

            Text(
                text = experience.description,
                style = Arcade.type.body,
                color = Arcade.colors.Text
            )

            if (experience.skills.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Arcade.spacing.sm),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    experience.skills.take(3).forEach { skill ->
                        Box(
                            modifier = Modifier
                                .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.chip / 2))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = skill,
                                style = Arcade.type.label,
                                color = Arcade.colors.TextMuted
                            )
                        }
                    }
                    if (experience.skills.size > 3) {
                        Text(
                            text = "+${experience.skills.size - 3} more",
                            style = Arcade.type.label,
                            color = Arcade.colors.TextFaint
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillsSection(
    skills: List<Skill>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Arcade.spacing.lg)
            .padding(bottom = Arcade.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Arcade.spacing.md)
    ) {
        SectionTitle("SKILL TREE")

        // Group skills by category, each category gets its own accent
        val skillsByCategory = skills.groupBy { it.category }

        skillsByCategory.entries.forEachIndexed { index, (category, categorySkills) ->
            val accent = Arcade.colors.accents[index % Arcade.colors.accents.size]
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Arcade.spacing.sm)
            ) {
                Text(
                    text = category.uppercase(),
                    style = Arcade.type.label,
                    color = accent
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(Arcade.spacing.sm)
                ) {
                    categorySkills.forEach { skill ->
                        SkillChip(skill = skill, accent = accent)
                    }
                }
            }
        }
    }
}

@Composable
private fun SkillChip(
    skill: Skill,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.pill))
            .neonBorder(accent, cornerRadius = Arcade.radii.pill, alpha = 0.5f)
            .padding(horizontal = Arcade.spacing.md, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = skill.name,
            style = Arcade.type.bodySmall,
            color = Arcade.colors.Text
        )
        // Power meter: proficiency out of 5 as lit pips
        StreakRow(
            days = List(5) { it < skill.proficiency },
            accent = accent,
            dotSize = 5.dp
        )
    }
}

@Composable
private fun ContactSection(
    contact: Contact,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Arcade.spacing.lg)
            .padding(bottom = Arcade.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Arcade.spacing.md)
    ) {
        SectionTitle("GET IN TOUCH")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md)
        ) {
            ContactButton(
                emoji = "✉",
                label = "EMAIL",
                url = contact.email,
                accent = Arcade.colors.Ember,
                modifier = Modifier.weight(1f)
            )
            ContactButton(
                emoji = "⌨",
                label = "GITHUB",
                url = contact.github,
                accent = Arcade.colors.Purple,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md)
        ) {
            ContactButton(
                emoji = "🌐",
                label = "LINKEDIN",
                url = contact.linkedIn,
                accent = Arcade.colors.Cyan,
                modifier = Modifier.weight(1f)
            )
            if (contact.twitter.isNotEmpty()) {
                ContactButton(
                    emoji = "🐦",
                    label = "TWITTER",
                    url = contact.twitter,
                    accent = Arcade.colors.Pink,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (contact.buyMeACoffee.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.md)
            ) {
                ContactButton(
                    emoji = "☕",
                    label = "BUY ME COFFEE",
                    url = contact.buyMeACoffee,
                    accent = Arcade.colors.Amber,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ContactButton(
    emoji: String,
    label: String,
    url: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    Row(
        modifier = modifier
            .height(56.dp)
            .background(Arcade.colors.SurfaceRaised, RoundedCornerShape(Arcade.radii.chip))
            .neonBorder(accent, cornerRadius = Arcade.radii.chip, alpha = 0.6f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                val target = if (url.contains("@") && !url.startsWith("http")) "mailto:$url" else url
                uriHandler.openUri(target)
            },
        horizontalArrangement = Arrangement.spacedBy(Arcade.spacing.sm, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, style = Arcade.type.bodySmall, color = accent)
        Text(
            text = label,
            style = Arcade.type.label,
            color = accent,
            maxLines = 1
        )
    }
}
