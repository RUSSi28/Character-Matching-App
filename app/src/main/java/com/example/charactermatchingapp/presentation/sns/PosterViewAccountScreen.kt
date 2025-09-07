package com.example.charactermatchingapp.presentation.sns

// (imports は変更なし)
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box // ★ Boxをインポート
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment // ★ Alignmentをインポート
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.charactermatchingapp.R
import com.example.charactermatchingapp.domain.matching.model.Post
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterViewAccountScreen(
    accountId: String,
    onPostClick: (Post) -> Unit,
    onEditClick: () -> Unit,
    onPostFabClick: () -> Unit
) {
    val viewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(accountId = accountId)
    )
    val profile by viewModel.profileState.collectAsState()
    val posts = viewModel.postsFlow.collectAsLazyPagingItems()
    // プロフィールがnullでない場合にUIを表示
    profile?.let {
        // ★★★ ScaffoldをBoxに変更 ★★★
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(), // paddingValuesは不要になる
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        EditableProfileHeader(profile = it, onEditClick = onEditClick)
                    }
                }
                items(
                    count = posts.itemCount,
                    key = { index -> posts[index]?.id ?: index }
                ) { index ->
                    val post = posts[index]
                    if (post != null) {
                        Image(
                            painter = if (LocalInspectionMode.current) {
                                painterResource(id = R.drawable.post_example)
                            } else {
                                rememberAsyncImagePainter(model = post.postImageResId)
                            },
                            contentDescription = "Post Image ${post.id}",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { onPostClick(post) },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // ★★★ FABをBoxの直接の子として配置 ★★★
            FloatingActionButton(
                onClick = onPostFabClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                // ★★★ Modifierで配置場所と余白を指定 ★★★
                modifier = Modifier
                    .align(Alignment.BottomEnd) // 右下に配置
                    .padding(16.dp) // 外周に16dpの余白
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "投稿",
                    tint = Color.White
                )
            }
        }
    }
}