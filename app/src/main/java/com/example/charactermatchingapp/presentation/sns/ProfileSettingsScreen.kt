package com.example.charactermatchingapp.presentation.sns

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
    var headerUrl by remember { mutableStateOf("") }
    var iconUrl by remember { mutableStateOf("") }
    var profileText by remember { mutableStateOf("ここにプロフィール文が入ります。") }

    val headerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { }
    )
    val iconLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { }
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
                    if (headerUrl == null) {
                        Image(
                            painter = painterResource(id = R.drawable.post_example2),
                            contentDescription = "Header Placeholder",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(headerUrl),
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
                    if (iconUrl == null) {
                        Image(
                            painter = painterResource(id = R.drawable.post_example2),
                            contentDescription = "Icon Placeholder",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(iconUrl),
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
                Column {
                    Text(
                        text = "プロフィール文",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = profileText,
                        onValueChange = { profileText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp) // 複数行を想定して高さを設定
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