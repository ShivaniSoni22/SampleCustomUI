package com.sample.samplecustomui.ui.compose

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sample.samplecustomui.R
import com.sample.samplecustomui.ui.theme.Orange
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.random.Random

val Colors.dotBackground: Color
    get() = if (isLight) Color.Gray else Orange

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PlayLogoAnimation(bitmap: Bitmap?,
    customText: String,
    isLayoutVisible: (Boolean) -> Unit) {

    val density = LocalDensity.current
    val dotBackground = MaterialTheme.colors.dotBackground

    Scaffold {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            with(density) {
                val maxWidth = maxWidth
                val maxHeight = maxHeight

                for (i in 0..50) {
                    var state by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        delay(Random.nextLong(100, 300))
                        state = true
                        delay(3000)
                        state = false
                        delay(3000)
                        isLayoutVisible(true)
                    }

                    val animScale by animateFloatAsState(
                        targetValue = if (state) 1f else 0f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing,
                        )
                    )

                    val animCenterX by animateFloatAsState(
                        targetValue = if (state) .8f else 0f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing,
                        )
                    )

                    val animCenterY by animateFloatAsState(
                        targetValue = if (state) .8f else 0f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearEasing,
                        )
                    )

                    val centerX = remember {
                        Random.nextInt(0, maxWidth.toPx().toInt()).toFloat()
                    }
                    val centerY = remember {
                        Random.nextInt(0, maxHeight.toPx().toInt()).toFloat()
                    }
                    val radius = remember {
                        Random.nextInt(16, min(maxWidth.toPx(), minHeight.toPx()).toInt() / 14)
                            .toFloat()
                    }
                    val alpha = remember { (Random.nextInt(10, 85) / 100f) }

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = dotBackground,
                            center = Offset(
                                x = if (i % 2 != 0) centerX * animCenterX else centerX,
                                y = if (i % 2 == 0) centerY * animCenterY else centerY
                            ),
                            radius = radius * animScale,
                            alpha = alpha
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var state by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                val startDelay = Random.nextLong(100, 300)
                delay(startDelay)
                state = true
                delay(3000)
                state = false
                delay(2000 - startDelay)
            }

            val animAlpha by animateFloatAsState(
                targetValue = if (state) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing,
                )
            )

            val animRotation by animateFloatAsState(
                targetValue = if (state) 360f else 0f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing,
                )
            )

            val animScale by animateFloatAsState(
                targetValue = if (state) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing,
                )
            )

            bitmap?.let {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .graphicsLayer {
                            alpha = animAlpha
                            rotationX = animRotation
                            rotationY = animRotation
                            rotationZ = animRotation
                            scaleX = animScale
                            scaleY = animScale
                        },
                ) {
                    Surface(
                        color = Color.Black, modifier = Modifier
                            .fillMaxSize()
                            .alpha(1f)
                    ) {}
                    GlideImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        imageModel = it,
                        contentScale = ContentScale.Fit,
                        contentDescription = "Logo Gif"
                    )
                }
                AnimatedText(
                    text = customText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = animAlpha
                            rotationX = animRotation
                            rotationY = animRotation
                            rotationZ = animRotation
                            scaleX = animScale
                            scaleY = animScale
                        }
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )
            }
        }
    }
}

@Composable
fun AnimatedText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        text.forEach { char ->

            var state by remember { mutableStateOf(false) }

            LaunchedEffect(char) {
                while (true) {
                    val startDelay = Random.nextLong(300, 900)
                    delay(startDelay)
                    state = true
                    delay(3000)
                    state = false
                    delay(2000 - startDelay)
                }
            }

            val animAlpha by animateFloatAsState(
                targetValue = if (state) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 900,
                    easing = FastOutSlowInEasing,
                )
            )

            Text(
                text = char.toString(),
                fontFamily = FontFamily(Font(R.font.lato_bold)),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer {
                    alpha = animAlpha
                }
            )
        }
    }
}