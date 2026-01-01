package org.example.kotlinconference101

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        PortfolioApp()
    }
}

@Composable
private fun PortfolioApp() {
    var currentScreen by remember { mutableStateOf(NavigationItem.HOME) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Screen content
        ScreenContent(
            currentScreen = currentScreen,
            onNavigateToProjects = { currentScreen = NavigationItem.PROJECTS },
            onNavigateToAbout = { currentScreen = NavigationItem.ABOUT },
            modifier = Modifier.weight(1f)
        )

        // Bottom Navigation Bar
        PortfolioBottomNavigationBar(
            currentItem = currentScreen,
            onItemSelected = { currentScreen = it }
        )
    }
}

@Composable
private fun ScreenContent(
    currentScreen: NavigationItem,
    onNavigateToProjects: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (currentScreen) {
        NavigationItem.HOME -> HomeScreen(
            onNavigateToProjects = onNavigateToProjects,
            onNavigateToAbout = onNavigateToAbout,
            modifier = modifier
        )
        NavigationItem.PROJECTS -> ProjectsScreen(
            modifier = modifier
        )
        NavigationItem.ABOUT -> AboutScreen(
            modifier = modifier
        )
    }
}

