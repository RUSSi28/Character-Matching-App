package com.example.charactermatchingapp.ui.sns

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.charactermatchingapp.R
import com.example.charactermatchingapp.domain.matching.model.Post
import com.example.charactermatchingapp.domain.matching.model.Profile
import kotlinx.coroutines.flow.flowOf


// --- UIコンポーネント ---
@Composable
fun PostItem(
    userIconResId: Uri,
    userName: String,
    characterName: String,
    postText: String,
    postImageResId: Uri
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = if (LocalInspectionMode.current) {
                    painterResource(id = R.drawable.post_example2)
                } else {
                    rememberAsyncImagePainter(model = userIconResId)
                },
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = userName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Image(
            painter = if (LocalInspectionMode.current) {
                painterResource(id = R.drawable.post_example)
            } else {
                rememberAsyncImagePainter(model = postImageResId)
            },
            contentDescription = "Post Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Text(text = characterName, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            val textColor = if (LocalInspectionMode.current) {
                Color(0xFF007AFF)
            } else {
                Color.Unspecified
            }

            Text(
                text = postText,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = textColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHeader(
    profile: Profile,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // トップバー
        TopAppBar(
            title = { Text("アカウント") },
            // ★★★ ここで左矢印の戻るボタンを実装 ★★★
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "戻る"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        // ヘッダー画像とプロフィール情報
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = if (LocalInspectionMode.current) {
                    painterResource(id = R.drawable.post_example2)
                } else {
                    rememberAsyncImagePainter(model = profile.headerImageResId)
                },
                contentDescription = "Header Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = if (LocalInspectionMode.current) {
                    painterResource(id = R.drawable.post_example2)
                } else {
                    rememberAsyncImagePainter(model = profile.iconImageResId)
                },
                contentDescription = "Icon Image",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 40.dp)
                    .size(80.dp)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(56.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = profile.accountName,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = profile.profileText,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


// --- 新しいUI ---

@Composable
fun AccountScreen(
    profile: Profile,
    viewModel: SnsViewModel = viewModel(),
    onPostClick: (Post) -> Unit,
    onBackClick: () -> Unit
) {
    val posts: LazyPagingItems<Post> = viewModel.postPagingFlow.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                ProfileHeader(profile = profile, onBackClick = onBackClick)
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
        items(
            count = posts.itemCount,
            key = { index -> posts[index]?.id ?: index }
        ) { index ->
            val post = posts[index]
            if (post != null) {
                Image(
                    painter = if (LocalInspectionMode.current) {
                        painterResource(id = R.drawable.post_example)
                    } else {
                        rememberAsyncImagePainter(model = post.postImageResId)
                    },
                    contentDescription = "Post Image ${post.id}",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable { onPostClick(post) },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    viewModel: SnsViewModel = viewModel()
) {
    val posts: LazyPagingItems<Post> = viewModel.postPagingFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("投稿") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: 戻る処理 */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(
                count = posts.itemCount,
                key = { index -> posts[index]?.id ?: index } // 各アイテムを区別するためのキー
            ) { index ->
                val post = posts[index]
                if (post != null) {
                    PostItem(
                        userIconResId = post.userIconResId,
                        userName = post.userName,
                        characterName = post.characterName,
                        postText = post.postText,
                        postImageResId = post.postImageResId
                    )
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                }
            }
        }
    }
}

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
                    rememberAsyncImagePainter(model = profile.headerImageResId)
                },
                contentDescription = "Header Image",
                modifier = Modifier.fillMaxWidth().height(160.dp),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = if (LocalInspectionMode.current) {
                    painterResource(id = R.drawable.post_example2)
                } else {
                    rememberAsyncImagePainter(model = profile.iconImageResId)
                },
                contentDescription = "Icon Image",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 40.dp)
                    .size(80.dp)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape),
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
                    text = profile.accountName,
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
                        text = profile.profileText,
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


/**
 * 新しいアカウント画面（投稿者用）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterViewAccountScreen(
    profile: Profile,
    viewModel: SnsViewModel = viewModel(),
    onPostClick: (Post) -> Unit,
    onEditClick: () -> Unit,
    onPostFabClick: () -> Unit
) {
    val posts = viewModel.postPagingFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(52.dp), // 高さを指定
                containerColor = MaterialTheme.colorScheme.primary // 背景色を青に
            ) {
                val itemColors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White.copy(alpha = 0.3f), // 選択中アイテムの背景色
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f)
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Text("スワイプ") },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Text("お気に入り") },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* TODO */ },
                    icon = { Text("アカウント") },
                    colors = itemColors
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPostFabClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "投稿",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    EditableProfileHeader(profile = profile, onEditClick = onEditClick)
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                }
            }
            items(
                count = posts.itemCount,
                key = { index -> posts[index]?.id ?: index }
            ) { index ->
                val post = posts[index]
                if (post != null) {
                    Image(
                        painter = if (LocalInspectionMode.current) {
                            painterResource(id = R.drawable.post_example)
                        } else {
                            rememberAsyncImagePainter(model = post.postImageResId)
                        },
                        contentDescription = "Post Image ${post.id}",
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { onPostClick(post) },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

/**
 * お気に入り画面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: SnsViewModel = viewModel()
) {
    val posts = viewModel.postPagingFlow.collectAsLazyPagingItems()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(52.dp), // 高さを指定
                containerColor = MaterialTheme.colorScheme.primary // 背景色を青に
            ) {
                val itemColors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White.copy(alpha = 0.3f),
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f)
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Text("スワイプ") },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* TODO */ },
                    icon = { Text("お気に入り") },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Text("アカウント") },
                    colors = itemColors
                )
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 52.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(
                count = posts.itemCount,
                key = { index -> posts[index]?.id ?: index }
            ) { index ->
                val post = posts[index]
                if (post != null) {
                    Image(
                        painter = if (LocalInspectionMode.current) {
                            painterResource(id = R.drawable.post_example2)
                        } else {
                            rememberAsyncImagePainter(model = post.postImageResId)
                        },
                        contentDescription = "Favorite Image ${post.id}",
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { /* TODO: 画像クリック時の遷移処理 */ },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}



// --- ここからプレビュー ---
@Preview(showBackground = true, name = "投稿アイテムプレビュー")
@Composable
fun PostItemPreview() {
    val samplePost = Post(
        id = "1",
        userName = "User Name",
        userIconResId = Uri.EMPTY,
        characterName = "キャラ名",
        postImageResId = Uri.EMPTY,
        posttags = listOf("イラスト", "オリジナル", "女の子")
    )

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF007AFF),
            background = Color.White
        )
    ) {
        // ★★★ 作成したオブジェクトのプロパティを渡す ★★★
        PostItem(
            userIconResId = samplePost.userIconResId,
            userName = samplePost.userName,
            characterName = samplePost.characterName,
            postText = samplePost.postText, // get()で自動生成されたテキストが使われる
            postImageResId = samplePost.postImageResId
        )
    }
}


