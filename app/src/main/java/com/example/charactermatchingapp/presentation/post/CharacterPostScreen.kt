package com.example.charactermatchingapp.presentation.post

import com.example.charactermatchingapp.domain.post.model.PostInfo
import com.example.charactermatchingapp.ui.theme.CharacterMatchingAppTheme

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.google.accompanist.flowlayout.FlowRow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterPostScreen(onPost: (PostInfo) -> Unit) {
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

    val context = LocalContext.current



    Scaffold(
        topBar = {
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(MaterialTheme.colorScheme.tertiary, Color(0xFF90CAF9))
            )

            Box(
                modifier = Modifier.background(gradientBrush)
            ) {
                TopAppBar(
                    title = { Text("キャラクター投稿") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            }
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

            // 画像アップロードボタン（secondary）
            GradientButton(
                text = "画像をアップロード",
                onClick = { launcher.launch("image/*") },
                baseColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary
            )

            // 名前
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
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (tagInput.text.isNotBlank() && tags.size < 10) {
                            tags = tags + tagInput.text
                            tagInput = TextFieldValue("") // 入力欄をクリア
                        }
                    })
                )
            }

            // タグ表示
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                tags.forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier
                            .clickable { tags = tags - tag }
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
                        .align(Alignment.Start)
                )
            }

            // 説明（自由入力）
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

            // 投稿ボタン（primary）
            GradientButton(
                text = "投稿",
                onClick = {
                    val postInfo = PostInfo(
                        name = name.text,
                        tags = tags,
                        description = description.text,
                        imageUri = selectedImageUri
                    )
                    onPost(postInfo)
                },
                baseColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterPostScreenPreview() {
    CharacterMatchingAppTheme {
        CharacterPostScreen(onPost = {})
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    baseColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            baseColor,
            baseColor.copy(alpha = 0.8f)
        )
    )

    Box(
        modifier = modifier
            .background(gradientBrush, shape = MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor)
    }
}

