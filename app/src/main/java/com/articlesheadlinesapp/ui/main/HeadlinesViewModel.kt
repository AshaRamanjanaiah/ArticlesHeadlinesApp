package com.articlesheadlinesapp.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.articlesheadlinesapp.Utils.SharedPreferenceHelper
import com.articlesheadlinesapp.database.AppDatabaseDao
import com.articlesheadlinesapp.model.Article
import com.articlesheadlinesapp.model.Headlines
import com.articlesheadlinesapp.network.NewsApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class HeadlinesViewModel(
    val database: AppDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private val TAG = HeadlinesViewModel::class.java.simpleName

    private val _articlesList by lazy { MutableLiveData<List<Article>>() }

    private val disposable = CompositeDisposable()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val articleList = database.getAllArticles()

    var sourceList = arrayListOf<String>()

    init {
        refresh()
    }

    fun refresh() {
        clearDatabase()
        sourceList = SharedPreferenceHelper
            .getSharedPreferenceArrayList(getApplication())
        if(!sourceList.isNullOrEmpty()) {
            for(source in sourceList) {
                getHeadlinesData(source)
                Log.d("Source ", source)
            }
        }
    }

    private fun getHeadlinesData(source: String) {
        disposable.add(
            NewsApi.retrofitService.getheadlines(source)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Headlines>() {
                    override fun onSuccess(headlines: Headlines) {
                        _articlesList.value = headlines.articles
                        insertIntoDatabase()
                    }

                    override fun onError(e: Throwable) {
                        var kk = "tets"
                        /*if (!articlesList.value.isNullOrEmpty()) {
                            _loadError.value = false
                        } else {
                            articlesList.value = listOf()
                            _loadError.value = true
                        }
                        _loading.value = false
                        Log.d(TAG, "Error loading data")*/
                    }

                })
        )
    }

    fun insertIntoDatabase() {
        uiScope.launch {
            insertArticles(_articlesList.value)
        }
    }

    private suspend fun insertArticles(articleList: List<Article>?) {
        withContext(Dispatchers.IO) {
            database.saveArticles(articleList)
        }
    }

    fun clearDatabase() {
        uiScope.launch {
            clearDataFromdb()
        }
    }

    private suspend fun clearDataFromdb() {
        withContext(Dispatchers.IO) {
            database.deleteArticles()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        viewModelJob.cancel()
        SharedPreferenceHelper.customPrefs(getApplication()).edit().clear().commit()
    }
}