import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.charactermatchingapp.domain.matching.model.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

// ★★★ コンストラクタでQueryを受け取るように変更 ★★★
class PostPagingSource(
    private val query: Query
) : PagingSource<QuerySnapshot, Post>() {

    companion object {
        private const val PAGE_SIZE = 20L
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            // ★★★ 受け取ったqueryをベースにする ★★★
            val baseQuery = query.limit(PAGE_SIZE)

            val currentPage = params.key ?: baseQuery.get().await()
            val lastVisibleDocument = currentPage.documents.lastOrNull()

            val nextPageQuery = lastVisibleDocument?.let {
                baseQuery.startAfter(it)
            } ?: baseQuery

            val nextPage = if (params.key == null) currentPage else nextPageQuery.get().await()

            val posts = nextPage.toObjects(Post::class.java)

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
