package com.articlesheadlinesapp.viewmodel

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

    var tempArticleList = arrayListOf<Article>()

    private val _loadError by lazy { MutableLiveData<Boolean>() }
    val loadError: LiveData<Boolean> get() = _loadError

    init {
        refresh()
    }

    fun refresh() {
        clearDatabase()
        tempArticleList.clear()
        getArticlesFromSources()
        _loadError.value = false
    }

    /**
     * Get list os news sources from shared preference
     */
    fun getArticlesFromSources() {
        sourceList = SharedPreferenceHelper
            .getSharedPreferenceArrayList(getApplication())
        if(!sourceList.isNullOrEmpty()) {
            for(source in sourceList) {
                getHeadlinesData(source)
                Log.d("Source ", source)
            }
        }
    }

    /**
     * Get articles from the selected sources over network
     * @param source of the article
     */
    private fun getHeadlinesData(source: String) {
        disposable.add(
            NewsApi.retrofitService.getheadlines(source)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Headlines>() {
                    override fun onSuccess(headlines: Headlines) {
                        if(headlines.articles.isNullOrEmpty()) {
                            Log.d(TAG, "Articles are empty")
                        } else {
                            tempArticleList.addAll(headlines.articles)
                            _articlesList.value = tempArticleList
                            insertIntoDatabase()
                        }
                        _loadError.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (!_articlesList.value.isNullOrEmpty()) {
                            _loadError.value = false
                        } else {
                            _articlesList.value = listOf()
                            _loadError.value = true
                        }

                        Log.d(TAG, "Error loading data")
                    }

                })
        )
    }

    fun insertIntoDatabase() {
        uiScope.launch {
            insertArticles(_articlesList.value)
        }
    }

    /**
     * Insert list of articles in to DB
     * @param articleList that needs to be saved in DB
     */

    private suspend fun insertArticles(articleList: List<Article>?) {
        withContext(Dispatchers.IO) {
            database.saveArticles(articleList)
        }
    }

    /**
     * Clear database when data no longer required
     */

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