package com.example.charactermatchingapp.presentation.matching

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.charactermatchingapp.domain.matching.model.CharacterInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.abs

@Composable
fun CharacterMatchingScreen(
    modifier: Modifier = Modifier,
    items: ImmutableList<CharacterInfo>,
    onItemsDrop: () -> Unit,
) {
    var sizeNo by remember { mutableFloatStateOf(20f) }
    var sizeBookmark by remember { mutableFloatStateOf(20f) }
    var sizeGood by remember { mutableFloatStateOf(20f) }
    // 視覚的にわかりやすくするための判定用
    var dragX by remember { mutableFloatStateOf(0f) }
    var dragY by remember { mutableFloatStateOf(0f) }
    when {
        (abs(dragX) < abs(dragY)) && (dragY < -100 && (abs(dragY) > -1000)) -> {
            sizeNo = 16f
            sizeBookmark = 24f
            sizeGood = 16f
        }

        (abs(dragX) > abs(dragY)) && (abs(dragX) > 100 && (abs(dragX) < 1000)) -> {
            if (dragX > 0) {
                sizeNo = 16f
                sizeBookmark = 16f
                sizeGood = 24f
            } else {
                sizeNo = 24f
                sizeBookmark = 16f
                sizeGood = 16f
            }
        }

        else -> {
            sizeNo = 20f
            sizeBookmark = 20f
            sizeGood = 20f
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            items.forEachIndexed { index, item ->
                if (index == items.lastIndex || index == items.lastIndex - 1) {
                    DraggableCard(
                        characterInfo = item,
                        cardWidth = 350.dp,
                        cardHeight = 520.dp,
                        onSwiped = {
                            onItemsDrop()
                            dragX = 0f
                            dragY = 0f
                        },
                        onDragProgress = { x, y ->
                            dragX = x
                            dragY = y
                        },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // clickableにはonSwipedと同じ処理を渡す
            Icon(
                painter = rememberVectorPainter(Icons.Default.Close),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onItemsDrop()
                        dragX = 0f
                        dragY = 0f
                    }
                    .background(
                        color = if (sizeNo == 16f)
                            Color.LightGray else
                            Color(0xFFFF4D4D),
                    )
                    .padding(sizeNo.dp)
            )
            Icon(
                painter = rememberVectorPainter(Icons.Default.Star),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onItemsDrop()
                        dragX = 0f
                        dragY = 0f
                    }
                    .background(
                        color = if (sizeBookmark == 16f)
                            Color.LightGray else
                            Color(0xFF4D7AFF),
                    )
                    .padding(sizeBookmark.dp)
            )
            Icon(
                painter = rememberVectorPainter(Icons.Default.Favorite),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onItemsDrop()
                        dragX = 0f
                        dragY = 0f
                    }
                    .background(
                        color = if (sizeGood == 16f)
                            Color.LightGray else
                            Color(0xFF6AE354),
                    )
                    .padding(sizeGood.dp)
            )
        }
    }
}

@Preview
@Composable
private fun CharacterMatchingScreenPreview() {
    CharacterMatchingScreen(
        items = listOf(
            CharacterInfo(
                id = "1",
                name = "Character 1",
                image = "https://example.com/image1.jpg",
                description = "Description for Character 1",
                tags = listOf("優しい", "天真爛漫"),
                contributor = "Contributor 1"
            ),
            CharacterInfo(
                id = "2",
                name = "Character 2",
                image = "https://example.com/image2.jpg",
                description = "Description for Character 2",
                tags = listOf("根暗", "リスカ", "眼帯", "デコラ風", "優しい"),
                contributor = "Contributor 1"
            ),
            CharacterInfo(
                id = "3",
                name = "Character 3",
                image = "https://example.com/image3.jpg",
                description = "Description for Character 3",
                tags = listOf("あほ", "明るい"),
                contributor = "Contributor 2"
            )
        ).toImmutableList(),
        onItemsDrop = {}
    )
}
