package com.example.charactermatchingapp.presentation.sns

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.example.charactermatchingapp.presentation.sns.SnsViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun AccountScreen(
    accountId: String,
    onPostClick: (Post) -> Unit,
    onClick: () -> Unit
) {
    val viewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(accountId = accountId)
    )
    // ViewModelからプロフィールと投稿リストの状態を監視
    val profile by viewModel.profileState.collectAsState()
    val posts by viewModel.postsState.collectAsState()

    profile?.let {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    ProfileHeader(profile = it, onBackClick = onClick)
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                }
            }
            items(
                count = posts.size,
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
    }
}

@Preview(showBackground = true, name = "アカウント画面プレビュー")
@Composable
fun AccountScreenPreview() {
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
            background = Color.White,
            primary = Color(0xFF007AFF)

        )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    ProfileHeader(
                        profile = sampleProfile,
                        onBackClick = {} // プレビューでは何もしない
                    )
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