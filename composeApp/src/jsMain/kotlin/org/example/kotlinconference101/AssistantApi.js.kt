package org.example.kotlinconference101

actual suspend fun fetchAssistantReply(query: String): String =
    "Voice assistant network calls are only implemented on Android."
