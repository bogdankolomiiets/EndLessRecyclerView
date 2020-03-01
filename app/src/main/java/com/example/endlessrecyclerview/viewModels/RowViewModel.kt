package com.example.endlessrecyclerview.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.endlessrecyclerview.Repository
import com.example.endlessrecyclerview.adapters.RowRecyclerViewAdapter
import com.example.endlessrecyclerview.models.Row
import com.example.endlessrecyclerview.utils.PageConfigurator
import com.example.endlessrecyclerview.utils.PageConfigurator.Companion.CURRENT_PAGE
import com.example.endlessrecyclerview.utils.PageConfigurator.Companion.ITEMS_PER_PAGE
import kotlinx.coroutines.*

class RowViewModel: ViewModel() {

    private val rowRecyclerViewAdapter: RowRecyclerViewAdapter = RowRecyclerViewAdapter()
    private val mRowList = MutableLiveData<List<Row>>()
    val rowList: LiveData<List<Row>>
    get() = mRowList

    init {
        getRows()
    }

    fun getRowRecyclerViewAdapter() = rowRecyclerViewAdapter

    fun onLoadMore(): Boolean {
        return getRows()
    }

    private fun getRows(): Boolean {
            return if (PageConfigurator.isPageAvailable()) {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(500)
                    val result = Repository.getNewPortionOfData(CURRENT_PAGE, ITEMS_PER_PAGE)
                    if (result.isNotEmpty()) {
                        mRowList.postValue(result)
                        CURRENT_PAGE++
                    }
                }
                true
            } else false
    }

    fun clearList() {
        mRowList.value = listOf()
    }
}