package com.example.charactermatchingapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.activity.compose.rememberLauncherForActivityResult
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterPostScreen() {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    // 画像アップロード
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    // タグ管理
    var tagInput by remember { mutableStateOf(TextFieldValue("")) }
    var tags by remember { mutableStateOf(listOf<String>()) }

    val context = LocalContext.current // Contextをここで取得

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("キャラクター投稿") }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 画像表示
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "キャラ画像",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.Gray, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.White)
                }
            }

            Button(onClick = { launcher.launch("image/*") }) {
                Text("画像をアップロード")
            }

            // 名前（自由入力）
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("名前") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // タグ入力
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = tagInput,
                    onValueChange = { tagInput = it },
                    label = { Text("タグを入力") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Button(
                    onClick = {
                        if (tagInput.text.isNotBlank() && tags.size < 10) {
                            tags = tags + tagInput.text
                            tagInput = TextFieldValue("") // 入力欄をクリア
                        }
                    }
                ) {
                    Text("追加")
                }
            }

            // タグ表示（横並び）
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(tags) { tag ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier
                            .clickable {
                                // タップで削除できるようにする（任意）
                                tags = tags - tag
                            }
                    ) {
                        Text(
                            text = tag,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // 説明メッセージ
            if (tags.isNotEmpty()) {
                Text(
                    text = "※ タグをタップすると削除できます",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.Start) // 左寄せ
                )
            }

            // 説明（自由入力・改行可）
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("キャラクターの説明") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 投稿ボタン
            Button(onClick = {
                val summary = """
        名前: ${name.text}
        タグ: ${tags.joinToString(", ")}
        説明: ${description.text}
        画像: ${selectedImageUri?.toString() ?: "未選択"}
    """.trimIndent()

                Toast.makeText(
                    context,
                    summary,
                    Toast.LENGTH_LONG
                ).show()

                /*
                if (selectedImageUri != null) {
                    CharacterPostActivity.uploadCharacterData(
                        name = name.text,
                        tags = tags,
                        description = description.text,
                        imageUri = selectedImageUri!!,
                        onSuccess = { ... },
                        onFailure = { ... }
                    )
                }
                */
            }) {
                Text("投稿")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterPostScreenPreview() {
    CharacterPostScreen()
}
