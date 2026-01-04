package org.example.kotlinconference101

import androidx.compose.runtime.Composable

actual class TextToSpeechService {
    actual var onSpeechStart: (() -> Unit)? = null
    actual var onSpeechDone: (() -> Unit)? = null
    actual var onSpeechError: ((String) -> Unit)? = null
    actual var isInitialized: Boolean = false

    actual fun init() { isInitialized = true }
    actual fun speak(text: String, language: String) { /* no-op for wasm */ }
    actual fun stop() { /* no-op */ }
    actual fun shutdown() { isInitialized = false }
    actual fun setSpeechRate(rate: Float) { /* no-op */ }
}

@Composable
actual fun rememberTextToSpeechService(): TextToSpeechService? = null
