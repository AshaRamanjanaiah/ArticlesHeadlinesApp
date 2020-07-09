package com.articlesheadlinesapp.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.articlesheadlinesapp.R
import com.articlesheadlinesapp.Utils.SharedPreferenceHelper
import com.articlesheadlinesapp.database.AppDatabase

/**
 * A Headlines fragment containing headlines of articles.
 */
class HeadlinesFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var headlinesViewModel: HeadlinesViewModel
    private lateinit var mOnSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)

        context?.let {
            SharedPreferenceHelper.customPrefs(it).registerOnSharedPreferenceChangeListener(this)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).appDatabaseDao

        val viewModelFactory = HeadlinesViewModelFactory(dataSource, application)

        headlinesViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(HeadlinesViewModel::class.java)

        headlinesViewModel.articleList.observe(viewLifecycleOwner, Observer { it ->
            var kk = it
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.let {
            SharedPreferenceHelper.customPrefs(it).unregisterOnSharedPreferenceChangeListener(this)
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): HeadlinesFragment {
            return HeadlinesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key.equals(SharedPreferenceHelper.SELECTED_LIST)) {
            headlinesViewModel.refresh()
        }
    }
}