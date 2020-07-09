package com.articlesheadlinesapp.Utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.articlesheadlinesapp.model.NewsSources
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferenceHelper {

    private val NEWS_SOURCES = "sources_list"
    val SELECTED_LIST = "selected_list"
    private val NEWS_HEADLINES = "news_headlines"

    fun customPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(NEWS_HEADLINES, Context.MODE_PRIVATE)

    fun saveSharedPreferenceObject(context: Context, newsSources: NewsSources) {
        val editor = customPrefs(context).edit()
        val gson = Gson()
        val json: String = gson.toJson(newsSources)
        editor.putString(NEWS_SOURCES, json)
        editor.apply()
    }

    fun getSharedPreferenceObject(context: Context): NewsSources {
        val gson = Gson()
        val json: String? = customPrefs(context).getString(NEWS_SOURCES, "")
        val newsSources = gson.fromJson(json, NewsSources::class.java)
        return newsSources
    }

    fun saveSharedPreferenceArrayList(context: Context, arrayList: ArrayList<String?>) {
        val editor = customPrefs(context).edit()
        val gson = Gson();
        val jsonText = gson.toJson(arrayList);
        editor.putString(SELECTED_LIST, jsonText)
        editor.apply()
    }

    fun getSharedPreferenceArrayList(context: Context): ArrayList<String> {
        val arrayList = arrayListOf<String>()
        val gson = Gson()
        val json: String? = customPrefs(context).getString(SELECTED_LIST, "")
        val type = object : TypeToken<java.util.ArrayList<String?>?>() {}.getType()

        val list = gson.fromJson(json, type)?: arrayList
        return list
    }

}