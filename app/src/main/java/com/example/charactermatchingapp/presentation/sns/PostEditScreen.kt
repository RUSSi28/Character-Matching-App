package com.example.charactermatchingapp.presentation.sns

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreationScreen(
    onClick: () -> Unit
) {
    var imageUri by remember { mutableStateOf("") }
    var characterName by remember { mutableStateOf("") }
    var tag1 by remember { mutableStateOf("") }
    var tag2 by remember { mutableStateOf("") }
    var tag3 by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // テーマで定義された背景色（通常は白）を適用
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { /* Textは削除 */ },
                    navigationIcon = {
                        TextButton(onClick = onClick) {
                            Text("戻る")
                        }
                    },
                    actions = {
                        Button(onClick = onClick) {
                            Text("投稿")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // --- イラスト画像プレビューエリア ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // 1:1の正方形
                        .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                        .clickable {
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
                            painter = rememberAsyncImagePainter(model= imageUri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- キャラ名・タグ入力エリア ---
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp) // 各要素間のスペースを調整
                ) {
                    // ★★★ ここからレイアウト変更 ★★★
                    // キャラ名
                    Column {
                        Text(
                            text = "キャラ名",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        OutlinedTextField(
                            value = characterName,
                            onValueChange = { characterName = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    // タグ1
                    Column {
                        Text(
                            text = "タグ1",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        OutlinedTextField(
                            value = tag1,
                            onValueChange = { tag1 = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    // タグ2
                    Column {
                        Text(
                            text = "タグ2",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        OutlinedTextField(
                            value = tag2,
                            onValueChange = { tag2 = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    // タグ3
                    Column {
                        Text(
                            text = "タグ3",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        OutlinedTextField(
                            value = tag3,
                            onValueChange = { tag3 = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostCreationScreenPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF007AFF),
            background = Color.White
        )
    ) {
        PostCreationScreen(onClick = {})
    }
}