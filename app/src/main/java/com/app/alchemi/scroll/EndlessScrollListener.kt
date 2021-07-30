package com.app.alchemi.scroll

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Extension of [RecyclerView.OnScrollListener] for endless scrolling
 * @param loadingThreshold the threshold to the end of the list
 * where loading more items is triggered
 * @param onReachedEndOfList the function that is invoked when the end of
 * the list (minus the threshold) is reached, e.g. load more data
 */
class EndlessScrollListener(private val loadingThreshold: Int, private val onReachedEndOfList: () -> Unit)
    : RecyclerView.OnScrollListener() {

    private var totalItemCount: Int? = 0

    private var firstVisibleItemPos = 0

    private var visibleItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        // only check for end of list if the user scrolled down
        if (dy > 0) {
            checkLoadMore(recyclerView)
        }
    }

    /**
     * Check if the conditions are met to load the next page
     * @param recyclerView the [RecyclerView] that is checked for the conditions
     */
    private fun checkLoadMore(recyclerView: RecyclerView) {
        visibleItemCount = recyclerView.childCount
        totalItemCount = recyclerView.layoutManager?.itemCount
        firstVisibleItemPos = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        if ((totalItemCount?.minus(visibleItemCount)) ?: 0 <= (firstVisibleItemPos + loadingThreshold)) {
            onReachedEndOfList()
        }
    }

}