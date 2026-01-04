package org.example.kotlinconference101

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private val json = Json { ignoreUnknownKeys = true }
private val httpClient by lazy {
    HttpClient(OkHttp) {
        install(ContentNegotiation) { json(json) }
        install(Logging)
    }
}

actual suspend fun fetchAssistantReply(query: String): String {
    val payload = buildJsonObject {
        put("query", query)
    }

    val response = httpClient.post(
        urlString = "https://portfolio-assistant-546011950324.us-central1.run.app/api/generate"
    ) {
        contentType(ContentType.Application.Json)
        setBody(payload)
    }

    val raw = response.bodyAsText()
    return parseAssistantResponse(raw)
}

private fun parseAssistantResponse(raw: String): String {
    return try {
        val element = json.parseToJsonElement(raw)
        if (element is JsonObject) {
            element["reply"]?.jsonPrimitive?.contentOrNull
                ?: element["response"]?.jsonPrimitive?.contentOrNull
                ?: element["result"]?.jsonPrimitive?.contentOrNull
                ?: element["text"]?.jsonPrimitive?.contentOrNull
                ?: element["message"]?.jsonPrimitive?.contentOrNull
                ?: raw
        } else {
            raw
        }
    } catch (e: Exception) {
        raw
    }
}
