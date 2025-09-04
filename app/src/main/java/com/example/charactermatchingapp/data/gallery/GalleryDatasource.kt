/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.charactermatchingapp.data.gallery

import com.example.charactermatchingapp.R
import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository

/**
 * [GalleryDatasource] generates a list of [GalleryItem]
 */
class GalleryDatasource() : GalleryRepository {
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
