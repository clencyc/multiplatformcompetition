package org.example.kotlinconference101

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    onFilterSelected: (FilterType) -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Arcade.colors.Ink)
            .verticalScroll(rememberScrollState())
            .padding(Arcade.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "LUCKY ROLL",
            style = Arcade.type.arcadeCaption,
            color = Arcade.colors.Ember
        )

        Spacer(modifier = Modifier.height(Arcade.spacing.sm))

        Text(
            text = "Flick the die and see which projects turn up. It'll jump you straight to the filtered list.",
            style = Arcade.type.body,
            color = Arcade.colors.TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Arcade.spacing.lg)
        )

        Spacer(modifier = Modifier.height(Arcade.spacing.xl))

        DiceFilterSandbox(onFilterSelected = onFilterSelected)

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "A bigger game is still cooking.",
            style = Arcade.type.bodySmall,
            color = Arcade.colors.TextFaint,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Arcade.spacing.lg)
        )

        Spacer(modifier = Modifier.height(Arcade.spacing.lg))

        Button(
            onClick = {
                uriHandler.openUri("https://docs.google.com/forms/d/e/1FAIpQLSd-o8joJdAKyHNpZ5ug51X2iquzcYnzT2NFw9dpge4jKgX6QQ/viewform?usp=publish-editor")
            },
            modifier = Modifier
                .height(56.dp)
                .neonGlow(Arcade.colors.Ember, radius = 16.dp, alpha = 0.3f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Arcade.colors.Ember,
                contentColor = Arcade.colors.OnEmber
            ),
            shape = RoundedCornerShape(Arcade.radii.chip)
        ) {
            Text(
                text = "INSERT COIN · JOIN WAITLIST",
                style = Arcade.type.arcadeCaption,
                color = Arcade.colors.OnEmber,
                modifier = Modifier.padding(horizontal = Arcade.spacing.sm)
            )
        }
    }
}
