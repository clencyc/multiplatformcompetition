package org.example.kotlinconference101

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
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
    var showVoiceAssistant by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            PortfolioTopAppBar(currentScreen = currentScreen)
        },
        bottomBar = {
            PortfolioBottomNavigationBar(
                currentItem = currentScreen,
                onItemSelected = { currentScreen = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showVoiceAssistant = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Assistant"
                )
            }
        }
    ) { innerPadding ->
        // Screen content with padding from Scaffold
        ScreenContent(
            currentScreen = currentScreen,
            onNavigateToProjects = { currentScreen = NavigationItem.PROJECTS },
            onNavigateToAbout = { currentScreen = NavigationItem.ABOUT },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )

        // Voice Assistant Chat Interface
        if (showVoiceAssistant) {
            VoiceAssistantChat(
                onDismiss = { showVoiceAssistant = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PortfolioTopAppBar(currentScreen: NavigationItem, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = currentScreen.label,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
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
        NavigationItem.GAME -> GameScreen(
            modifier = modifier
        )
        NavigationItem.ABOUT -> AboutScreen(
            modifier = modifier
        )
    }
}

