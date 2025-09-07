package com.example.charactermatchingapp.presentation.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.ui.theme.CharacterMatchingAppTheme
import com.example.charactermatchingapp.ui.theme.SubColor
import com.example.charactermatchingapp.ui.theme.SubContainerColor
import com.example.charactermatchingapp.ui.theme.TextMainColor
import com.example.charactermatchingapp.ui.theme.TextSubColor
import com.google.firebase.Timestamp

@Composable
fun GalleryApp(
    modifier: Modifier = Modifier,
    galleryViewModel: GalleryViewModel = viewModel(),
    onItemClick: (String) -> Unit,
    windowInsets: WindowInsets,
) {
    val galleryItems by galleryViewModel.galleryItems.collectAsState()
    val isLoading by galleryViewModel.isLoading.collectAsState()
    val canLoadNext by galleryViewModel.canLoadNext.collectAsState()
    val canLoadPrevious by galleryViewModel.canLoadPrevious.collectAsState()

    Column(
        modifier = modifier
            .windowInsetsPadding(
                insets = windowInsets.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(galleryItems) { galleryItem ->
                    GalleryItemCard(
                        galleryItem = galleryItem,
                        onClick = { onItemClick(galleryItem.authorId) }
                    )
                }
                item(key = "prev_and_next") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .padding(bottom = 100.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { galleryViewModel.loadPreviousPage() },
                            enabled = canLoadPrevious && !isLoading
                        ) {
                            Text("前へ")
                        }
                        Button(
                            onClick = { galleryViewModel.loadNextPage() },
                            enabled = canLoadNext && !isLoading
                        ) {
                            Text("次へ")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun GalleryItemCard(galleryItem: GalleryItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = SubContainerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = galleryItem.imageUrl,
                contentDescription = galleryItem.characterName,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = galleryItem.characterName,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextMainColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = galleryItem.authorName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSubColor,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    galleryItem.tags.forEach { tag ->
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = SubColor
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSubColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryItemCardPreview() {
    CharacterMatchingAppTheme {
        GalleryItemCard(
            galleryItem = GalleryItem(
                artworkId = "1",
                authorId = "author1",
                authorName = "Test Author",
                characterName = "Test Character",
                characterDescription = "This is a test character description.",
                imageUrl = "https://via.placeholder.com/150",
                thumbUrl = null,
                tags = listOf("#tag1", "#tag2"),
                likeCount = 10,
                postedAt = Timestamp.now()
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryAppPreview() {
    CharacterMatchingAppTheme {
        GalleryApp(
            windowInsets = WindowInsets.safeDrawing,
            onItemClick = {}
        )
    }
}
