package com.articlesheadlinesapp.ui.main

import android.content.Context
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

class SavedArticleAdapter(private val savedArticlesList: ArrayList<Article>) :
    RecyclerView.Adapter<SavedArticleAdapter.SavedArticleViewHolder>() {

    lateinit var itemSelectedListener: ItemSelectedListener

    fun updateDetailsList(mlistener: ItemSelectedListener, newsavedArticlesList: List<Article>) {
        savedArticlesList.clear()
        savedArticlesList.addAll(newsavedArticlesList)
        savedArticlesList.removeIf {
            it.title.isNullOrEmpty() &&
                    it.description.isNullOrEmpty() && it.urlToImage.isNullOrEmpty()
        }
        itemSelectedListener = mlistener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.headlines_item_layout, parent, false)
        return SavedArticleViewHolder(view)
    }

    override fun getItemCount(): Int = savedArticlesList.size

    override fun onBindViewHolder(holder: SavedArticleViewHolder, position: Int) {
        holder.itemView.title.text = savedArticlesList.get(position).title
        holder.itemView.description.text = savedArticlesList.get(position).description
        holder.itemView.author.text = savedArticlesList.get(position).author
        Glide.with(holder.itemView.thumbnailImage.context)
            .load(savedArticlesList.get(position).urlToImage)
            .into(holder.itemView.thumbnailImage)
        holder.itemView.headlines_layout.setOnClickListener {
            val intent = Intent(it.context, ArticleDetailActivity::class.java)
            intent.putExtra(GlobalConstants.ARTICLE, savedArticlesList.get(position))
            it.context.startActivity(intent)
        }
        holder.itemView.close_icon.visibility = View.VISIBLE
        holder.itemView.close_icon.setOnClickListener {
            itemSelectedListener.onItemSelected(savedArticlesList.get(position))
        }
    }

    class SavedArticleViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface ItemSelectedListener {
        fun onItemSelected(item: Article)
    }

}