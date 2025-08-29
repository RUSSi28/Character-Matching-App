package com.example.charactermatchingapp.ui.sns

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charactermatchingapp.R
import androidx.compose.material3.Divider


// --- データクラスの定義 ---
/**
 * 投稿1件分のデータを保持するためのデータクラス
 */
data class Post(
    val id: Int, // 各投稿を区別するための一意なID
    val userName: String,
    val userIconResId: Int,
    val postText: String,
    val postImageResId: Int
)

// --- ここからUIコンポーネント ---

/**
 * 投稿1件分を表示するためのコンポーザブル関数
 */
@Composable
fun PostItem(
    userIconResId: Int,
    userName: String,
    postText: String,
    postImageResId: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = userIconResId),
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = postText,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Image(
            painter = painterResource(id = postImageResId),
            contentDescription = "Post Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * アカウント画面のヘッダー部分を表示するためのコンポーザブル関数
 */
@Composable
fun ProfileHeader(
    headerImageResId: Int,
    iconImageResId: Int,
    accountName: String,
    profileText: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = headerImageResId),
                contentDescription = "Header Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = iconImageResId),
                contentDescription = "Icon Image",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = accountName,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = profileText,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}


/**
 * プロフィールと投稿一覧を組み合わせた画面
 * @param posts 投稿データのリスト
 */
@Composable
fun ProfileScreen(
    headerImageResId: Int,
    iconImageResId: Int,
    accountName: String,
    profileText: String,
    posts: List<Post>
) {
    // LazyColumnは、画面に表示されているアイテムだけを描画するため、
    // 大量のリストを扱ってもパフォーマンスが良いスクロールコンテナです。
    LazyColumn {
        // 1. プロフィールヘッダー部分 (リストの先頭に1つだけ表示)
        item {
            ProfileHeader(
                headerImageResId = headerImageResId,
                iconImageResId = iconImageResId,
                accountName = accountName,
                profileText = profileText
            )
            Divider(color = Color.LightGray, thickness = 1.dp)
        }

        // 2. 投稿リスト部分 (postsリストの要素数だけPostItemを繰り返し表示)
        items(posts) { post ->
            PostItem(
                userIconResId = post.userIconResId,
                userName = post.userName,
                postText = post.postText,
                postImageResId = post.postImageResId
            )
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}


// --- ここからプレビュー ---

/**
 * 画面全体のプレビュー
 */
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    // プレビュー用にサンプルの投稿データを作成
    val sampleUser = "User Name"
    val sampleIcon = R.drawable.post_example2
    val sampleImage = R.drawable.post_example

    // 新しいものが上に来るようにリストを作成
    val samplePosts = listOf(
        Post(id = 1, userName = sampleUser, userIconResId = sampleIcon, postText = "最新の投稿です！✨", postImageResId = sampleImage),
        Post(id = 2, userName = sampleUser, userIconResId = sampleIcon, postText = "2番目の投稿です。スクロールで全体が見えます。", postImageResId = sampleImage),
        Post(id = 3, userName = sampleUser, userIconResId = sampleIcon, postText = "3番目の投稿になります。", postImageResId = sampleImage),
        Post(id = 4, userName = sampleUser, userIconResId = sampleIcon, postText = "これは4番目の投稿です。", postImageResId = sampleImage)
    )

    ProfileScreen(
        headerImageResId = R.drawable.post_example2,
        iconImageResId = R.drawable.post_example2,
        accountName = sampleUser,
        profileText = "ここにプロフィール文が入ります。この画面全体がスクロールできます。",
        posts = samplePosts
    )
}