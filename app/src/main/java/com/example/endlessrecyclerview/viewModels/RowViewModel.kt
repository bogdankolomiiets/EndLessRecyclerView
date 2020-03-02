package com.example.endlessrecyclerview.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.endlessrecyclerview.Constants.Companion.ITEMS_PER_PAGE
import com.example.endlessrecyclerview.Constants.Companion.START_PAGE
import com.example.endlessrecyclerview.Repository
import com.example.endlessrecyclerview.adapters.RowRecyclerViewAdapter
import com.example.endlessrecyclerview.models.Row
import kotlinx.coroutines.*

class RowViewModel: ViewModel() {

    private val rowRecyclerViewAdapter: RowRecyclerViewAdapter = RowRecyclerViewAdapter()
    private val mRowList = MutableLiveData<List<Row>>()
    val rowList: LiveData<List<Row>>
    get() = mRowList

    init {
        getRows(START_PAGE)
    }

    fun getRowRecyclerViewAdapter() = rowRecyclerViewAdapter

    fun loadMoreRows(page: Int) {
        getRows(page)
    }

    private fun getRows(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            val result = Repository.getNewPortionOfData(page, ITEMS_PER_PAGE)
            if (result.isNotEmpty()) {
                mRowList.postValue(result)
            }
        }
    }

    fun clearList() {
        mRowList.value = listOf()
    }
}