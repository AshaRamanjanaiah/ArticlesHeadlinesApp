package com.articlesheadlinesapp.Utils

import com.articlesheadlinesapp.model.Source


class GlobalConstants {
    companion object {
        val sourcesList = ArrayList<Source>()

        fun getSourceList(): ArrayList<Source> {
            return sourcesList;
        }

        fun setSourceList(list: ArrayList<Source>) {
            sourcesList.clear()
            sourcesList.addAll(list)
        }
    }
}