package com.articlesheadlinesapp.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles_table")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val articleId: Int,
    @Embedded
    val source: SourceInfo?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val isFavorite: Boolean = false
) {
}