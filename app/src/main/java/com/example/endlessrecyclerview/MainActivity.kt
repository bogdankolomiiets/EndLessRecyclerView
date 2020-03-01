package com.example.endlessrecyclerview

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bohdan.kolomiiets.eventsreminder.base.adapter.BaseAdapterCallback
import com.bohdan.kolomiiets.eventsreminder.base.adapter.EndlessRecyclerViewScrollListener
import com.example.endlessrecyclerview.adapters.RowRecyclerViewAdapter
import com.example.endlessrecyclerview.adapters.CustomItemCallback
import com.example.endlessrecyclerview.models.Row
import com.example.endlessrecyclerview.utils.PageConfigurator.Companion.CURRENT_PAGE
import com.example.endlessrecyclerview.utils.PageConfigurator.Companion.START_PAGE
import com.example.endlessrecyclerview.viewModels.RowViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progress: ProgressBar
    private lateinit var noDataTv: TextView
    private lateinit var rowViewModel: RowViewModel
    private lateinit var rowRecyclerViewAdapter: RowRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init UI components
        recyclerView = findViewById(R.id.recycler_view)
        progress = findViewById(R.id.progress)
        noDataTv = findViewById(R.id.no_data_tv)

        //init RowViewModel
        rowViewModel = ViewModelProvider(this).get(RowViewModel::class.java)

        //setup recyclerView
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        rowRecyclerViewAdapter = rowViewModel.getRowRecyclerViewAdapter()
        recyclerView.adapter = rowRecyclerViewAdapter

        rowRecyclerViewAdapter.attachBaseCallback(object : BaseAdapterCallback<Row> {
            override fun onItemClick(model: Row, view: View) {
                Toast.makeText(this@MainActivity, "onItemClick", Toast.LENGTH_SHORT).show()
            }

            override fun onLongItemClick(model: Row, view: View): Boolean {
                Toast.makeText(this@MainActivity, "onLongItemClick", Toast.LENGTH_SHORT).show()
                return true
            }
        })

        rowRecyclerViewAdapter.attachCustomItemCallback(object : CustomItemCallback {
            override fun onToastBtn(model: Row, position: Int) {
                Toast.makeText(this@MainActivity, model.title, Toast.LENGTH_SHORT).show()
            }

            override fun onSnackBtn(model: Row, position: Int) {
                Snackbar.make(recyclerView, model.title, Snackbar.LENGTH_SHORT).show()
            }
        })

        rowRecyclerViewAdapter.itemsCount.observe(this, Observer {
            noDataTv.isVisible = it == 0
        })

        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(): Boolean {
                val result = rowViewModel.onLoadMore()
                if (result) showProgressBar()
                return result
            }
        })

        rowViewModel.rowList.observe(this, Observer {
            rowRecyclerViewAdapter.setData(it, CURRENT_PAGE > START_PAGE)
            hideProgressBar()
        })
    }

    private fun showProgressBar() {
        progress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progress.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        rowRecyclerViewAdapter.detachCustomItemCallback()
        rowViewModel.clearList()
    }
}