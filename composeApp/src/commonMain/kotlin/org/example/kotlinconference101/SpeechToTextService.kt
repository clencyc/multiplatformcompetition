package org.example.kotlinconference101

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

expect class SpeechToTextService {
    val isListening: StateFlow<Boolean>
    val lastResult: StateFlow<String?>
    val lastError: StateFlow<String?>

    fun startListening(language: String = "en-US")
    fun stopListening()
    fun cancel()
    fun shutdown()
}

@Composable
expect fun rememberSpeechToTextService(
    onResult: (String) -> Unit,
    onError: (String) -> Unit
): SpeechToTextService?
