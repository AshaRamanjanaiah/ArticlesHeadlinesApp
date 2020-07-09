package com.articlesheadlinesapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.articlesheadlinesapp.model.Article

@Dao
interface AppDatabaseDao {
    /**
     * Selects and returns all articles from table.
     */
    @Query("SELECT * FROM articles_table")
    fun getAllArticles(): LiveData<List<Article>>

    @Insert
    fun saveArticles(articles: List<Article>?)

    @Query("DELETE FROM articles_table")
    fun deleteArticles()
}