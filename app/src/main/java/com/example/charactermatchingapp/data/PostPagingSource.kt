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
            val currentPage = params.key ?: query.limit(PAGE_SIZE).get().await()

            val lastVisibleDocument = currentPage.documents.lastOrNull()

            val posts = currentPage.toObjects(Post::class.java)

            LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = if (posts.isEmpty()) null else lastVisibleDocument?.let {
                    query.limit(PAGE_SIZE).startAfter(it).get().await()
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Post>): QuerySnapshot? {
        return null
    }
}
