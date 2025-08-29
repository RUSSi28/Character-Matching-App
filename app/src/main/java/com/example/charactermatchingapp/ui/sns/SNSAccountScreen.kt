package com.example.charactermatchingapp.ui.sns

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charactermatchingapp.R


// --- データクラスの定義 ---
data class Profile(
    val accountName: String,
    val headerImageResId: Int,
    val iconImageResId: Int,
    val profiletag1: String,
    val profiletag2: String,
    val profiletag3: String
) {
    val profileText: String
        get() = "#$profiletag1　#$profiletag2　#$profiletag3"
}

data class Post(
    val id: Int,
    val userName: String,
    val userIconResId: Int,
    val characterName: String,
    val postImageResId: Int,
    val posttag1: String,
    val posttag2: String,
    val posttag3: String
) {
    val postText: String
        get() = "#$posttag1　#$posttag2　#$posttag3"
}

// --- UIコンポーネント ---
@Composable
fun PostItem(
    userIconResId: Int,
    userName: String,
    characterName: String,
    postText: String,
    postImageResId: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = userIconResId),
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = userName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Image(
            painter = painterResource(id = postImageResId),
            contentDescription = "Post Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Text(text = characterName, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            val textColor = if (LocalInspectionMode.current) {
                Color(0xFF007AFF)
            } else {
                Color.Unspecified
            }

            Text(
                text = postText,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = textColor
            )
        }
    }
}

@Composable
fun ProfileHeader(
    profile: Profile
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = profile.headerImageResId),
                contentDescription = "Header Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = profile.iconImageResId),
                contentDescription = "Icon Image",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 40.dp)
                    .size(80.dp)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(56.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = profile.accountName,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // ★★★ ここから変更 ★★★
                // プレビューの時だけ色が変わるように設定
                val textColor = if (LocalInspectionMode.current) {
                    Color(0xFF007AFF) // プレビューの時は青色
                } else {
                    Color.Unspecified // 通常時（アプリ実行時）はテーマのデフォルト色
                }

                Text(
                    text = profile.profileText,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = textColor // 設定した色を適用
                )
                // ★★★ ここまで変更 ★★★
            }
        }
    }
}

// --- 新しいUI ---

@Composable
fun AccountScreen(
    profile: Profile,
    posts: List<Post>,
    onPostClick: (Post) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                ProfileHeader(profile = profile)
                HorizontalDivider(thickness = 8.dp, color = Color.LightGray)
            }
        }
        items(posts) { post ->
            Image(
                painter = painterResource(id = post.postImageResId),
                contentDescription = "Post Image ${post.id}",
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable { onPostClick(post) },
                contentScale = ContentScale.Crop
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    posts: List<Post>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("投稿") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: 戻る処理 */ }) {
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
            items(posts) { post ->
                PostItem(
                    userIconResId = post.userIconResId,
                    userName = post.userName,
                    characterName = post.characterName,
                    postText = post.postText,
                    postImageResId = post.postImageResId
                )
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
}


// --- ここからプレビュー ---
@Preview(showBackground = true, name = "アカウント画面プレビュー")
@Composable
fun AccountScreenPreview() {
    val sampleProfile = Profile(
        accountName = "User Name",
        headerImageResId = R.drawable.post_example,
        iconImageResId = R.drawable.post_example2,
        profiletag1 = "イラスト描きます",
        profiletag2 = "ゲーム好き",
        profiletag3 = "週末活動"
    )

    val samplePosts = List(10) { i ->
        Post(
            id = i,
            userName = sampleProfile.accountName,
            userIconResId = sampleProfile.iconImageResId,
            characterName = "キャラ名 $i",
            postImageResId = R.drawable.post_example,
            posttag1 = "タグA$i",
            posttag2 = "タグB$i",
            posttag3 = "タグC$i"
        )
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF007AFF), // ボタン等の色
            background = Color.White
        )
    ) {
        AccountScreen(
            profile = sampleProfile,
            posts = samplePosts,
            onPostClick = {}
        )
    }
}

@Preview(showBackground = true, name = "タイムライン画面プレビュー")
@Composable
fun TimelineScreenPreview() {
    val samplePosts = listOf(
        Post(
            id = 1,
            userName = "User Name",
            userIconResId = R.drawable.post_example2,
            characterName = "サンプルキャラA",
            postImageResId = R.drawable.post_example,
            posttag1 = "イラスト",
            posttag2 = "オリジナル",
            posttag3 = "女の子"
        ),
        Post(
            id = 2,
            userName = "User Name",
            userIconResId = R.drawable.post_example2,
            characterName = "サンプルキャラB",
            postImageResId = R.drawable.post_example,
            posttag1 = "風景",
            posttag2 = "ファンタジー",
            posttag3 = "背景"
        ),
    )

    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Color.White,
            primary = Color(0xFF007AFF)
        )
    ) {
        TimelineScreen(posts = samplePosts)
    }
}