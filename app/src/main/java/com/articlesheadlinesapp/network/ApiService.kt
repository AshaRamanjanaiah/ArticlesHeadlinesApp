package com.articlesheadlinesapp.network

import com.articlesheadlinesapp.model.Headlines
import com.articlesheadlinesapp.model.NewsSources
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://newsapi.org/"
private const val API_KEY = "298e4f036f7f4dd58d13a74ecf7a4d45"

private val interceptor = run {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.apply {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}

private val okHttpClient = OkHttpClient.Builder()
    .addNetworkInterceptor(interceptor) // same for .addInterceptor(...)
    .build()

// Using retrofit 2 to pull data from Network

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .client(okHttpClient)
    .build()

interface NewsApiEndPoint{
    @GET("v2/sources?apiKey=$API_KEY")
    fun getNewsSources(): Single<NewsSources>

    @GET("v2/top-headlines?apiKey=$API_KEY")
    fun getheadlines(@Query("sources") sources: String): Single<Headlines>
}

object NewsApi{
    val retrofitService: NewsApiEndPoint by lazy{
        retrofit.create(NewsApiEndPoint::class.java)
    }
}