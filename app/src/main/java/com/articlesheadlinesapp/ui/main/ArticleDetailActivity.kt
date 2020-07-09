package com.articlesheadlinesapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.articlesheadlinesapp.R
import com.articlesheadlinesapp.Utils.GlobalConstants
import com.articlesheadlinesapp.database.AppDatabase
import com.articlesheadlinesapp.model.Article
import kotlinx.android.synthetic.main.activity_article_detail.*

class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var articleDetailViewModel: ArticleDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val article: Article = intent.getParcelableExtra<Article>(GlobalConstants.ARTICLE)
        webview.webViewClient = WebViewClient()
        webview.loadUrl(article.url)

        val dataSource = AppDatabase.getInstance(application).appDatabaseDao

        val viewModelFactory = ArticleDetailViewModelFactory(dataSource, article, application)

        articleDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ArticleDetailViewModel::class.java)

        save_button.setOnClickListener {
            articleDetailViewModel.UpdateArticleInDB()
        }

        articleDetailViewModel.isSaved.observe(this, Observer { article ->
            if (article.isFavorite == 1) {
                save_button.text = resources.getString(R.string.you_have_saved_this_article)
                save_button.isEnabled = false
            } else {
                save_button.isEnabled = true
                save_button.text = resources.getString(R.string.save_this_article)
            }
        })

    }

    override fun onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            super.onBackPressed()
        }
    }
}