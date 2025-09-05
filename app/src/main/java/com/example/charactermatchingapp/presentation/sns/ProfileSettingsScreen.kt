package com.example.charactermatchingapp.presentation.sns

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.charactermatchingapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(
    accountId: String,
    viewModel: AccountSettingsViewModel = viewModel(), // ViewModelを取得
    onClick: () -> Unit
) {
    // ViewModelのUI状態を監視
    val uiState by viewModel.uiState.collectAsState()

    // 画面表示時に一度だけプロフィール情報を読み込む
    LaunchedEffect(accountId) {
        viewModel.loadProfile(accountId)
    }

    // 画像選択ランチャー
    val headerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = viewModel::onHeaderImageSelected // 結果をViewModelに渡す
    )
    val iconLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = viewModel::onIconImageSelected // 結果をViewModelに渡す
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* ... */ },
                navigationIcon = { TextButton(onClick = onClick) { Text("戻る") } },
                actions = {
                    Button(onClick = {
                        viewModel.updateProfile(accountId)
                        onClick()
                    }) { Text("更新") }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- ヘッダーとアイコン画像 ---
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .align(Alignment.TopCenter)
                            .clickable { headerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.headerImageUrl.isEmpty()) {
                            Image(painter = painterResource(id = R.drawable.post_example2),
                                contentDescription = "Header Placeholder",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                                )
                        } else {
                            Image(painter = rememberAsyncImagePainter(uiState.headerImageUrl),
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

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 10.dp) // 下からのオフセットは維持
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color.LightGray, CircleShape)
                            .clickable { iconLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.iconImageUrl.isEmpty()) {
                            Image(painter = painterResource(id = R.drawable.post_example2),
                                contentDescription = "Icon Placeholder",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                                )
                        } else {
                            Image(painter = rememberAsyncImagePainter(uiState.iconImageUrl),
                                contentDescription = "Icon Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- アカウント名とプロフィール文 ---
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(text = "プロフィール文", /*...*/)
                        OutlinedTextField(
                            value = uiState.profileText,
                            onValueChange = viewModel::onProfileTextChange, // ViewModelの関数を呼び出す
                            modifier = Modifier.fillMaxWidth().height(120.dp)
                        )
                    }
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
        AccountSettingsScreen(
            accountId = "NjMe4XK8J4rm9f4ogvEj",
            onClick = {})
    }
}