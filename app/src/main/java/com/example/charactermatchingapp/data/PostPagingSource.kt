package com.example.charactermatchingapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.charactermatchingapp.R
import com.example.charactermatchingapp.ui.sns.Post
import kotlinx.coroutines.delay

class PostPagingSource : PagingSource<Int, Post>() {

    // サンプルの全データ（本来はデータベースなどから取得）
    private val sampleAllPosts = List(200) { i ->
        Post(
            id = i,
            userName = "User Name",
            userIconResId = R.drawable.post_example2,
            characterName = "キャラクター名 $i",
            postImageResId = R.drawable.post_example,
            posttag1 = "タグA$i",
            posttag2 = "タグB$i",
            posttag3 = "タグC$i"
        )
    }

    // データを読み込む中心的な処理
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            // ページ番号を取得（初回はnullなので1ページ目とする）
            val currentPage = params.key ?: 1
            // 1ページあたりのアイテム数
            val pageSize = 10

            // データベースやネットワークからデータを取得する処理をシミュレート
            delay(1000) // 1秒待機

            // 取得するデータの範囲を計算
            val startIndex = (currentPage - 1) * pageSize
            val endIndex = minOf(startIndex + pageSize, sampleAllPosts.size)
            val data = if (startIndex < sampleAllPosts.size) {
                sampleAllPosts.subList(startIndex, endIndex)
            } else {
                emptyList()
            }

            // 読み込み結果を返す
            LoadResult.Page(
                data = data,
                prevKey = if (currentPage == 1) null else currentPage - 1, // 前のページ番号
                nextKey = if (data.isEmpty()) null else currentPage + 1  // 次のページ番号
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // データが更新されたときに、どのページから再読み込みを始めるかを決める
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition
    }
}