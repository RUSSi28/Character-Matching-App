package com.example.charactermatchingapp.presentation.sns

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.charactermatchingapp.R
import com.example.charactermatchingapp.domain.matching.model.Post
import kotlinx.coroutines.flow.flowOf
import com.example.charactermatchingapp.data.PostRepository


/**
 * お気に入り画面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    accountId: String,
    onPostClick: (Post) -> Unit
) {
    val viewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(
            accountId = accountId,
            postRepository = PostRepository() // 本来はDI（依存性注入）するのが望ましい
        )
    )
    val posts by viewModel.favoritesState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 52.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(
                count = posts.size,
                key = { index -> posts[index].id }
            ) { index ->
                val post = posts[index]
                Image(
                    painter = if (LocalInspectionMode.current) {
                        painterResource(id = R.drawable.post_example2)
                    } else {
                        rememberAsyncImagePainter(model = post.postImageResId)
                    },
                    contentDescription = "Favorite Image ${post.id}",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable { onPostClick(post) },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "お気に入り画面プレビュー")
@Composable
fun FavoritesScreenPreview() {
    val samplePosts = List(50) { i ->
        Post(
            id = i.toString(),
            userName = "User Name",
            userIconResId = "",
            characterName = "キャラ名 $i",
            characterText = "キャラテキスト$i",
            postImageResId = "",
            posttags = listOf("タグA$i", "タグB$i", "タグC$i")
        )
    }

    val posts: LazyPagingItems<Post> = flowOf(PagingData.from(samplePosts))
        .collectAsLazyPagingItems()
    
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF007AFF),
            background = Color.White
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(top = 52.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(
                    count = posts.itemCount,
                    key = { index -> posts[index]?.id ?: index }
                ) { index ->
                    val post = posts[index]
                    if (post != null) {
                        Image(
                            painter = if (LocalInspectionMode.current) {
                                painterResource(id = R.drawable.post_example2)
                            } else {
                                rememberAsyncImagePainter(model = post.postImageResId)
                            },
                            contentDescription = "Favorite Image ${post.id}",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { /* TODO: 画像クリック時の遷移処理 */ },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}