package com.articlesheadlinesapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.articlesheadlinesapp.database.AppDatabaseDao
import com.articlesheadlinesapp.model.Article
import kotlinx.coroutines.*

class ArticleDetailViewModel(
    val database: AppDatabaseDao,
    val article: Article,
    application: Application): AndroidViewModel(application) {

    private val TAG = ArticleDetailViewModel::class.java.simpleName

    val isSaved = database.getArticle(article.articleId)

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun UpdateArticleInDB() {
        uiScope.launch {
            updateArticle(article.articleId)
        }
    }


    /**
     * Update article in DB by marking isFavorite = 1
     * @param articleId to find and update article
     */
    private suspend fun updateArticle(articleId: Int) {
        withContext(Dispatchers.IO) {
            database.updateArticle(articleId, 1)
        }
    }
}