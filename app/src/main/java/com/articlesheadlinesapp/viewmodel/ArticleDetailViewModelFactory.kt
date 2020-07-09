package com.articlesheadlinesapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.articlesheadlinesapp.database.AppDatabaseDao
import com.articlesheadlinesapp.model.Article
import java.lang.IllegalArgumentException

class ArticleDetailViewModelFactory(
    val database: AppDatabaseDao,
    val article: Article,
    val application: Application ): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ArticleDetailViewModel::class.java)) {
            return ArticleDetailViewModel(
                database,
                article,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}