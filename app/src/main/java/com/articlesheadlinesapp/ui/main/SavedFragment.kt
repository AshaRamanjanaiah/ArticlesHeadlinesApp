package com.articlesheadlinesapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.articlesheadlinesapp.R
import com.articlesheadlinesapp.database.AppDatabase
import com.articlesheadlinesapp.model.Article
import com.articlesheadlinesapp.viewmodel.HeadlinesViewModelFactory
import com.articlesheadlinesapp.viewmodel.SavedArticleViewModel
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass.
 * Use the [SavedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedFragment : Fragment(), SavedArticleAdapter.ItemSelectedListener {
    private lateinit var savedArticleViewModel: SavedArticleViewModel
    private val savedArticleAdapter = SavedArticleAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_saved, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).appDatabaseDao

        val viewModelFactory =
            HeadlinesViewModelFactory(
                dataSource,
                application
            )

        articlesList.apply {
            adapter = savedArticleAdapter
            layoutManager = LinearLayoutManager(context)
        }

        savedArticleViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SavedArticleViewModel::class.java)

        savedArticleViewModel.savedArticles.observe(viewLifecycleOwner, Observer { list ->
            if (list.isEmpty()) {
                noResults.visibility = View.VISIBLE
                articlesList.visibility = View.GONE
            } else {
                noResults.visibility = View.GONE
                articlesList.visibility = View.VISIBLE
            }
            savedArticleAdapter.updateDetailsList(this, list)
        })
    }

    companion object {
        /**
         * Returns a new instance of this fragment.
         */
        @JvmStatic
        fun newInstance(): SavedFragment {
            return SavedFragment()
        }
    }

    override fun onItemSelected(item: Article) {
        var article = item
        article.isFavorite = 0
        savedArticleViewModel.UpdateArticleInDB(article)
    }
}