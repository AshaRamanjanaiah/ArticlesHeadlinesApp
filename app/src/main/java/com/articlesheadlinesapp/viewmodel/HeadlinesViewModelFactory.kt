package com.articlesheadlinesapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.articlesheadlinesapp.database.AppDatabaseDao
import java.lang.IllegalArgumentException

class HeadlinesViewModelFactory(
    private val appDatabaseDao: AppDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HeadlinesViewModel::class.java)) {
            return HeadlinesViewModel(
                appDatabaseDao,
                application
            ) as T
        } else if(modelClass.isAssignableFrom(SavedArticleViewModel::class.java)) {
            return SavedArticleViewModel(
                appDatabaseDao,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}