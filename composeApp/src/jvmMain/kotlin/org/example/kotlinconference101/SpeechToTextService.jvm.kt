package org.example.kotlinconference101

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class SpeechToTextService {
    private val _isListening = MutableStateFlow(false)
    private val _lastResult = MutableStateFlow<String?>(null)
    private val _lastError = MutableStateFlow<String?>(null)

    actual val isListening: StateFlow<Boolean> = _isListening
    actual val lastResult: StateFlow<String?> = _lastResult
    actual val lastError: StateFlow<String?> = _lastError

    actual fun startListening(language: String) { /* not supported on JVM */ }
    actual fun stopListening() { /* no-op */ }
    actual fun cancel() { /* no-op */ }
    actual fun shutdown() { /* no-op */ }
}

@Composable
actual fun rememberSpeechToTextService(
    onResult: (String) -> Unit,
    onError: (String) -> Unit
): SpeechToTextService? = null
