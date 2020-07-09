package com.articlesheadlinesapp.model

data class Headlines(
    val status: String?,
    val totalResults: Int?,
    val articles: ArrayList<Article>?) {
}