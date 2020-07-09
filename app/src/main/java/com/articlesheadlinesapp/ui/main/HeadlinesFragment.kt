package com.articlesheadlinesapp.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.articlesheadlinesapp.R
import com.articlesheadlinesapp.Utils.SharedPreferenceHelper
import com.articlesheadlinesapp.database.AppDatabase
import com.articlesheadlinesapp.viewmodel.HeadlinesViewModel
import com.articlesheadlinesapp.viewmodel.HeadlinesViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.loadError

/**
 * A Headlines fragment containing headlines of articles.
 */
class HeadlinesFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var headlinesViewModel: HeadlinesViewModel
    private val headlinesAdapter = HeadlinesAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        context?.let {
            SharedPreferenceHelper.customPrefs(it).registerOnSharedPreferenceChangeListener(this)
        }

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
            adapter = headlinesAdapter
            layoutManager = LinearLayoutManager(context)
        }

        headlinesViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(HeadlinesViewModel::class.java)

        headlinesViewModel.articleList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                if (list.isEmpty()) {
                   noResults.visibility = View.VISIBLE
                } else {
                    noResults.visibility = View.GONE
                    articlesList.visibility = View.VISIBLE
                }
                headlinesAdapter.updateDetailsList(list)
            }
        })

        headlinesViewModel.loadError.observe(viewLifecycleOwner, Observer { isError ->
            if (isError) {
                noResults.visibility = View.GONE
                loadError.visibility = View.VISIBLE
            } else {
                loadError.visibility = View.GONE
            }

        })

        headlinesRefreshLayout.setOnRefreshListener {
            loadError.visibility = View.GONE
            headlinesViewModel.refresh()
            headlinesRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.let {
            SharedPreferenceHelper.customPrefs(it).unregisterOnSharedPreferenceChangeListener(this)
        }
    }

    companion object {
        /**
         * Returns a new instance of this fragment.
         */
        @JvmStatic
        fun newInstance(): HeadlinesFragment {
            return HeadlinesFragment()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key.equals(SharedPreferenceHelper.SELECTED_LIST)) {
            headlinesViewModel.refresh()
        }
    }
}