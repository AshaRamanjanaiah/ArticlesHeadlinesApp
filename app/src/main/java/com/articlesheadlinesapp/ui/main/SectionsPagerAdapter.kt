package com.articlesheadlinesapp.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.articlesheadlinesapp.R

private val TAB_TITLES = arrayOf(
    R.string.sources,
    R.string.head_lines,
    R.string.Saved
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        when(position) {
            1 -> return HeadlinesFragment.newInstance(position + 1)
            2 -> return SavedFragment.newInstance(position + 1)
            else -> return SourcesFragment.newInstance(position + 1)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show total pages.
        return TAB_TITLES.size
    }
}