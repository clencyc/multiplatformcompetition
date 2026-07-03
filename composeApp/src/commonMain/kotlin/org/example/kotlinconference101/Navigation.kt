package org.example.kotlinconference101

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    HOME("HOME", Icons.Default.Home, "Home"),
    PROJECTS("QUESTS", Icons.Default.Work, "Projects"),
    GAME("ARCADE", Icons.Default.SportsSoccer, "Game"),
    ABOUT("PLAYER", Icons.Default.Info, "About")
}

@Composable
fun PortfolioBottomNavigationBar(
    currentItem: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Arcade.colors.Surface,
        contentColor = Arcade.colors.Text,
        tonalElevation = 0.dp,
    ) {
        NavigationItem.entries.forEach { item ->
            NavigationBarItem(
                selected = currentItem == item,
                onClick = { onItemSelected(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescription
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = Arcade.type.arcadeCaption.copy(fontSize = 9.sp, letterSpacing = 0.5.sp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Arcade.colors.Ember,
                    selectedTextColor = Arcade.colors.Ember,
                    unselectedIconColor = Arcade.colors.TextFaint,
                    unselectedTextColor = Arcade.colors.TextMuted,
                    indicatorColor = Arcade.colors.EmberDim,
                ),
                interactionSource = remember { MutableInteractionSource() }
            )
        }
    }
}
