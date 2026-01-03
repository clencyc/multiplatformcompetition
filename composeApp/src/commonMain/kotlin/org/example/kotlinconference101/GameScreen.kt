package org.example.kotlinconference101

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameScreen(
    modifier: Modifier = Modifier
) {
    var isHovered by remember { mutableStateOf(false) }
    val buttonColor by animateColorAsState(
        targetValue = if (isHovered) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary,
        animationSpec = tween(300)
    )
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Coming Soon",
            modifier = Modifier
                .height(120.dp)
                .padding(bottom = 24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Coming Soon",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "I'm building something exciting! A game experience powered by C++ backend code. Check back soon to play!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Open the waitlist form using LocalUriHandler
                uriHandler.openUri("https://docs.google.com/forms/d/e/1FAIpQLSd-o8joJdAKyHNpZ5ug51X2iquzcYnzT2NFw9dpge4jKgX6QQ/viewform?usp=publish-editor")
            },
            modifier = Modifier
                .height(56.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Join the Waitlist",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            )
        }
    }
}
