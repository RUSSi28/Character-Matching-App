package com.example.charactermatchingapp

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

@Composable
fun GlassmorphicBottomNavigation(
    currentTab: BottomNavigationTab,
    onItemClick: (Screen) -> Unit,
    hazeState: HazeState,
) {
    val animatedSelectedTabIndex by animateFloatAsState(
        targetValue = currentTab.ordinal.toFloat(),
        label = "animatedSelectedTabIndex",
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )
    val animatedColor by animateColorAsState(
        targetValue = Color(0xFF4A99CE),
        label = "animatedColor",
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )
    Box(
        modifier = Modifier
            .padding(vertical = 24.dp, horizontal = 32.dp)
            .fillMaxWidth()
            .height(64.dp)
            .hazeEffect(
                state = hazeState,
                style = HazeDefaults.style(
                    backgroundColor = MaterialTheme.colorScheme.background,
                    blurRadius = 10.dp,
                )
            )
            .border(
                width = Dp.Hairline,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = .8f),
                        Color.White.copy(alpha = .2f),
                    ),
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavigationItem(
                icon = Icons.Rounded.AccountBox,
                label = "Matching",
                selected = currentTab == BottomNavigationTab.Matching,
                onClick = { onItemClick(Screen.Matching) },
            )
            BottomNavigationItem(
                icon = Icons.Rounded.PhotoLibrary,
                label = "Gallery",
                selected = currentTab == BottomNavigationTab.Gallery,
                onClick = { onItemClick(Screen.Gallery) },
            )
            BottomNavigationItem(
                icon = Icons.Rounded.Home,
                label = "Home",
                selected = currentTab == BottomNavigationTab.Home,
                onClick = { onItemClick(Screen.Home) },
            )
            BottomNavigationItem(
                icon = Icons.Rounded.Settings,
                label = "Settings",
                selected = currentTab == BottomNavigationTab.Settings,
                onClick = { onItemClick(Screen.Settings) },
            )
            BottomNavigationItem(
                icon = Icons.Outlined.Lightbulb,
                label = "Recommend",
                selected = currentTab == BottomNavigationTab.Recommend,
                onClick = { onItemClick(Screen.Recommend) },
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SelectedTabCircleBlurredBackground(
                color = animatedColor,
                animatedSelectedTabIndex = animatedSelectedTabIndex,
                modifier = Modifier
                    .padding(12.dp)
                    .matchParentSize()
            )
        }
    }
}

@Composable
fun SelectedTabCircleBlurredBackground(
    animatedSelectedTabIndex: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .blur(60.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
    ) {
        val tabWidth = size.width / BottomNavigationTab.entries.size
        val radius = size.height / 2
        val selectedTabCenter = Offset(
            x = (tabWidth * animatedSelectedTabIndex) + (tabWidth / 2),
            y = radius,
        )

        drawCircle(
            color = color.copy(alpha = .6f),
            radius = radius,
            center = selectedTabCenter
        )
    }
}

@Composable
fun BottomNavigationItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val alpha by animateFloatAsState(
        targetValue = if (selected) 1f else .35f,
        label = "alpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else .98f,
        visibilityThreshold = .000001f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        ),
        label = "scale"
    )
    Column(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                alpha = alpha
            )
            .clickable(
                onClick = { onClick() },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) {
                Color(0xFF4A99CE)
            } else {
                Color(0xFFD7D7D7)
            }
        )
        Text(
            text = label,
            color = if (selected) {
                Color(0xFF4A99CE)
            } else {
                Color(0xFFD7D7D7)
            },
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}

enum class BottomNavigationTab {
    Matching,
    Gallery,
    Home,
    Settings,
    Recommend,
}
