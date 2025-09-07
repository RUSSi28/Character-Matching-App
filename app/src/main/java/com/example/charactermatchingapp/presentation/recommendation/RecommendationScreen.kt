package com.example.charactermatchingapp.presentation.recommendation

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.charactermatchingapp.domain.recommendation.model.Recommendation
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecommendationScreen(viewModel: RecommendationViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF363636)),
        contentAlignment = Alignment.Center
    ) {
        if (!uiState.hasStarted) {
            InitialView(onExecuteClick = { viewModel.fetchRecommendations() })
        } else {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error!!,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {
                    RecommendationContent(uiState.recommendations, uiState.searchSuggestions)
                }
            }
        }
    }
}

@Composable
private fun InitialView(onExecuteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "あなたがいいねしたタグ情報をもとに、Geminiがおすすめの漫画やアニメを提案します。",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onExecuteClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A99CE))
        ) {
            Text(text = "おすすめを実行", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }
    }
}

@Composable
private fun RecommendationContent(
    recommendations: List<Recommendation>,
    searchSuggestions: String?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (searchSuggestions != null) {
            item {
                SearchSuggestionWebView(htmlContent = searchSuggestions)
            }
        }

        items(recommendations) { item ->
            RecommendationCard(recommendation = item)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun SearchSuggestionWebView(htmlContent: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp) // 高さはコンテンツに応じて調整が必要な場合も
            .clip(RoundedCornerShape(12.dp))
    )
}

@Composable
private fun RecommendationCard(recommendation: Recommendation) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4F4F4F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = recommendation.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                maxLines = Int.MAX_VALUE // タイトルを全文表示するために行数制限をなくす
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "タイプ: ${recommendation.type}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recommendation.reason,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "一致したタグ: ${recommendation.matchedTags.joinToString()}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )

            // 出典URLのトグル
            if (recommendation.sourceUrls.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.clickable { isExpanded = !isExpanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "出典・参考情報",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Toggle Sources",
                        tint = Color.LightGray
                    )
                }
                AnimatedVisibility(visible = isExpanded) {
                    Column(modifier = Modifier.padding(start = 8.dp, top = 4.dp)) {
                        recommendation.sourceUrls.forEach { url ->
                            Text(
                                text = url,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4A99CE), // Link color
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
