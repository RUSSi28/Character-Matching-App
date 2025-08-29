package com.example.charactermatchingapp.ui.sns

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.charactermatchingapp.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreationScreen() {
    var text by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Textは削除 */ },
                navigationIcon = {
                    TextButton(onClick = { /* TODO: 戻る処理 */ }) {
                        Text("戻る")
                    }
                },
                actions = {
                    Button(onClick = { /* TODO: 投稿処理 */ }) {
                        Text("投稿")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- ユーザーアイコンと投稿文入力エリア ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.post_example2), // 仮のアイコン
                    contentDescription = "User Icon",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop // アイコン画像を余白なく表示
                )
                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    placeholder = { Text("投稿文") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- イラスト画像プレビューエリア ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // 1:1の正方形
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    // 画像設定後もクリックして選び直せるように変更
                    .clickable {
                        galleryLauncher.launch("image/*")
                    }
            ) {
                if (imageUri == null) {
                    Text(
                        text = "投稿イラスト",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray,
                        fontSize = 20.sp
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    // --- ここからIconButtonのブロックを削除 ---
                    // IconButton( ... ) { ... }
                    // --- ここまでを削除しました ---
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostCreationScreenPreview() {
    // プレビューにもテーマを適用すると配色の確認ができます
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF007AFF), // 青色
            background = Color.White // 背景を白
        )
    ) {
        PostCreationScreen()
    }
}