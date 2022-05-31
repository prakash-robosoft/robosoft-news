package com.robosoft.news.presentation.newsdetails

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosoft.news.R
import com.robosoft.news.common.Constants
import com.robosoft.news.data.remote.dto.Article
import com.robosoft.news.databinding.FragmentNewsDetailsBinding
import com.robosoft.news.presentation.common.PopularNewsFetchState
import com.robosoft.news.presentation.common.adapter.PopularNewsAdapter
import com.robosoft.news.presentation.common.viewmodel.NewsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class NewsDetailsFragment : Fragment(), PopularNewsAdapter.PopularNewsItemClickListener {

    private val newsViewModel by sharedViewModel<NewsViewModel>()
    private lateinit var viewBinding: FragmentNewsDetailsBinding
    private lateinit var popularNewsAdapter: PopularNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        // Inflate the layout
        viewBinding = FragmentNewsDetailsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.popularNewsSectionHeader.title.text =
            getString(R.string.title_popular_news_header)
        initRecyclerView()
        val value = newsViewModel.popularNewsFetchState.value
        val news = (value as? PopularNewsFetchState.Success)?.popularNews
        news?.articles?.subList(0, 2)?.let { popularNewsAdapter.appendNews(it) }
        prepareWebView()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem: MenuItem = menu.findItem(R.id.searchMenu)
        searchItem.isVisible = false
        val bookmarkItem: MenuItem = menu.findItem(R.id.bookmarkMenu)
        bookmarkItem.isVisible = false
    }

    override fun onItemClick(item: Article, position: Int) {}

    /**
     * Initializes the recycler view
     */
    private fun initRecyclerView() {
        viewBinding.popularNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            popularNewsAdapter = PopularNewsAdapter(this@NewsDetailsFragment)
            adapter = popularNewsAdapter
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
        }
    }

    /**
     * Handles the visibility of progress bar
     */
    private fun handleLoading(loading: Boolean) {
        if (loading) viewBinding.progressBar.visibility =
            View.VISIBLE else viewBinding.progressBar.visibility = View.GONE
    }

    /**
     * Loads selected news url
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun prepareWebView() {
        viewBinding.webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                handleLoading(true)
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
                false

            override fun onPageFinished(view: WebView, url: String) {
                handleLoading(false)
                viewBinding.popularNewsContainerLayout.visibility = View.VISIBLE
            }
        }
        viewBinding.webView.settings.javaScriptEnabled = true
        newsViewModel.savedStateHandle.get<String>(Constants.PARAM_SELECTED_NEWS_URL)?.let { url ->
            activity?.title = url
            viewBinding.webView.loadUrl(url)
        }
    }
}