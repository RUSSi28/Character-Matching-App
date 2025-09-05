package com.example.charactermatchingapp.presentation.sns

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.charactermatchingapp.domain.matching.model.Post
import com.example.charactermatchingapp.domain.matching.model.Profile
import com.example.charactermatchingapp.presentation.sns.SnsViewModel
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    accountId: String,
    onClick: () -> Unit
) {
    val viewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(accountId = accountId)
    )
    val posts by viewModel.postsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("投稿") },
                navigationIcon = {
                    IconButton(onClick = onClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(
                count = posts.size,
                key = { index -> posts[index]?.id ?: index } // 各アイテムを区別するためのキー
            ) { index ->
                val post = posts[index]
                if (post != null) {
                    PostItemScreen(post)
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "タイムライン画面プレビュー")
@Composable
fun TimelineScreenPreview() {
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
        colorScheme = lightColorScheme(background = Color.White, primary = Color(0xFF007AFF))
    ) {
        // ★★★ TimelineScreenの中身を直接ここに記述する ★★★
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("投稿") },
                    navigationIcon = {
                        IconButton(onClick = { /* プレビューでは何もしない */ }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "戻る"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(
                    count = posts.itemCount,
                    key = { index -> posts[index]?.id ?: index }
                ) { index ->
                    val post = posts[index]
                    if (post != null) {
                        PostItemScreen(post)
                        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }
}