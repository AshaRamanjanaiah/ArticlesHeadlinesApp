package com.articlesheadlinesapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.articlesheadlinesapp.Utils.SharedPreferenceHelper
import com.articlesheadlinesapp.model.NewsSources
import com.articlesheadlinesapp.model.Source
import com.articlesheadlinesapp.network.NewsApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class SourcesViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = SourcesViewModel::class.java.simpleName

    private val _newsSourcesList by lazy { MutableLiveData<List<Source>>() }

    val newsSourcesList: LiveData<List<Source>> get() = _newsSourcesList

    private val _loading by lazy { MutableLiveData<Boolean>() }
    val loading: LiveData<Boolean> get() = _loading

    private val _loadError by lazy { MutableLiveData<Boolean>() }
    val loadError: LiveData<Boolean> get() = _loadError

    private val disposable = CompositeDisposable()

    init {
        refresh()
        _loading.value = true
    }

    fun refresh() {
        getNewsSourcesData()
        _loadError.value = false
    }

    /**
     * This method gets weather data from network
     */
    private fun getNewsSourcesData() {
        disposable.add(
            NewsApi.retrofitService.getNewsSources()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewsSources>() {
                    override fun onSuccess(newsSources: NewsSources) {
                        _newsSourcesList.value = newsSources.sources
                        SharedPreferenceHelper.saveSharedPreferenceObject(getApplication(), newsSources)
                        _loading.value = false
                        _loadError.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (!_newsSourcesList.value.isNullOrEmpty()) {
                            _loadError.value = false
                        } else {
                            _newsSourcesList.value = listOf()
                            _loadError.value = true
                        }
                        _loading.value = false
                        Log.d(TAG, "Error loading data")
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}