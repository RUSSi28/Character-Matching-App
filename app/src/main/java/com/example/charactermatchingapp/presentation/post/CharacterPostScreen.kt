package com.example.charactermatchingapp.presentation.post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.charactermatchingapp.domain.post.model.PostInfo
import com.example.charactermatchingapp.ui.theme.CharacterMatchingAppTheme
import com.google.accompanist.flowlayout.FlowRow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterPostScreen(
    modifier: Modifier = Modifier, // Re-add this line
    onPost: (PostInfo) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
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

    val addTag = {
        if (tagInput.text.isNotBlank() && tags.size < 10) {
            tags = tags + tagInput.text
            tagInput = TextFieldValue("") // 入力欄をクリア
        }
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
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
        OutlinedTextField(
            value = tagInput,
            onValueChange = { tagInput = it },
            label = { Text("タグを入力") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { addTag() })
        )

        // タグ表示
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp
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
                onShowSnackbar("投稿完了！")
            },
            baseColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterPostScreenPreview() {
    CharacterMatchingAppTheme {
        CharacterPostScreen(onPost = {}, onShowSnackbar = {})
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

