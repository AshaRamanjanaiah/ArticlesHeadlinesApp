package com.articlesheadlinesapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.articlesheadlinesapp.database.AppDatabaseDao
import com.articlesheadlinesapp.model.Article
import kotlinx.coroutines.*

class SavedArticleViewModel(val databaseDao: AppDatabaseDao,
                            application: Application): AndroidViewModel(application) {

    private val TAG = SavedArticleViewModel::class.java.simpleName

    val savedArticles = databaseDao.getSavedArticles()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Update the article field isFavorite = 0 in DB
     * @param article that needs to be updated
     */

    fun UpdateArticleInDB(article: Article) {
        uiScope.launch {
            updateArticle(article)
        }
    }

    private suspend fun updateArticle(article: Article) {
        withContext(Dispatchers.IO) {
            databaseDao.update(article)
        }
    }
}