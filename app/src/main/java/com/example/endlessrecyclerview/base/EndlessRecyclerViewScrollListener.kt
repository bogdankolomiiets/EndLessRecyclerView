package com.bohdan.kolomiiets.eventsreminder.base.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(private val mLinearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    // The minimum number of items to have below your current scroll position
    // before loading more.
    private val visibleThreshold = 5

    // True if we are still waiting for the last set of data to load.
    private var loading: Boolean = true

    // The total number of items in the dataSet after the last load
    private var previousTotalItemCount = 0
    private var totalItemCount: Int = 0
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()
        visibleItemCount = recyclerView.childCount
        totalItemCount = mLinearLayoutManager.itemCount

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }
        // If it’s still loading, we check to see if the dataSet count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        // If it’s still loading, we check to see if the dataSet count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            onLoadMore()
            loading = true
        }
    }

    abstract fun onLoadMore(): Boolean

}