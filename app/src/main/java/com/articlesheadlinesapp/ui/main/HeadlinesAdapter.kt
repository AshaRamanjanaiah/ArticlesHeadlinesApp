package com.articlesheadlinesapp.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.articlesheadlinesapp.R
import com.articlesheadlinesapp.Utils.GlobalConstants
import com.articlesheadlinesapp.model.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.headlines_item_layout.view.*

class HeadlinesAdapter(private val headlinesList: ArrayList<Article>) :
    RecyclerView.Adapter<HeadlinesAdapter.HeadlinesViewHolder>() {

    fun updateDetailsList(newDetailsList: List<Article>) {
        headlinesList.clear()
        headlinesList.addAll(newDetailsList)
        headlinesList.removeIf {
            it.title.isNullOrEmpty() &&
                    it.description.isNullOrEmpty() && it.urlToImage.isNullOrEmpty()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlinesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.headlines_item_layout, parent, false)
        return HeadlinesViewHolder(view)
    }

    override fun getItemCount(): Int = headlinesList.size

    override fun onBindViewHolder(holder: HeadlinesViewHolder, position: Int) {
        holder.itemView.title.text = headlinesList.get(position).title
        holder.itemView.description.text = headlinesList.get(position).description
        holder.itemView.author.text = headlinesList.get(position).author
        Glide.with(holder.itemView.thumbnailImage.context)
            .load(headlinesList.get(position).urlToImage)
            .into(holder.itemView.thumbnailImage)
        holder.itemView.headlines_layout.setOnClickListener {
            val intent = Intent(it.context, ArticleDetailActivity::class.java)
            intent.putExtra(GlobalConstants.ARTICLE, headlinesList.get(position))
            it.context.startActivity(intent)
        }
    }

    class HeadlinesViewHolder(view: View) : RecyclerView.ViewHolder(view)

}