package com.example.charactermatchingapp.data.gallery

import com.example.charactermatchingapp.R
import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository

/**
 * [GalleryRepositoryImpl] generates a list of [GalleryItem]
 */
class GalleryRepositoryImpl() : GalleryRepository {
    override suspend fun getGalleryItems(): List<GalleryItem> {
        return listOf(
            GalleryItem(
                R.string.affirmation1,
                R.drawable.image1,
                "Eleanor Roosevelt",
                listOf("#コンテナ", "#無機物", "#デカい")
            ),
            GalleryItem(
                R.string.affirmation2,
                R.drawable.image2,
                "Amelia Earhart",
                listOf("#浴槽", "#無機物", "#優しい")
            ),
            GalleryItem(
                R.string.affirmation3,
                R.drawable.image3,
                "Maya Angelou",
                listOf("#動物", "#デカい", "#クジラ", "仮想通貨", "ホエール", "ビットコイン")
            ),
            GalleryItem(
                R.string.affirmation4,
                R.drawable.image4,
                "Confucius",
                listOf("#男キャラ", "#ゾンビ", "#スマホ中毒")
            ),
            GalleryItem(
                R.string.affirmation5,
                R.drawable.image5,
                "Nelson Mandela",
                listOf("#富士山", "#ダイヤモンド富士", "#縁起")
            ),
            GalleryItem(
                R.string.affirmation6,
                R.drawable.image6,
                "Walt Disney",
                listOf("#着ぐるみ", "#クマ", "#熊")
            ),
            GalleryItem(
                R.string.affirmation7,
                R.drawable.image7,
                "Albert Einstein",
                listOf("#天使", "#メルヘン", "#ピンク")
            ),
            GalleryItem(
                R.string.affirmation8,
                R.drawable.image8,
                "Helen Keller",
                listOf("#ロボット", "#心", "#照れ")
            ),
            GalleryItem(
                R.string.affirmation9,
                R.drawable.image9,
                "Martin Luther King Jr.",
                listOf("#音楽家", "#ウクレレ", "#旅")
            ),
            GalleryItem(
                R.string.affirmation10,
                R.drawable.image10,
                "Lao Tzu",
                listOf("#かわいい", "#アイドル", "#白")
            )
        )
    }
}
