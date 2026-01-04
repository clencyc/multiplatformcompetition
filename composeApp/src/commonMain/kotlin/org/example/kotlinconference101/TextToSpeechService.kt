package org.example.kotlinconference101

import androidx.compose.runtime.Composable

expect class TextToSpeechService {
    var onSpeechStart: (() -> Unit)?
    var onSpeechDone: (() -> Unit)?
    var onSpeechError: ((String) -> Unit)?

    fun init()

    fun speak(text: String, language: String = "en")

    fun stop()

    fun shutdown()

    fun setSpeechRate(rate: Float)

    var isInitialized: Boolean
}

@Composable
expect fun rememberTextToSpeechService(): TextToSpeechService?