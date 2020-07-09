package com.articlesheadlinesapp.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.articlesheadlinesapp.R
import com.articlesheadlinesapp.Utils.SharedPreferenceHelper
import com.articlesheadlinesapp.model.Source
import kotlinx.android.synthetic.main.source_item_layout.view.*

class SourcesAdapter(val sourcesList: ArrayList<Source>) :
    RecyclerView.Adapter<SourcesAdapter.SourcesViewModel>() {

    var selectedSources = ArrayList<String?>()

    fun updateSourcesList(newSourcesList: List<Source>) {
        sourcesList.clear()
        sourcesList.addAll(newSourcesList)
        sourcesList.removeIf {
            it.name.isNullOrEmpty()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourcesViewModel {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.source_item_layout, parent, false)
        return SourcesViewModel(view)
    }

    override fun getItemCount(): Int {
        return sourcesList.size
    }

    override fun onBindViewHolder(holder: SourcesViewModel, position: Int) {
        holder.view.source_name.text = sourcesList.get(position).name
        if (sourcesList.get(position).isSelected) {
            holder.view.checkmark.setImageResource(android.R.drawable.checkbox_on_background)
        } else {
            holder.view.checkmark.setImageResource(android.R.drawable.checkbox_off_background)
        }
        holder.view.source_layout.setOnClickListener {
            var currentSource = sourcesList.get(position)
            if (sourcesList.get(position).isSelected) {
                holder.view.checkmark.setImageResource(android.R.drawable.checkbox_off_background)
                sourcesList.get(position).isSelected = false
                selectedSources.remove(sourcesList.get(position).name)
                SharedPreferenceHelper.saveSharedPreferenceArrayList(holder.view.context, selectedSources)
            } else {
                holder.view.checkmark.setImageResource(android.R.drawable.checkbox_on_background)
                sourcesList.get(position).isSelected = true
                selectedSources.add(sourcesList.get(position).name)
                SharedPreferenceHelper.saveSharedPreferenceArrayList(holder.view.context, selectedSources)
            }

            for(source in selectedSources) {
                Log.d("test", source)
            }
        }
    }

    class SourcesViewModel(var view: View) : RecyclerView.ViewHolder(view) {
    }
}