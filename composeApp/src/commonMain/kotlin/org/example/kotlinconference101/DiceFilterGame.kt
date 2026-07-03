package org.example.kotlinconference101

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

/** The portfolio categories a die roll can land on and filter the project list by. */
enum class FilterType(val label: String) {
    KOTLIN("Kotlin"),
    PYTHON("Python"),
    AI_AGENTS("AI/Agents"),
    ALL("All")
}

// A die has 6 faces but we only have 4 filter buckets, so two faces double up
// on a bucket instead of inventing categories that don't map to real projects.
private val FACE_TO_FILTER = listOf(
    FilterType.KOTLIN,     // face 1
    FilterType.PYTHON,     // face 2
    FilterType.AI_AGENTS,  // face 3
    FilterType.ALL,        // face 4
    FilterType.KOTLIN,     // face 5
    FilterType.AI_AGENTS   // face 6
)

private const val FRICTION_PER_SECOND = 0.22f // velocity multiplier applied over one full second of travel
private const val RESTITUTION = 0.65f         // fraction of speed kept after bouncing off a wall
private const val SETTLE_SPEED_PX = 55f       // below this speed (px/s) the die is considered stopped
private const val SPIN_FACTOR = 0.18f         // deg/sec of tumble per px/sec of travel speed
private const val FACE_CYCLE_MS = 70L         // how often the pips flicker while the die is airborne
private const val MIN_FLING_SPEED = 200f      // ignore flicks too weak to be a deliberate roll

/**
 * A minimalist, physics-driven die that the user can flick around a bounded arena.
 * It bounces off the walls, loses energy via friction, and once it settles reports
 * the resulting [FilterType] via [onFilterSelected].
 */
