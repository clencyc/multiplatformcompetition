package org.example.kotlinconference101

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import java.util.UUID


actual class TextToSpeechService {
    private var textToSpeech: TextToSpeech? = null
    actual var isInitialized: Boolean = false

    private var currentLanguage: String = "en"
    private var pendingText: String? = null
    private var speechRate: Float = 1.0f

    // Callback for TTS events
    actual var onSpeechStart: (() -> Unit)? = null
    actual var onSpeechDone: (() -> Unit)? = null
    actual var onSpeechError: ((String) -> Unit)? = null
    actual fun init() {
        textToSpeech = TextToSpeech(AndroidContextHolder.context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                setupTTS()

                // Speak any pending text
                pendingText?.let { text ->
                    speak(text, currentLanguage)
                    pendingText = null
                }
            } else {
                isInitialized = false
                onSpeechError?.invoke("TTS initialization failed")
            }
        }
    }

    private fun setupTTS() {
        textToSpeech?.let { tts ->
            // Set default language with a safe locale
            val locale = languageToLocale(currentLanguage)
            try {
                val result = tts.setLanguage(locale)
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    onSpeechError?.invoke("TTS language not supported; falling back to default")
                }
            } catch (e: Exception) {
                onSpeechError?.invoke("TTS language error: ${e.message}")
                tts.language = Locale.US
            }

            tts.setSpeechRate(speechRate)

            // Set up progress listener
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    onSpeechStart?.invoke()
                }

                override fun onDone(utteranceId: String?) {
                    onSpeechDone?.invoke()
                }

                override fun onError(utteranceId: String?) {
                    onSpeechError?.invoke("Speech error occurred")
                }
            })
        }
    }
    actual fun speak(text: String, language: String) {
        if (text.isBlank()) return

        currentLanguage = language

        if (!isInitialized) {
            // Queue the text for when TTS is ready
            pendingText = text
            return
        }

        textToSpeech?.let { tts ->
            // Update language if changed
            val locale = languageToLocale(language)

            tts.language = locale

            // Generate unique utterance ID for tracking
            val utteranceId = UUID.randomUUID().toString()

            // QUEUE_FLUSH stops current speech and starts new
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        }
    }
    actual fun stop() {
        textToSpeech?.stop()
    }
    actual fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized = false
    }
    actual fun setSpeechRate(rate: Float) {
        speechRate = rate
        textToSpeech?.setSpeechRate(rate)
    }

    private fun languageToLocale(language: String): Locale {
        return when (language.lowercase()) {
            "es" -> Locale("es", "ES")
            "fr" -> Locale("fr", "FR")
            "de" -> Locale("de", "DE")
            "en" -> Locale.US
            else -> Locale.forLanguageTag(language).takeIf { it.language.isNotBlank() } ?: Locale.US
        }
    }
}

@Composable
actual fun rememberTextToSpeechService(): TextToSpeechService? {
    val context = LocalContext.current
    AndroidContextHolder.set(context)

    val service = remember { TextToSpeechService().apply { init() } }

    DisposableEffect(service) {
        onDispose {
            service.shutdown()
        }
    }

    return service
}