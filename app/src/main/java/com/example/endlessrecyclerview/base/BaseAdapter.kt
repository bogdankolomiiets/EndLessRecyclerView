package com.bohdan.kolomiiets.eventsreminder.base.adapter

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {
    private var mDataList: MutableList<T> = ArrayList()
    private var mCallback: BaseAdapterCallback<T>? = null
    private lateinit var mApplicationContext: Context
    private val mItemsCount = MutableLiveData<Int>()
    val itemsCount: LiveData<Int>
    get() = mItemsCount

    fun attachBaseCallback(callback: BaseAdapterCallback<T>) {
        mCallback = callback
    }

    fun detachBaseCallback() {
        mCallback = null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mApplicationContext = recyclerView.context.applicationContext
    }

    open fun setData(dataList: List<T>, append: Boolean) {
        if (append) {
            val startPosition = mDataList.size
            mDataList.addAll(dataList)
            notifyItemRangeInserted(startPosition.inc(), mDataList.size)
        } else {
            mDataList.apply {
                clear()
                addAll(dataList)
            }
            notifyDataSetChanged()
        }
        postItemsCount()
    }

    fun addItem(newItem: T) {
        mDataList.add(newItem)
        notifyItemInserted(mDataList.size.dec())
        postItemsCount()
    }

    fun addItemToTop(newItem: T) {
        mDataList.add(0, newItem)
        notifyItemInserted(0)
        postItemsCount()
    }

    open fun removeItem(model: T) {
        val indexOf = mDataList.indexOf(model)
        if (indexOf >= 0) {
            mDataList.removeAt(indexOf)
            notifyItemRemoved(indexOf)
        }
        postItemsCount()
    }

    open fun removeItem(indexOf: Int) {
        try {
            mDataList.removeAt(indexOf)
            notifyItemRemoved(indexOf)
        } catch (e: Exception) {
            if (::mApplicationContext.isInitialized) {
                Toast.makeText(mApplicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        postItemsCount()
    }

    fun clearData() {
        mDataList.clear()
        notifyDataSetChanged()
        postItemsCount()
    }

    fun getData() = mDataList as List<T>

    private fun postItemsCount() {
        mItemsCount.postValue(mDataList.size)
    }

    override fun getItemCount() = mDataList.count()

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(mDataList[position], position)
        holder.itemView.setOnClickListener{ mCallback?.onItemClick(mDataList[position], holder.itemView) }
        holder.itemView.setOnLongClickListener {
            return@setOnLongClickListener if (mCallback == null) {
                false
            } else {
                mCallback!!.onLongItemClick(mDataList[position], holder.itemView)
                true
            }
        }
    }
}