package com.robosoft.news.presentation.common.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.robosoft.news.R
import com.robosoft.news.data.remote.dto.Article
import com.robosoft.news.databinding.ItemPopularNewsBinding

/**
 * Recycler View Adapter to list the popular news
 *
 * @param popularNewsItemClickListener a click listener
 */
class PopularNewsAdapter(private val popularNewsItemClickListener: PopularNewsItemClickListener) :
    RecyclerView.Adapter<PopularNewsAdapter.NewsViewHolder>() {

    private val popularNews: MutableList<Article> = mutableListOf()

    /**
     * News click listener contract
     */
    interface PopularNewsItemClickListener {
        fun onItemClick(item: Article, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPopularNewsBinding.inflate(layoutInflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount() = popularNews.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = popularNews[position]
        holder.bind(news)
    }

    /**
     * Appends paginated news
     */
    fun appendNews(articles: List<Article>) {
        popularNews.addAll(articles)
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(
        private val binding: ItemPopularNewsBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            popularNewsItemClickListener.onItemClick(popularNews[adapterPosition], adapterPosition)
        }

        fun bind(news: Article) {
            with(binding) {
                newsBody.newsTitle.setLines(1)
                newsBody.newsTitle.ellipsize = TextUtils.TruncateAt.END

                newsBody.newsDescription.setLines(2)
                newsBody.newsDescription.ellipsize = TextUtils.TruncateAt.END

                // Set the news information
                newsBody.newsTitle.text = news.title
                newsBody.newsDescription.text = news.description
                newsBody.sourceButton.text = news.source.name
                // Set the News image asynchronously
                val requestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)

                Glide.with(itemView.context)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(news.urlToImage)
                    .fitCenter()
                    .into(imageView)
            }
        }
    }

}