@Composable
fun DiceFilterSandbox(
    modifier: Modifier = Modifier,
    onFilterSelected: (FilterType) -> Unit
) {
    val density = LocalDensity.current
    val haptics = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val arenaSizeDp = 260.dp
    val dieSizeDp = 64.dp
    val arenaSizePx = with(density) { arenaSizeDp.toPx() }
    val dieSizePx = with(density) { dieSizeDp.toPx() }
    val dieRadiusPx = dieSizePx / 2f

    var center by remember { mutableStateOf(Offset(arenaSizePx / 2f, arenaSizePx / 2f)) }
    val dieRotationX = remember { Animatable(0f) }
    val dieRotationY = remember { Animatable(0f) }
    val dieRotationZ = remember { Animatable(0f) }
    var displayedFace by remember { mutableStateOf(1) }
    var isRolling by remember { mutableStateOf(false) }
    var resultLabel by remember { mutableStateOf<String?>(null) }

    fun startRoll(flingVelocity: Offset) {
        if (isRolling || flingVelocity.getDistance() < MIN_FLING_SPEED) return

        isRolling = true
        resultLabel = null
        val resultFace = Random.nextInt(1, 7)

        scope.launch {
            val maxSpeed = 2200f
            val speed = flingVelocity.getDistance()
            var velocity = if (speed > maxSpeed) flingVelocity * (maxSpeed / speed) else flingVelocity

            var lastFrameNanos = 0L
            var elapsedMs = 0L
            var lastFaceFlipMs = 0L

            while (true) {
                val frameNanos = withFrameNanos { it }
                if (lastFrameNanos == 0L) lastFrameNanos = frameNanos
                val dt = ((frameNanos - lastFrameNanos) / 1_000_000_000f).coerceIn(0f, 0.05f)
                lastFrameNanos = frameNanos
                elapsedMs += (dt * 1000f).roundToInt()

                var newCenter = center + velocity * dt
                var bounced = false

                if (newCenter.x - dieRadiusPx < 0f) {
                    newCenter = newCenter.copy(x = dieRadiusPx)
                    velocity = velocity.copy(x = -velocity.x * RESTITUTION)
                    bounced = true
                } else if (newCenter.x + dieRadiusPx > arenaSizePx) {
                    newCenter = newCenter.copy(x = arenaSizePx - dieRadiusPx)
                    velocity = velocity.copy(x = -velocity.x * RESTITUTION)
                    bounced = true
                }
                if (newCenter.y - dieRadiusPx < 0f) {
                    newCenter = newCenter.copy(y = dieRadiusPx)
                    velocity = velocity.copy(y = -velocity.y * RESTITUTION)
                    bounced = true
                } else if (newCenter.y + dieRadiusPx > arenaSizePx) {
                    newCenter = newCenter.copy(y = arenaSizePx - dieRadiusPx)
                    velocity = velocity.copy(y = -velocity.y * RESTITUTION)
                    bounced = true
                }
                if (bounced) haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                center = newCenter

                // Exponential friction decay keeps the slowdown frame-rate independent.
                velocity *= FRICTION_PER_SECOND.toDouble().pow(dt.toDouble()).toFloat()

                // Tumble the whole die in 3D proportional to how fast it's travelling.
                dieRotationX.snapTo((dieRotationX.value - velocity.y * SPIN_FACTOR * dt) % 360f)
                dieRotationY.snapTo((dieRotationY.value + velocity.x * SPIN_FACTOR * dt) % 360f)
                dieRotationZ.snapTo((dieRotationZ.value + (velocity.x - velocity.y) * 0.02f * dt) % 360f)

                // Flicker the pips while airborne to build a little suspense.
                if (elapsedMs - lastFaceFlipMs > FACE_CYCLE_MS) {
                    displayedFace = Random.nextInt(1, 7)
                    lastFaceFlipMs = elapsedMs
                }

                if (velocity.getDistance() < SETTLE_SPEED_PX) break
            }

            // Lock in the pre-selected result and let the tumble spring back to rest.
            displayedFace = resultFace
            val settleSpec = spring<Float>(dampingRatio = 0.7f, stiffness = 120f)
            launch { dieRotationX.animateTo(0f, settleSpec) }
            launch { dieRotationY.animateTo(0f, settleSpec) }
            launch { dieRotationZ.animateTo(0f, settleSpec) }

            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            val filter = FACE_TO_FILTER[resultFace - 1]
            resultLabel = filter.label
            isRolling = false
            onFilterSelected(filter)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(arenaSizeDp)
                .neonEdgeGlow(ArcadeColors.Ember, cornerRadius = ArcadeRadii.card, alpha = 0.2f)
                .neonBorder(ArcadeColors.Ember, cornerRadius = ArcadeRadii.card, alpha = 0.6f)
                .clip(RoundedCornerShape(ArcadeRadii.card))
                .background(ArcadeColors.Surface)
                .pointerInput(Unit) {
                    var tracker = VelocityTracker()
                    detectDragGestures(
                        onDragStart = { tracker = VelocityTracker() },
                        onDrag = { change, _ ->
                            change.consume()
                            tracker.addPosition(change.uptimeMillis, change.position)
                        },
                        onDragEnd = {
                            val velocity = tracker.calculateVelocity()
                            startRoll(Offset(velocity.x, velocity.y))
                        }
                    )
                }
        ) {
            val pipColor = ArcadeColors.Ember
            Surface(
                modifier = Modifier
                    .size(dieSizeDp)
                    .offset {
                        IntOffset(
                            (center.x - dieRadiusPx).roundToInt(),
                            (center.y - dieRadiusPx).roundToInt()
                        )
                    }
                    .graphicsLayer {
                        rotationX = dieRotationX.value
                        rotationY = dieRotationY.value
                        rotationZ = dieRotationZ.value
                        cameraDistance = 12f * density.density
                    }
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                color = ArcadeColors.SurfaceRaised,
                contentColor = pipColor
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    drawPips(displayedFace, pipColor)
                }
            }
        }

        val statusText = when {
            isRolling -> "ROLLING..."
            resultLabel != null -> "FILTER: ${resultLabel!!.uppercase()}"
            else -> "SWIPE TO ROLL"
        }
        Text(
            text = statusText,
            style = Arcade.type.arcadeCaption,
            color = if (isRolling) ArcadeColors.Amber else ArcadeColors.Ember,
            textAlign = TextAlign.Center
        )
    }
}

private fun DrawScope.drawPips(face: Int, color: Color) {
    val pipRadius = size.minDimension * 0.09f
    fun pip(fx: Float, fy: Float) = drawCircle(
        color = color,
        radius = pipRadius,
        center = Offset(size.width * fx, size.height * fy)
    )

    val positions = when (face.coerceIn(1, 6)) {
        1 -> listOf(0.5f to 0.5f)
        2 -> listOf(0.25f to 0.25f, 0.75f to 0.75f)
        3 -> listOf(0.25f to 0.25f, 0.5f to 0.5f, 0.75f to 0.75f)
        4 -> listOf(0.25f to 0.25f, 0.75f to 0.25f, 0.25f to 0.75f, 0.75f to 0.75f)
        5 -> listOf(0.25f to 0.25f, 0.75f to 0.25f, 0.5f to 0.5f, 0.25f to 0.75f, 0.75f to 0.75f)
        else -> listOf(0.25f to 0.25f, 0.75f to 0.25f, 0.25f to 0.5f, 0.75f to 0.5f, 0.25f to 0.75f, 0.75f to 0.75f)
    }
    positions.forEach { (fx, fy) -> pip(fx, fy) }
}
