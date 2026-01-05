package org.example.kotlinconference101

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.kotlinconference101.rememberTextToSpeechService
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.kotlinconference101.fetchAssistantReply
import org.example.kotlinconference101.rememberSpeechToTextService

@Composable
private fun VoicePromptBanner(text: String) {
    androidx.compose.material3.Surface(
        tonalElevation = 6.dp,
        shadowElevation = 12.dp,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun VoiceRipple(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    if (!isActive) return

    val transition = rememberInfiniteTransition(label = "voice-ripple")
    val radius1 by transition.animateFloat(
        initialValue = 24f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radius1"
    )
    val alpha1 by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha1"
    )

    val radius2 by transition.animateFloat(
        initialValue = 12f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, delayMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radius2"
    )
    val alpha2 by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, delayMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha2"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(160.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            drawCircle(color = color.copy(alpha = alpha1), radius = radius1, center = center)
            drawCircle(color = color.copy(alpha = alpha2), radius = radius2, center = center)
            drawCircle(color = color.copy(alpha = 0.65f), radius = 12.dp.toPx(), center = center)
        }
    }
}

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
    var voicePrompt by remember { mutableStateOf<String?>(null) }
    var isSpeaking by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf<String?>(null) }
    var lastHeard by remember { mutableStateOf<String?>(null) }
    var replyText by remember { mutableStateOf<String?>(null) }
    var autoListenAfterSpeech by remember { mutableStateOf(false) }
    val ttsService = rememberTextToSpeechService()
    val coroutineScope = remember { CoroutineScope(Dispatchers.Main) }
    val speechService = rememberSpeechToTextService(
        onResult = { spoken ->
            lastHeard = spoken
            statusText = "Processing..."
            coroutineScope.launch {
                try {
                    val reply = fetchAssistantReply(spoken)
                    replyText = reply
                    statusText = "Speaking reply"
                    ttsService?.speak(reply, "en")
                } catch (e: Exception) {
                    statusText = "Assistant error: ${e.message ?: "unknown"}"
                }
            }
        },
        onError = { err -> statusText = err }
    )

    LaunchedEffect(ttsService) {
        ttsService?.onSpeechStart = { isSpeaking = true }
        ttsService?.onSpeechDone = {
            isSpeaking = false
            if (autoListenAfterSpeech) {
                autoListenAfterSpeech = false
                statusText = "Listening..."
                // Must call startListening on main thread
                coroutineScope.launch(Dispatchers.Main) {
                    speechService?.startListening()
                }
            }
        }
        ttsService?.onSpeechError = { err ->
            isSpeaking = false
            statusText = err
        }
    }

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
                    val prompt = "Hello this is Christine's voice assistant, how can I help you?"
                    voicePrompt = prompt
                    statusText = "Speaking greeting"
                    autoListenAfterSpeech = speechService != null
                    if (ttsService != null) {
                        ttsService.speak(prompt, "en")
                    } else {
                        statusText = "Text-to-speech not available on this platform"
                    }
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

        // Lightweight voice prompt hint instead of full chat UI
        voicePrompt?.let { prompt ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VoicePromptBanner(text = prompt)
                VoiceRipple(isActive = isSpeaking)
            }
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

