package org.example.kotlinconference101

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class SpeechToTextService(
    private val context: Context,
    private val speechRecognizer: SpeechRecognizer,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit,
    private val requestPermission: () -> Unit
) {
    private val _isListening = MutableStateFlow(false)
    private val _lastResult = MutableStateFlow<String?>(null)
    private val _lastError = MutableStateFlow<String?>(null)

    actual val isListening: StateFlow<Boolean> = _isListening
    actual val lastResult: StateFlow<String?> = _lastResult
    actual val lastError: StateFlow<String?> = _lastError

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            _isListening.value = true
        }

        override fun onBeginningOfSpeech() {
            _isListening.value = true
        }

        override fun onRmsChanged(rmsdB: Float) {
            // no-op
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            // no-op
        }

        override fun onEndOfSpeech() {
            _isListening.value = false
        }

        override fun onError(error: Int) {
            _isListening.value = false
            val message = "Speech error: $error"
            _lastError.value = message
            onError(message)
        }

        override fun onResults(results: Bundle?) {
            _isListening.value = false
            val spokenText = results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
            _lastResult.value = spokenText
            if (!spokenText.isNullOrBlank()) {
                onResult(spokenText)
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            // ignore partial results for now
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            // no-op
        }
    }

    init {
        speechRecognizer.setRecognitionListener(listener)
    }

    actual fun startListening(language: String) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            val message = "Speech recognition not available on this device"
            _lastError.value = message
            onError(message)
            return
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }

        _isListening.value = true
        speechRecognizer.startListening(intent)
    }

    actual fun stopListening() {
        _isListening.value = false
        speechRecognizer.stopListening()
    }

    actual fun cancel() {
        _isListening.value = false
        speechRecognizer.cancel()
    }

    actual fun shutdown() {
        _isListening.value = false
        speechRecognizer.destroy()
    }
}

@Composable
actual fun rememberSpeechToTextService(
    onResult: (String) -> Unit,
    onError: (String) -> Unit
): SpeechToTextService? {
    val context = LocalContext.current
    val latestOnResult by rememberUpdatedState(newValue = onResult)
    val latestOnError by rememberUpdatedState(newValue = onError)

    val speechRecognizer = remember {
        SpeechRecognizer.createSpeechRecognizer(context)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                latestOnError("Microphone permission denied")
            }
        }
    )

    val service = remember {
        SpeechToTextService(
            context = context,
            speechRecognizer = speechRecognizer,
            onResult = { latestOnResult(it) },
            onError = { latestOnError(it) },
            requestPermission = {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            service.shutdown()
        }
    }

    return service
}
