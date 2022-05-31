package com.robosoft.news.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.robosoft.news.R
import com.robosoft.news.common.Constants
import com.robosoft.news.data.remote.dto.Article
import com.robosoft.news.databinding.FragmentHomeBinding
import com.robosoft.news.domain.model.PopularNews
import com.robosoft.news.domain.model.TopNews
import com.robosoft.news.presentation.common.PopularNewsFetchState
import com.robosoft.news.presentation.common.TopNewsFetchState
import com.robosoft.news.presentation.common.adapter.PopularNewsAdapter
import com.robosoft.news.presentation.common.viewmodel.NewsViewModel
import com.robosoft.news.presentation.common.viewmodel.NewsViewModel.Companion.CATEGORY
import com.robosoft.news.presentation.common.viewmodel.NewsViewModel.Companion.COUNTRY
import com.robosoft.news.presentation.common.viewmodel.NewsViewModel.Companion.PAGE_SIZE
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment(), PopularNewsAdapter.PopularNewsItemClickListener {

    private val newsViewModel by sharedViewModel<NewsViewModel>()
    private lateinit var homeViewBinding: FragmentHomeBinding
    private lateinit var popularNewsAdapter: PopularNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout
        homeViewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.app_name)
        homeViewBinding.topNewsSectionHeader.title.text = getString(R.string.title_top_news_header)
        homeViewBinding.popularNewsSectionHeader.title.text =
            getString(R.string.title_popular_news_header)

        initRecyclerView()
        observeTopNewsFetchState()
        observePopularNewsFetchState()
        enablePagination()
        // Fetch news if view model has no news
        val topNewsValue = newsViewModel.topNewsFetchState.value
        val topNews = (topNewsValue as? TopNewsFetchState.Success)?.topNews
        if (topNews == null) {
            newsViewModel.fetchTopNews(COUNTRY)
        }
        val value = newsViewModel.popularNewsFetchState.value
        val news = (value as? PopularNewsFetchState.Success)?.popularNews
        if (news?.articles?.size ?: 0 <= 0) {
            newsViewModel.fetchPopularNews(
                country = COUNTRY,
                category = CATEGORY,
                pageSize = PAGE_SIZE,
                page = 0
            )
        }
    }

    /**
     * Initializes the RecyclerView
     */
    private fun initRecyclerView() {
        homeViewBinding.popularNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            popularNewsAdapter = PopularNewsAdapter(this@HomeFragment)
            adapter = popularNewsAdapter
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
        }
    }

    /**
     * Observes to news fetch state changes
     */
    private fun observeTopNewsFetchState() {
        newsViewModel.topNewsFetchState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleTopNewsStateChange(state) }
            .launchIn(lifecycleScope)
    }

    /**
     * Observes to popular news fetch state changes
     */
    private fun observePopularNewsFetchState() {
        newsViewModel.popularNewsFetchState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handlePopularNewsStateChange(state) }
            .launchIn(lifecycleScope)
    }

    /**
     * Handles popular news fetch state changes
     */
    private fun handlePopularNewsStateChange(state: PopularNewsFetchState) {
        when (state) {
            is PopularNewsFetchState.Init -> Unit
            is PopularNewsFetchState.Error -> handleError(state.error.toString())
            is PopularNewsFetchState.Success -> handlePopularNews(state.popularNews)
            is PopularNewsFetchState.IsLoading -> handleLoading(
                state.isLoading,
                homeViewBinding.popularNewsProgressBar
            )
        }
    }

    /**
     * Handles top news fetch state changes
     */
    private fun handleTopNewsStateChange(state: TopNewsFetchState) {
        when (state) {
            is TopNewsFetchState.Init -> Unit
            is TopNewsFetchState.Error -> handleError(state.error.toString())
            is TopNewsFetchState.Success -> handleTopNews(state.topNews)
            is TopNewsFetchState.IsLoading -> handleLoading(
                state.isLoading,
                homeViewBinding.topNewsProgressBar
            )
        }
    }

    /**
     * Handles the visibility of the progress bar
     */
    private fun handleLoading(loading: Boolean, progressBar: ProgressBar) {
        if (loading) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
    }

    /**
     * Handles top news response
     *
     * @param news fetched news
     */
    private fun handleTopNews(news: TopNews) {
        with(homeViewBinding) {
            // Set the news information
            topNews.newsBody.newsTitle.text = news.title
            topNews.newsBody.newsDescription.text = news.description
            topNews.newsBody.sourceButton.text = news.source?.name
            // Set the News image asynchronously
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(root.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(news.urlToImage)
                .fitCenter()
                .into(topNews.imageView)
        }
    }

    /**
     * Handles popular news response
     *
     * @param news fetched news
     */
    private fun handlePopularNews(news: PopularNews) {
        popularNewsAdapter.appendNews(news.articles)
    }

    private fun handleError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(item: Article, position: Int) {
        newsViewModel.savedStateHandle.set(Constants.PARAM_SELECTED_NEWS_URL, item.url)
        findNavController().navigate(R.id.action_homeFragment_to_newsDetailsFragment)
    }

    /**
     * Enables pagination for lazy loading of popular news
     */
    private fun enablePagination() {
        with(homeViewBinding) {
            nestedScrollView.viewTreeObserver?.addOnScrollChangedListener {
                if (!nestedScrollView.canScrollVertically(1)) {
                    val value = newsViewModel.popularNewsFetchState.value
                    val news = (value as? PopularNewsFetchState.Success)?.popularNews
                    val totalResults = news?.totalResults?.toInt() ?: 0
                    if (popularNewsAdapter.itemCount < totalResults) {
                        popularNewsProgressBar.bringToFront()
                        newsViewModel.fetchPopularNews(
                            country = COUNTRY,
                            category = CATEGORY,
                            pageSize = PAGE_SIZE,
                            page = (popularNewsAdapter.itemCount) / PAGE_SIZE
                        )
                    }
                }
            }
        }
    }
}
