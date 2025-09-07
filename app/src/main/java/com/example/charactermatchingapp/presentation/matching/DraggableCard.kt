package com.example.charactermatchingapp.presentation.matching

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.charactermatchingapp.domain.matching.model.CharacterInfo
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun DraggableCard(
    characterInfo: CharacterInfo,
    onSwipedRight: (CharacterInfo) -> Unit,
    onSwipedUp: (CharacterInfo) -> Unit,
    onSwipedLeft: (CharacterInfo) -> Unit,
    modifier: Modifier = Modifier,
    onDragProgress: (Float, Float) -> Unit = { _, _ -> },
) {
    val coroutineScope = rememberCoroutineScope()
    val animX = remember { Animatable(0f) }
    val animY = remember { Animatable(0f) }
    val rotation by remember {
        derivedStateOf {
            (animX.value / 60).coerceIn(-15f, 15f)
        }
    }
    var textHeight by remember { mutableFloatStateOf(0f) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val cardHeight = screenHeight * 3 / 5
    LaunchedEffect(animX.value, animY.value) {
        onDragProgress(animX.value, animY.value)
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFADADAD)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(height = cardHeight.dp)
            .offset {
                IntOffset(animX.value.roundToInt(), animY.value.roundToInt())
            }
            .graphicsLayer(
                transformOrigin = TransformOrigin(0f, 1f),
                rotationZ = rotation
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { /* Todo(ドラッグ中のカードの色を変更する) */ },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            animX.snapTo(animX.value + dragAmount.x)
                            animY.snapTo(animY.value + dragAmount.y)
                        }
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            val currentX = animX.value
                            val currentY = animY.value
                            val swipeThresholdX = 300f
                            val swipeThresholdY = -300f

                            when {
                                abs(currentX) > swipeThresholdX -> {
                                    val targetX = if (currentX > 0) {
                                        onSwipedRight(characterInfo)
                                        2000f
                                    } else {
                                        onSwipedLeft(characterInfo)
                                        -2000f
                                    }
                                    animX.animateTo(
                                        targetValue = targetX,
                                        animationSpec = spring()
                                    )
                                }

                                currentY < swipeThresholdY -> {
                                    animY.animateTo(
                                        targetValue = -4000f,
                                        animationSpec = spring()
                                    )
                                    onSwipedUp(characterInfo)
                                }

                                else -> {
                                    animX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring()
                                    )
                                    animY.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring()
                                    )
                                }
                            }
                        }
                    },
                    onDragCancel = {
                        coroutineScope.launch {
                            animX.animateTo(0f, spring())
                            animY.animateTo(0f, spring())
                        }
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(characterInfo.image)
                    .crossfade(true)
                    .build(),
                placeholder = ColorPainter(color = Color(0xFFADADAD)),
                error = ColorPainter(color = Color(0xFFADADAD)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(with(LocalDensity.current) { (textHeight + 100).toDp() })
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.9f),
                                    Color.Transparent
                                ),
                                startY = Float.POSITIVE_INFINITY,
                                endY = 0f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .onGloballyPositioned {
                            textHeight = it.size.height.toFloat()
                        }
                ) {
                    Text(
                        text = characterInfo.name,
                        color = Color.White,
                        fontSize = 20.sp,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = characterInfo.description,
                        color = Color.White,
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    TagList(tags = characterInfo.tags)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagList(tags: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        maxLines = 2,
    ) {
        tags.forEachIndexed { index, tag ->
            Text(
                text = tag,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(50f)
                    )
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun DraggableCardPreview() {
    DraggableCard(
        characterInfo = CharacterInfo(
            id = "2",
            name = "Character 2",
            image = "https://example.com/image2.jpg",
            description = "Description for Character 2",
            tags = listOf("優しい", "天真爛漫", "金髪碧眼", "ロングヘア"),
            userName = "Contributor 1",
            likes = 10,
            postedAt = Timestamp.now()
        ),
        onSwipedRight = {},
        onSwipedUp = {},
        onSwipedLeft = {},
        onDragProgress = { _, _ -> }
    )
}