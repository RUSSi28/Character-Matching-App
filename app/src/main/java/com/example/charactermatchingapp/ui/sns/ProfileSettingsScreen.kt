package com.example.charactermatchingapp.ui.sns

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen() {
    var headerUri by remember { mutableStateOf<Uri?>(null) }
    var iconUri by remember { mutableStateOf<Uri?>(null) }
    var accountName by remember { mutableStateOf("User Name") }
    var tag1 by remember { mutableStateOf("ケモ耳") } // プレビュー用に初期値設定
    var tag2 by remember { mutableStateOf("ショートカット") }
    var tag3 by remember { mutableStateOf("インナーカラー") }

    val headerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> headerUri = uri }
    )
    val iconLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> iconUri = uri }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* タイトルを削除 */ },
                navigationIcon = { TextButton(onClick = { /* TODO */ }) { Text("戻る") } },
                actions = { Button(onClick = { /* TODO */ }) { Text("更新") } }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // --- ヘッダーとアイコン画像 ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .align(Alignment.TopCenter)
                        .clickable { headerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (headerUri == null) {
                        Image(
                            painter = painterResource(id = R.drawable.post_example2),
                            contentDescription = "Header Placeholder",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(headerUri),
                            contentDescription = "Header Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-40).dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                // ★★★ アイコン画像を中央に配置 ★★★
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // 中央寄せに変更
                        .offset(y = 10.dp) // 下からのオフセットは維持
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color.LightGray, CircleShape)
                        .clickable { iconLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (iconUri == null) {
                        Image(
                            painter = painterResource(id = R.drawable.post_example2),
                            contentDescription = "Icon Placeholder",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(iconUri),
                            contentDescription = "Icon Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // アカウント名
                Column {
                    Text(
                        text = "アカウント名",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = accountName,
                        onValueChange = { accountName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp) // ★★★ 最小の高さを設定 ★★★
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp) // ★★★ 最小の高さを設定 ★★★
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp) // ★★★ 最小の高さを設定 ★★★
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp) // ★★★ 最小の高さを設定 ★★★
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AccountSettingsScreenPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF007AFF),
            background = Color.White,
            surface = Color.White
        )
    ) {
        AccountSettingsScreen()
    }
}