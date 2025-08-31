package com.example.registration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                CharacterPostScreen()
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterPostScreen() {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var personality by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    // タグ選択用
    val tagItems = listOf("タグ1", "タグ2", "タグ3", "タグ4", "タグ5")
    var expanded by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf(tagItems[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("キャラクター投稿") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 仮の画像（ic_launcher_foreground を表示）
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "キャラ画像",
                modifier = Modifier.size(120.dp)
            )

            Button(onClick = { /* 画像選択処理を後で追加 */ }) {
                Text("画像をアップロード")
            }

            // 名前入力
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("名前") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 性格（自由入力）
            OutlinedTextField(
                value = personality,
                onValueChange = { personality = it },
                label = { Text("性格") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // タグ（プルダウン形式）
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedTag,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("タグを選択") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    tagItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                selectedTag = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            // 説明
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("キャラクターの簡単な説明") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 投稿ボタン
            Button(onClick = {
                println("投稿: ${name.text}, ${personality.text}, タグ=$selectedTag, ${description.text}")
            }) {
                Text("投稿")
            }
        }
    }
}

/**
 * プレビュー用
 */
@Preview(showBackground = true)
@Composable
fun CharacterPostScreenPreview() {
    CharacterPostScreen()
}
