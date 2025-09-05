package com.example.charactermatchingapp.presentation.sns

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.charactermatchingapp.R
import com.example.charactermatchingapp.domain.matching.model.Post


// --- UIコンポーネント ---
@Composable
fun PostItemScreen(
    post: Post
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = if (LocalInspectionMode.current) {
                    painterResource(id = R.drawable.post_example2)
                } else {
                    rememberAsyncImagePainter(model = post.userIconResId)
                },
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = post.userName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Image(
            painter = if (LocalInspectionMode.current) {
                painterResource(id = R.drawable.post_example)
            } else {
                rememberAsyncImagePainter(model = post.postImageResId)
            },
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
            Text(text = post.characterName, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = post.characterText,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            val textColor = if (LocalInspectionMode.current) {
                Color(0xFF007AFF)
            } else {
                Color(0xFF007AFF)
            }
            Text(
                text = post.postText,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true, name = "投稿アイテムプレビュー")
@Composable
fun PostItemPreview() {
    val samplePost = Post(
        id = "1",
        userName = "User Name",
        userIconResId = "",
        characterName = "キャラ名",
        characterText = "キャラテキスト",
        postImageResId = "",
        posttags = listOf("イラスト", "オリジナル", "女の子")
    )

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF007AFF),
            background = Color.White
        )
    ) {
        // ★★★ 作成したオブジェクトのプロパティを渡す ★★★
        PostItemScreen(post= samplePost)
    }
}