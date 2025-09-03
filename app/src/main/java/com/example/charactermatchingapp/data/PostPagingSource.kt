import android.net.Uri
import androidx.core.net.toUri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.charactermatchingapp.domain.matching.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.FieldPath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class PostPagingSource(
    private val firestore: FirebaseFirestore
) : PagingSource<QuerySnapshot, Post>() {

    companion object {
        private const val PAGE_SIZE = 20L
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            // ★★★ 並び順をタイムスタンプからドキュメントIDの降順に変更 ★★★
            val baseQuery: Query = firestore.collection("posts")
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
                .limit(PAGE_SIZE)

            val currentPage = params.key ?: baseQuery.get().await()
            val lastVisibleDocument = currentPage.documents.lastOrNull()

            val nextPageQuery = lastVisibleDocument?.let {
                baseQuery.startAfter(it)
            } ?: baseQuery

            val nextPage = if (params.key == null) currentPage else nextPageQuery.get().await()

            // ★★★ toObjects()を使わず、手動でPostオブジェクトにマッピング ★★★
            val posts = nextPage.documents.mapNotNull { doc ->
                // Firestoreのフィールド名を指定して値を取得
                val userName = doc.getString("userName") ?: ""
                val characterName = doc.getString("characterName") ?: ""
                // Firestore上はStringで保存されているURLをUriに変換
                val userIconResId = doc.getString("userIconResId")?.toUri() ?: Uri.EMPTY
                val postImageResId = doc.getString("postImageResId")?.toUri() ?: Uri.EMPTY
                val posttags = doc.get("posttags") as? List<String> ?: emptyList()

                Post(
                    id = doc.id, // ドキュメントIDをidとして使用
                    userName = userName,
                    userIconResId = userIconResId,
                    characterName = characterName,
                    postImageResId = postImageResId,
                    posttags = posttags
                )
            }

            LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = if (nextPage.isEmpty) null else nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Post>): QuerySnapshot? {
        return null
    }
}

class PostRepository(
    private val firestore: FirebaseFirestore
) {
    fun getPostsPager(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ), pagingSourceFactory = { PostPagingSource(firestore) }
        ).flow
    }
}