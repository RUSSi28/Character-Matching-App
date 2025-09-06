package com.example.charactermatchingapp.presentation.sns

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.charactermatchingapp.domain.matching.model.Profile
import kotlinx.coroutines.flow.flowOf

/**
 * 新しいアカウント画面（投稿者用）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterViewAccountScreen(
    // ViewModelは画面自身が取得する
    accountId: String,
    onPostClick: (Post) -> Unit,
    onEditClick: () -> Unit,
    onPostFabClick: () -> Unit
) {
    // ★★★ 作成したFactoryを使ってViewModelを初期化 ★★★
    val viewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(accountId = accountId)
    )
    // ViewModelからプロフィールと投稿リストの状態を監視
    val profile by viewModel.profileState.collectAsState()
    val posts by viewModel.postsState.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // テーマで定義された背景色（通常は白）を適用
    ){
        // プロフィールがnullでない（読み込み完了後）場合にUIを表示
        profile?.let {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = Color.White
                        )
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onPostFabClick,
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "投稿",
                            tint = Color.White
                        )
                    }
                }
            ) { paddingValues ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column {
                            EditableProfileHeader(profile = it, onEditClick = onEditClick)
                        }
                    }
                    items(
                        count = posts.size,
                        key = { index -> posts[index].id }
                    ) { index ->
                        val post = posts[index]
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "投稿者用アカウント画面プレビュー")
@Composable
fun PosterViewAccountScreenPreview() {
    val sampleProfile = Profile(
        accountName = "User Name",
        headerImageResId = "",
        iconImageResId = "",
        profileText = "ここにプロフィール文が入ります。この文章はサンプルです。"
    )

    val samplePosts = List(50) { i ->
        Post(
            id = i.toString(),
            userName = sampleProfile.accountName,
            userIconResId = sampleProfile.iconImageResId,
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
            primary =  Color(0xFF007AFF),
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
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "投稿",
                        tint = Color.White
                    )
                }
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        EditableProfileHeader(profile = sampleProfile, onEditClick = {})
                        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
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
                                .clickable { },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}