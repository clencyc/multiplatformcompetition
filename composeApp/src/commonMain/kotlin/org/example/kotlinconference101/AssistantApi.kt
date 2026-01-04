package org.example.kotlinconference101

/**
 * Calls the portfolio assistant API to translate/generate a reply.
 */
expect suspend fun fetchAssistantReply(query: String): String
