package com.articlesheadlinesapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.articlesheadlinesapp.database.AppDatabaseDao
import kotlinx.coroutines.*

class SavedArticleViewModel(val databaseDao: AppDatabaseDao,
                            application: Application): AndroidViewModel(application) {

    private val TAG = SavedArticleViewModel::class.java.simpleName

    val savedArticles = databaseDao.getSavedArticles()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun UpdateArticleInDB(id: Int) {
        uiScope.launch {
            updateArticle(id)
        }
    }

    private suspend fun updateArticle(articleId: Int) {
        withContext(Dispatchers.IO) {
            databaseDao.updateArticle(articleId, 0)
        }
    }
}