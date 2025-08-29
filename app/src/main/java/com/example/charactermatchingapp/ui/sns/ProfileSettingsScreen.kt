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
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
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
    var profileText by remember { mutableStateOf("ここにプロフィール文が入ります。この画面全体がスクロールできます。") }

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
        ) {
            // --- ヘッダーとアイコン画像 ---
            Box( // Box内で要素を重ねて表示
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // 1. ヘッダー画像 (一番下に描画)
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

                // 2. ヘッダー下のライン (ヘッダーの上、アイコンの下に描画)
                HorizontalDivider(
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Boxの底を基準に
                        .offset(y = (-40).dp),         // 40dp上にずらす（ヘッダー画像の底辺に一致）
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                // 3. アイコン画像 (一番上に描画)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp)
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                // プロフィール文
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
                            .height(120.dp)
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

