package com.example.endlessrecyclerview.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.endlessrecyclerview.Constants.Companion.START_PAGE

abstract class EndlessRecyclerViewScrollListener(private val mLinearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    // The minimum number of items to have below your current scroll position
    // before loading more.
    private val visibleThreshold = 5

    // Sets the starting page index
    private val startingPageIndex = START_PAGE
    // The current offset index of data you have loaded
    private var currentPage = START_PAGE

    // True if we are still waiting for the last set of data to load.
    private var loading: Boolean = true

    // The total number of items in the dataSet after the last load
    private var previousTotalItemCount = 0
    private var totalItemCount: Int = 0
    private var firstVisibleItem: Int = 0
    private var lastVisibleItem: Int = 0
    private var visibleItemCount: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()
        visibleItemCount = recyclerView.childCount
        totalItemCount = mLinearLayoutManager.itemCount
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition()

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            currentPage = this.startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItem + visibleThreshold) > totalItemCount) {
            onLoadMore(currentPage++, totalItemCount)
            loading = true
        }
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    fun setDefaults() {
        previousTotalItemCount = 0
        currentPage = START_PAGE
//        loading = true
    }

    fun getCurrentPage() = currentPage
}