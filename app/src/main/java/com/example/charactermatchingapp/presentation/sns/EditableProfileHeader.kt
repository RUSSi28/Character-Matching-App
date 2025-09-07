package com.example.charactermatchingapp.presentation.sns

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.charactermatchingapp.R
import com.example.charactermatchingapp.domain.matching.model.Profile
import androidx.compose.material3.MaterialTheme

/**
 * 「編集」ボタン付きのプロフィールヘッダー
 */
@Composable
fun EditableProfileHeader(
    profile: Profile,
    onEditClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = if (LocalInspectionMode.current) {
                    painterResource(id = R.drawable.post_example2)
                } else {
                    rememberAsyncImagePainter(model = profile.headerImageUrl)
                },
                contentDescription = "Header Image",
                modifier = Modifier.fillMaxWidth().height(160.dp),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = if (LocalInspectionMode.current) {
                    painterResource(id = R.drawable.post_example2)
                } else {
                    rememberAsyncImagePainter(model = profile.iconImageUrl)
                },
                contentDescription = "Icon Image",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 40.dp)
                    .size(80.dp)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.background, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(56.dp))
        // プロフィール情報と編集ボタンを配置するためのBox
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = profile.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = profile.bio ?: "", // bio can be null
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            // 「編集」ボタンを右上に配置
            Button(
                onClick = onEditClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = (-56).dp), // y方向に少しずらす
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text("編集")
            }
        }
    }
}