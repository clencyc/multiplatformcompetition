package org.example.kotlinconference101

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.datetime.*

@Composable
@Preview
fun App() {
    MaterialTheme {
        var location by remember { mutableStateOf("Europe/Paris") }
        var timeAtLocation by remember { mutableStateOf("No location selected") }

        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
        ) {
            Text(timeAtLocation)
            TextField(value = location, onValueChange = { location = it })
            Button(onClick = { timeAtLocation = currentTimeAt(location) ?: "Invalid Location" }) {
                Text("Show Time At Location")
            }
        }
    }
}

@OptIn(kotlin.time.ExperimentalTime::class)
fun currentTimeAt(location: String): String? {
    fun LocalTime.formatted() = "$hour:$minute:$second"

    return try {
        val time = Clock.System.now()
        val zone = TimeZone.of(location)
        val localTime = time.toLocalDateTime(zone).time
        "The time in $location is ${localTime.formatted()}"
    } catch (ex: IllegalTimeZoneException) {
        null
    }
}
