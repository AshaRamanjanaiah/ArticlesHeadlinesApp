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
import com.articlesheadlinesapp.viewmodel.SourcesViewModel
import kotlinx.android.synthetic.main.fragment_sources.*

/**
 * A Sources fragment containing sources of articles.
 * Use the [SourcesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SourcesFragment : Fragment() {
    private lateinit var sourcesViewModel: SourcesViewModel
    private var sourceAdapter = SourcesAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sourcesViewModel = ViewModelProviders.of(this).get(SourcesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sources, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_sourcelist.apply {
            adapter = sourceAdapter
            layoutManager = LinearLayoutManager(context)
        }

        sourcesViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                loadError.visibility = View.GONE
            }
        })
        sourcesViewModel.loadError.observe(viewLifecycleOwner, Observer { isError ->
            loadError.visibility = if (isError) View.VISIBLE else View.GONE
        })
        sourcesViewModel.newsSourcesList.observe(viewLifecycleOwner, Observer { newsSourceData ->
            sourceAdapter.updateSourcesList(newsSourceData)
        })

        refreshLayout.setOnRefreshListener {
            loadError.visibility = View.GONE
            sourcesViewModel.refresh()
            refreshLayout.isRefreshing = false
        }
    }

    companion object {
        /**
         * Returns a new instance of this fragment.
         */
        @JvmStatic
        fun newInstance(): SourcesFragment {
            return SourcesFragment()
        }
    }
}