@Preview(showBackground = true, name = "アカウント画面プレビュー")
@Composable
fun AccountScreenPreview() {
    val sampleProfile = Profile(
        accountName = "User Name",
        headerImageResId = Uri.EMPTY,
        iconImageResId = Uri.EMPTY,
        profileText = "ここにプロフィール文が入ります。この文章はサンプルです。"
    )

    val samplePosts = List(50) { i ->
        Post(
            id = i.toString(),
            userName = sampleProfile.accountName,
            userIconResId = sampleProfile.iconImageResId,
            characterName = "キャラ名 $i",
            postImageResId = Uri.EMPTY,
            posttags = listOf("タグA$i", "タグB$i", "タグC$i")
        )
    }

    val posts: LazyPagingItems<Post> = flowOf(PagingData.from(samplePosts))
        .collectAsLazyPagingItems()

    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Color.White,
            primary = Color(0xFF007AFF)

        )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    ProfileHeader(
                        profile = sampleProfile,
                        onBackClick = {} // プレビューでは何もしない
                    )
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                }
            }
            items(
                count = posts.itemCount,
                key = { index -> posts[index]?.id ?: index }
            ) { index ->
                val post = posts[index]
                if (post != null) {
                    Image(
                        painter = if (LocalInspectionMode.current) {
                            painterResource(id = R.drawable.post_example)
                        } else {
                            rememberAsyncImagePainter(model = post.postImageResId)
                        },
                        contentDescription = "Post Image ${post.id}",
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "タイムライン画面プレビュー")
@Composable
fun TimelineScreenPreview() {
    val sampleProfile = Profile(
        accountName = "User Name",
        headerImageResId = Uri.EMPTY,
        iconImageResId = Uri.EMPTY,
        profileText = "ここにプロフィール文が入ります。この文章はサンプルです。"
    )
    val samplePosts = List(50) { i ->
        Post(
            id = i.toString(),
            userName = sampleProfile.accountName,
            userIconResId = sampleProfile.iconImageResId,
            characterName = "キャラ名 $i",
            postImageResId = Uri.EMPTY,
            posttags = listOf("タグA$i", "タグB$i", "タグC$i")
        )
    }

    val posts: LazyPagingItems<Post> = flowOf(PagingData.from(samplePosts))
        .collectAsLazyPagingItems()

    MaterialTheme(
        colorScheme = lightColorScheme(background = Color.White, primary = Color(0xFF007AFF))
    ) {
        // ★★★ TimelineScreenの中身を直接ここに記述する ★★★
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("投稿") },
                    navigationIcon = {
                        IconButton(onClick = { /* プレビューでは何もしない */ }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "戻る"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(
                    count = posts.itemCount,
                    key = { index -> posts[index]?.id ?: index }
                ) { index ->
                    val post = posts[index]
                    if (post != null) {
                        PostItem(
                            userIconResId = post.userIconResId,
                            userName = post.userName,
                            characterName = post.characterName,
                            postText = post.postText,
                            postImageResId = post.postImageResId
                        )
                        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "投稿者用アカウント画面プレビュー")
@Composable
fun PosterViewAccountScreenPreview() {
    val sampleProfile = Profile(
        accountName = "User Name",
        headerImageResId = Uri.EMPTY,
        iconImageResId = Uri.EMPTY,
        profileText = "ここにプロフィール文が入ります。この文章はサンプルです。"
    )

    val samplePosts = List(50) { i ->
        Post(
            id = i.toString(),
            userName = sampleProfile.accountName,
            userIconResId = sampleProfile.iconImageResId,
            characterName = "キャラ名 $i",
            postImageResId = Uri.EMPTY,
            posttags = listOf("タグA$i", "タグB$i", "タグC$i")
        )
    }

    val posts: LazyPagingItems<Post> = flowOf(PagingData.from(samplePosts))
        .collectAsLazyPagingItems()

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF007AFF),
            background = Color.White
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.height(52.dp), // 高さを指定
                    containerColor = MaterialTheme.colorScheme.primary // 背景色を青に
                ) {
                    val itemColors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White.copy(alpha = 0.3f), // 選択中アイテムの背景色
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.7f)
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = { /* TODO */ },
                        icon = { Text("スワイプ") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { /* TODO */ },
                        icon = { Text("お気に入り") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = { /* TODO */ },
                        icon = { Text("アカウント") },
                        colors = itemColors
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "投稿",
                        tint = Color.White
                    )
                }
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        EditableProfileHeader(profile = sampleProfile, onEditClick = {})
                        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    }
                }
                items(
                    count = posts.itemCount,
                    key = { index -> posts[index]?.id ?: index }
                ) { index ->
                    val post = posts[index]
                    if (post != null) {
                        Image(
                            painter = if (LocalInspectionMode.current) {
                                painterResource(id = R.drawable.post_example)
                            } else {
                                rememberAsyncImagePainter(model = post.postImageResId)
                            },
                            contentDescription = "Post Image ${post.id}",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "お気に入り画面プレビュー")
@Composable
fun FavoritesScreenPreview() {
    val samplePosts = List(50) { i ->
        Post(
            id = i.toString(),
            userName = "User Name",
            userIconResId = Uri.EMPTY,
            characterName = "キャラ名 $i",
            postImageResId = Uri.EMPTY,
            posttags = listOf("タグA$i", "タグB$i", "タグC$i")
        )
    }

    val posts: LazyPagingItems<Post> = flowOf(PagingData.from(samplePosts))
        .collectAsLazyPagingItems()
    
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF007AFF),
            background = Color.White
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.height(52.dp), // 高さを指定
                    containerColor = MaterialTheme.colorScheme.primary // 背景色を青に
                ) {
                    val itemColors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White.copy(alpha = 0.3f),
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.7f)
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = { /* TODO */ },
                        icon = { Text("スワイプ") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = { /* TODO */ },
                        icon = { Text("お気に入り") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { /* TODO */ },
                        icon = { Text("アカウント") },
                        colors = itemColors
                    )
                }
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(top = 52.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(
                    count = posts.itemCount,
                    key = { index -> posts[index]?.id ?: index }
                ) { index ->
                    val post = posts[index]
                    if (post != null) {
                        Image(
                            painter = if (LocalInspectionMode.current) {
                                painterResource(id = R.drawable.post_example2)
                            } else {
                                rememberAsyncImagePainter(model = post.postImageResId)
                            },
                            contentDescription = "Favorite Image ${post.id}",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { /* TODO: 画像クリック時の遷移処理 */ },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}