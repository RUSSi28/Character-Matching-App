package com.example.charactermatchingapp

// AppNavigation.ktのような新しいファイルに作成するのがおすすめです
object AppDestinations {
    const val POSTER_VIEW_ACCOUNT_ROUTE = "posterViewAccount"
    const val ACCOUNT_SETTINGS_ROUTE = "accountSettings"
    const val POST_EDIT_ROUTE = "postEdit"
    const val TIMELINE_ARG_ACCOUNT_ID = "accountId"
    const val TIMELINE_ARG_POST_ID = "postId"
    const val TIMELINE_ROUTE = "timeline/{$TIMELINE_ARG_ACCOUNT_ID}/{$TIMELINE_ARG_POST_ID}"
    const val FAVORITE_ROUTE = "favorite"
    const val GALLERY_ROUTE = "gallery"
    const val ACCOUNT_ARG_AUTHOR_ID = "authorId"
    const val ACCOUNT_ROUTE = "account/{$ACCOUNT_ARG_AUTHOR_ID}"
}